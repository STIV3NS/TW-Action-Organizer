package io.github.stiv3ns.twactionorganizer.twao.assigners

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.util.*
import kotlin.Comparator

class NobleAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean,
        private val maxNobleRange: Int
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val offAction = Player::putNobleAssignment
    override val fakeAction = Player::putFakeNobleAssignment

    private var resWithNobles = resources.filter { it.owner.hasNoble() }.toMutableList()

    override fun putResourcesToQueue(referencePoint: Village) {
        resourcesQueue.clear()
        resWithNobles.forEach { resourcesQueue.offer( Pair(it, it distanceTo referencePoint) ) }
    }



    override fun run() {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        while (targetsQueue.isNotEmpty() && resWithNobles.isNotEmpty()) {
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
            if (distance > maxNobleRange) {
                break
            }

            assign(nearestAllyVillage, target, distance)
            updateOwnerAndResources(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted()) {
            targets.remove(target)
        }
    }

    private fun updateOwnerAndResources(nearestAllyVillage: AllyVillage) {
        nearestAllyVillage.owner.delegateNoble()

        resources.remove(nearestAllyVillage)

        if (!nearestAllyVillage.owner.hasNoble()) {
            updateResWithNobles()
        }
        else {
            resWithNobles.remove(nearestAllyVillage)
        }
    }

    private fun updateResWithNobles() {
        resWithNobles = resWithNobles.filter { it.owner.hasNoble() }.toMutableList()
    }
}