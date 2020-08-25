package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.util.*
import java.util.concurrent.Callable
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.runBlocking

abstract class Assigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Collection<AllyVillage>,
    protected val mainReferencePoint: Village,
    protected val isAssigningFakes: Boolean
) : Callable<AssignerReport>
{
    protected val targets: MutableCollection<TargetVillage>
    protected val resources: MutableCollection<AllyVillage>

    init {
        this.targets = targets.toMutableList()
        this.resources = resources.toMutableList()
    }

    protected val distanceComparator = Comparator.comparing(Pair<Village, Int>::second)

    protected open val targetsQueue = PriorityQueue<Pair<TargetVillage, Int>>(
        targets.size,
        distanceComparator
    )

    protected open val resourcesQueue = PriorityQueue<Pair<AllyVillage, Int>>(
        resources.size,
        distanceComparator
    )

    protected abstract val offAction: Player.(Assignment) -> Unit
    protected abstract val fakeAction: Player.(Assignment) -> Unit

    protected open fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        resources.forEach { resourcesQueue.offer(Pair(it, it distanceTo referencePoint)) }
    }

    protected open fun putTargetsToQueue(referencePoint: Village) {
        targetsQueue.clear()
        targets.forEach { targetsQueue.offer(Pair(it, it distanceTo referencePoint)) }
    }

    protected fun assign(allyVillage: AllyVillage, targetVillage: TargetVillage, distance: Int) {
        val assignment = Assignment(
            departure = allyVillage,
            destination = targetVillage,
            squaredDistance = distance,
            delayInMinutes = targetVillage.delayInMinutes
        )

        when (isAssigningFakes) {
            true -> allyVillage.owner.fakeAction(assignment)
            false -> allyVillage.owner.offAction(assignment)
        }
    }
}