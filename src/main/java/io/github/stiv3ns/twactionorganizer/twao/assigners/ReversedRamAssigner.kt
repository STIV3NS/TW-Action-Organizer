package io.github.stiv3ns.twactionorganizer.twao.assigners

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.util.*
import kotlin.Comparator

class ReversedRamAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment

    override fun run() {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() && resources.isNotEmpty()) {
            val (target, _) = targetsQueue.poll()
            handleTarget(target)
        }
    }

    private fun handleTarget(target: TargetVillage) {
        putResourcesToQueue(referencePoint = target)

        for ((nearestAllyVillage, distance) in resourcesQueue) {
            if (target.isAssignCompleted()) {
                break
            }

            assign(nearestAllyVillage, target, distance)

            resources.remove(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted()) {
            targets.remove(target)
        }
    }
}