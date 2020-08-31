package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.util.PriorityQueue

abstract class Assigner internal constructor(
    val name: String,
    targets: Collection<TargetVillage>,
    resources: Resources,
    val mainReferencePoint: Village,
    val type: AssignerType
) {
    protected val targets: MutableCollection<TargetVillage>
    protected val allyVillages: MutableCollection<AllyVillage>
    protected val allyPlayers: Map<String, Player>

    init {
        this.targets = targets.toMutableList()
        this.allyVillages = resources.villages.toMutableList()
        this.allyPlayers = resources.players.associateBy { it.nickname }
    }





    protected val distanceComparator = Comparator.comparing(Pair<Village, Int>::second)

    protected open val targetsQueue = PriorityQueue<Pair<TargetVillage, Int>>(
        targets.size,
        distanceComparator
    )

    protected open val resourcesQueue = PriorityQueue(
        resources.villageCount,
        distanceComparator
    )

    protected open fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        allyVillages.forEach { allyVillage ->
            resourcesQueue.offer(
                Pair(allyVillage, allyVillage distanceTo referencePoint))
        }
    }

    protected open fun putTargetsToQueue(referencePoint: Village) {
        targetsQueue.clear()
        targets.forEach { target ->
            targetsQueue.offer(
                Pair(target, target distanceTo referencePoint))
        }
    }






    protected val assignments = mutableListOf<Assignment>()

    protected fun assign(allyVillage: AllyVillage, targetVillage: TargetVillage, distance: Int) {
        val assignment = Assignment(
            departure = allyVillage,
            destination = targetVillage,
            squaredDistance = distance,
            delayInMinutes = targetVillage.delayInMinutes,
            type = type
        )

        assignments.add(assignment)
    }





    abstract fun call(): AssignerReport

    open fun prepareReport(): AssignerReport =
        AssignerReport(
            name = name,
            result = assignments,
            unusedResources = Resources(players = allyPlayers.values, villages = allyVillages),
            unassignedTargets = targets
        )
}