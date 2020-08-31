package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.util.*

open class StandardRamAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
    type: AssignerType = AssignerType.RAM
) : Assigner(targets, resources, mainReferencePoint, type)
{
    override val resourcesQueue = PriorityQueue<Pair<AllyVillage, Int>>(
        resources.villageCount,
        distanceComparator.reversed()
    )



    override fun call(): AssignerReport {
        putResourcesToQueue(referencePoint = mainReferencePoint)

        while (resourcesQueue.isNotEmpty() && targets.isNotEmpty()) {
            val (allyVillage, _) = resourcesQueue.poll()
            processAllyVillage(allyVillage)
        }

        return prepareReport()
    }

    protected open fun processAllyVillage(allyVillage: AllyVillage) {
        putTargetsToQueue(referencePoint = allyVillage)

        val (nearestTarget, distance) = targetsQueue.poll()

        assign(allyVillage, nearestTarget, distance)
        nearestTarget.attack()

        allyVillages.remove(allyVillage)
        if (nearestTarget.isAssignCompleted()) {
            targets.remove(nearestTarget)
        }
    }
}