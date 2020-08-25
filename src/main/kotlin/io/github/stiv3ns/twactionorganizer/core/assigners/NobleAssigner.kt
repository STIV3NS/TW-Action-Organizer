package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class NobleAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Collection<AllyVillage>,
    mainReferencePoint: Village,
    isAssigningFakes: Boolean,
    protected val maxNobleRange: Int
) : Assigner(targets, resources, mainReferencePoint, isAssigningFakes)
{
    override val offAction = Player::putNobleAssignment
    override val fakeAction = Player::putFakeNobleAssignment

    private val resWithNobles = resources.filter { it.owner.hasNoble() }.toMutableList()

    override fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        resWithNobles.forEach { resourcesQueue.offer(Pair(it, it distanceTo referencePoint)) }
    }


    override fun call(): AssignerReport {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() && resWithNobles.isNotEmpty()) {
            val (target, _) = targetsQueue.poll()
            handleTarget(target)
        }

        return AssignerReport(
            unusedResourceVillages = resources,
            unassignedTargetVillages = targets
        )
    }

    private fun handleTarget(target: TargetVillage) {
        putResourcesToQueue(referencePoint = target)

        for ((nearestAllyVillage, distance) in resourcesQueue) {
            if (target.isAssignCompleted())
                break

            if (distance > maxNobleRange)
                break

            if (!nearestAllyVillage.owner.hasNoble())
                continue

            assign(nearestAllyVillage, target, distance)
            updateOwnerAndResources(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted())
            targets.remove(target)
    }

    private fun updateOwnerAndResources(nearestAllyVillage: AllyVillage) {
        nearestAllyVillage.owner.delegateNoble()

        resources.remove(nearestAllyVillage)

        if (!nearestAllyVillage.owner.hasNoble())
            updateResWithNobles()
        else
            resWithNobles.remove(nearestAllyVillage)
    }

    private fun updateResWithNobles() {
        resWithNobles.removeAll { !it.owner.hasNoble() }
    }
}