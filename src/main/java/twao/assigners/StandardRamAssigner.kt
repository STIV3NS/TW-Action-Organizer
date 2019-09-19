package twao.assigners

import twao.Player
import twao.villages.AllyVillage
import twao.villages.TargetVillage
import twao.villages.Village
import java.util.*
import kotlin.Comparator

class StandardRamAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val targetsQueue = PriorityQueue< Pair<TargetVillage, Int> >(targets.size, setTargetsPriorities())
    override val resourcesQueue = PriorityQueue< Pair<AllyVillage, Int> >(resources.size, setResourcesPriorities())

    override val offAction = Player::putOffAssignment
    override val fakeAction = Player::putFakeAssignment

    override fun run() {
        putResourcesToQueue(referencePoint = mainReferencePoint)

        for (allyVillage in resources) {
            if (targets.isEmpty())
                break

            putTargetsToQueue(referencePoint = allyVillage)

            val (nearestTarget, distance) = targetsQueue.poll()

            assign(allyVillage, nearestTarget, distance)
            nearestTarget.attack()

            resources.remove(allyVillage)
            if (nearestTarget.isAssignCompleted()) targets.remove(nearestTarget)
        }
    }

    private fun setTargetsPriorities()
        = Comparator.comparing(Pair<Village, Int>::second)

    private fun setResourcesPriorities()
        = Comparator.comparing(Pair<Village, Int>::second)
                    .reversed()
}