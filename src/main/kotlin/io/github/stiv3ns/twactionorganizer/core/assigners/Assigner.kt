package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.VillageAssignment
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.util.*
import java.util.concurrent.Callable

abstract class Assigner internal constructor(
    protected val targets: MutableList<TargetVillage>,
    protected val resources: MutableList<AllyVillage>,
    protected val mainReferencePoint: Village,
    protected val isAssigningFakes: Boolean
) : Callable<AssignerReport> {

    protected val distanceComparator = Comparator.comparing(Pair<Village, Int>::second)

    protected open val targetsQueue = PriorityQueue<Pair<TargetVillage, Int>>(
        targets.size,
        distanceComparator
    )

    protected open val resourcesQueue = PriorityQueue<Pair<AllyVillage, Int>>(
        resources.size,
        distanceComparator
    )

    protected abstract val offAction: Player.(VillageAssignment) -> Unit
    protected abstract val fakeAction: Player.(VillageAssignment) -> Unit

    protected open fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        resources.forEach { resourcesQueue.offer(Pair(it, it distanceTo referencePoint)) }
    }

    protected open fun putTargetsToQueue(referencePoint: Village) {
        targetsQueue.clear()
        targets.forEach { targetsQueue.offer(Pair(it, it distanceTo referencePoint)) }
    }

    protected fun assign(allyVillage: AllyVillage, targetVillage: TargetVillage, distance: Int) {
        val assignment = VillageAssignment(
            departure = allyVillage,
            destination = targetVillage,
            squaredDistance = distance
        )

        when (isAssigningFakes) {
            true -> allyVillage.owner.fakeAction(assignment)
            false -> allyVillage.owner.offAction(assignment)
        }
    }
}