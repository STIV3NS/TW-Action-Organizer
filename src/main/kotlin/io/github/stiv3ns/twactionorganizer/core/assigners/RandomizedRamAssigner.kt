package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class RandomizedRamAssigner internal constructor(
    name: String,
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
    type: AssignerType
) : StandardRamAssigner(name, targets, resources, mainReferencePoint, type)
{
    override fun processAllyVillage(allyVillage: AllyVillage) {
        val randomTarget = targets.random()

        assign(allyVillage, randomTarget, allyVillage distanceTo randomTarget)
        randomTarget.attack()

        allyVillages.remove(allyVillage)
        if (randomTarget.isAssignCompleted()) {
            targets.remove(randomTarget)
        }
    }
}