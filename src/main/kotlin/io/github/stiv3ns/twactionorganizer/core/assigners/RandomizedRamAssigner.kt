package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class RandomizedRamAssigner internal constructor(
    targets: MutableList<TargetVillage>,
    resources: MutableList<AllyVillage>,
    mainReferencePoint: Village,
    isAssigningFakes: Boolean
) : StandardRamAssigner(targets, resources, mainReferencePoint, isAssigningFakes) {

    override fun processAllyVillage(allyVillage: AllyVillage) {
        val randomTarget = targets.random()

        assign(allyVillage, randomTarget, allyVillage distanceTo randomTarget)
        randomTarget.attack()

        resources.remove(allyVillage)
        if (randomTarget.isAssignCompleted()) {
            targets.remove(randomTarget)
        }
    }
}