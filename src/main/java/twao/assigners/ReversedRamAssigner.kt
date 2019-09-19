package twao.assigners

import twao.Player
import twao.villages.AllyVillage
import twao.villages.TargetVillage
import twao.villages.Village
import java.util.*
import kotlin.Comparator

class ReversedRamAssigner internal constructor(
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
        putTargetsToQueue(referencePoint = mainReferencePoint)

        for (targetPair in targetsQueue) {
            if (resources.isEmpty())
                break;

            handleTarget(targetPair.first)
        }

    }

    private fun handleTarget(target: TargetVillage) {
        while (!target.isAssignCompleted()) {
            if (resources.isEmpty())
                break;

            putResourcesToQueue(referencePoint = target)
            val (nearestAllyVillage, distance) = resourcesQueue.poll()

            assign(nearestAllyVillage, target, distance)

            resources.remove(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted()) targets.remove(target)
    }

    private fun setTargetsPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)

    private fun setResourcesPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)
}