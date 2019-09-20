package io.github.stiv3ns.twactionorganizer.twao.assigners

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.VillageAssignment
import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.util.*

abstract class Assigner internal constructor(
        protected val targets: MutableList<TargetVillage>,
        protected val resources: MutableList<AllyVillage>,
        protected val mainReferencePoint: Village,
        protected val isAssigningFakes: Boolean
) : Runnable {

    protected abstract val targetsQueue: PriorityQueue< Pair<TargetVillage, Int> >
    protected abstract val resourcesQueue: PriorityQueue< Pair<AllyVillage, Int> >

    protected abstract val offAction: Player.(VillageAssignment) -> Unit
    protected abstract val fakeAction: Player.(VillageAssignment) -> Unit

    protected open fun putResourcesToQueue(referencePoint: Village) {
        resources.forEach { resourcesQueue.offer( Pair(it, Village.distance(it, referencePoint)) ) }
    }

    protected open fun putTargetsToQueue(referencePoint: Village) {
        targets.forEach { targetsQueue.offer( Pair(it, Village.distance(it, referencePoint)) ) }
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