package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class ReversedRamAssigner internal constructor(
    name: String,
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
    type: AssignerType = AssignerType.REVERSED_RAM
) : Assigner(name, targets, resources, mainReferencePoint, type)
{
    override fun call(): AssignerReport {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() && allyVillages.isNotEmpty()) {
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

            assign(nearestAllyVillage, target, distance)

            allyVillages.remove(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted())
            targets.remove(target)
    }
}