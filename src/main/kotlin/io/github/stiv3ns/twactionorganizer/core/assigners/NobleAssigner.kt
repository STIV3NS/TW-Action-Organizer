package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class NobleAssigner internal constructor(
    name: String,
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
    type: AssignerType,
    val maxNobleRange: Int,
    val minNobleRange: Int
) : Assigner(name, targets, resources, mainReferencePoint, type)
{
    protected val playerAvailableNobles: MutableMap<Player, Int>
    protected val villagesWithAvailableNobles: MutableCollection<AllyVillage>

    protected fun AllyVillage.owner(): Player =
        allyPlayers[ ownerNickname ]
            ?: throw IllegalArgumentException("Invalid Resources.")

    protected open fun Player.hasNoble(): Boolean =
        (playerAvailableNobles[this] ?: 0) > 0

    protected open fun Player.delegateNoble() {
        playerAvailableNobles[this] = (playerAvailableNobles[this] ?: 0 ) - 1
    }

    init {
        villagesWithAvailableNobles = allyVillages.filter { it.owner().hasNoble() }.toMutableList()

        playerAvailableNobles = resources
                                .players
                                .map { player -> player to player.numberOfNobles }
                                .toMap().toMutableMap()
    }





    override fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        allyVillages.forEach { allyVillage ->
            resourcesQueue.offer(
                Pair(allyVillage, allyVillage distanceTo referencePoint))
        }
    }


    override fun prepareReport(): AssignerReport =
        AssignerReport(
            name = name,
            result = assignments,

            unassignedTargets = attacksNeeded
                                .mapNotNull { (target, numberOfMissingAttacks) ->
                                    if (numberOfMissingAttacks < 1)
                                        null
                                    else
                                        target.copy(numberOfAttacks = numberOfMissingAttacks)
                                }.toList(),

            unusedResources = Resources(villages = allyVillages,
                                        players = playerAvailableNobles
                                                    .map { (player, availableNobles) ->
                                                        Player(player.nickname, availableNobles)
                                                    })
        )

    override fun call(): AssignerReport {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() and allyVillages.isNotEmpty()) {
            val (target, _) = targetsQueue.poll()
            handleTarget(target)
        }

        return prepareReport()
    }

    protected open fun handleTarget(target: TargetVillage) {
        putResourcesToQueue(referencePoint = target)

        for ((nearestAllyVillage, distance) in resourcesQueue) {
            if (target.isAssignCompleted())
                break

            if (distance < minNobleRange)
                continue
            if (distance > maxNobleRange)
                break

            if (!nearestAllyVillage.owner().hasNoble())
                continue

            assign(nearestAllyVillage, target, distance)
            updateOwnerAndResources(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted())
            targets.remove(target)
    }

    protected open fun updateOwnerAndResources(nearestAllyVillage: AllyVillage) {
        nearestAllyVillage.owner().delegateNoble()

        allyVillages.remove(nearestAllyVillage)
        villagesWithAvailableNobles.remove(nearestAllyVillage)

        val owner = nearestAllyVillage.owner()
        if (owner.hasNoble() == false)
            villagesWithAvailableNobles.removeAll { it.owner() == owner }
    }
}