package io.github.stiv3ns.twactionorganizer.twao.assigners

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.util.*
import kotlin.Comparator

class StandardRamAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val targetsQueue = PriorityQueue< Pair<TargetVillage, Int> >(targets.size, getInternalTargetsComparator())
    override val resourcesQueue = PriorityQueue< Pair<AllyVillage, Int> >(resources.size, getInternalResourcesComparator())

    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment


    override fun run() {
        putResourcesToQueue(referencePoint = mainReferencePoint)

        for ((allyVillage, _) in resourcesQueue) {
            if (targets.isEmpty()) {
                break
            }

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

    private fun getInternalTargetsComparator()
        = Comparator.comparing(Pair<Village, Int>::second)

    private fun getInternalResourcesComparator()
        = Comparator.comparing(Pair<Village, Int>::second)
                    .reversed()
}