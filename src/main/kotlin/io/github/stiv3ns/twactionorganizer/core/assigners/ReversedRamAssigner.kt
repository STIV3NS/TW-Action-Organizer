package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class ReversedRamAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Collection<AllyVillage>,
    mainReferencePoint: Village,
    isAssigningFakes: Boolean
) : Assigner(targets, resources, mainReferencePoint, isAssigningFakes)
{
    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment

    override fun call(): AssignerReport {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() && resources.isNotEmpty()) {
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

            assign(nearestAllyVillage, target, distance)

            resources.remove(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted())
            targets.remove(target)
    }
}