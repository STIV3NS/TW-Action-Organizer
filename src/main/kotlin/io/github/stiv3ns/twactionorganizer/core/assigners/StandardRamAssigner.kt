package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.util.*

class StandardRamAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val resourcesQueue = PriorityQueue< Pair<AllyVillage, Int> >(
            resources.size,
            distanceComparator.reversed()
    )

    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment

    override fun call(): AssignerReport {
        putResourcesToQueue(referencePoint = mainReferencePoint)

        while (resourcesQueue.isNotEmpty() && targets.isNotEmpty()) {
            val (allyVillage, _) = resourcesQueue.poll()
            processAllyVillage(allyVillage)
        }

        return AssignerReport(
            unusedResourceVillages = resources,
            unassignedTargetVillages = targets
        )
    }

    private fun processAllyVillage(allyVillage: AllyVillage) {
        putTargetsToQueue(referencePoint = allyVillage)

        val (nearestTarget, distance) = targetsQueue.poll()

        assign(allyVillage, nearestTarget, distance)
        nearestTarget.attack()

        resources.remove(allyVillage)
        if (nearestTarget.isAssignCompleted()) {
            targets.remove(nearestTarget)
        }
    }
}