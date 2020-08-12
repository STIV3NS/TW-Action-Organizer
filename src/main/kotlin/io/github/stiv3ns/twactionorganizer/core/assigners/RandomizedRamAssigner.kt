package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.VillageAssignment
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class RandomizedRamAssigner internal constructor(
    targets: MutableList<TargetVillage>,
    resources: MutableList<AllyVillage>,
    isAssigningFakes: Boolean
) : Assigner(targets, resources, Village(0,0), isAssigningFakes) {

    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment

    override fun call(): AssignerReport {
        while (targets.isNotEmpty() && resources.isNotEmpty()) {
            val randomTarget = targets.random()
            handleTarget(randomTarget)
        }

        return AssignerReport(
            unusedResourceVillages = resources,
            unassignedTargetVillages = targets
        )
    }

    private fun handleTarget(target: TargetVillage) {
        while (resources.isNotEmpty()) {
            if (target.isAssignCompleted()) {
                break;
            }

            val randomResource = resources.random()
            resources.remove(randomResource)

            assign(randomResource, target, randomResource distanceTo target)
            target.attack()

            if (target.isAssignCompleted()) {
                targets.remove(target)
            }
        }
    }
}