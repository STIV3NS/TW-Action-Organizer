package twao.assigners

import twao.Player
import twao.villages.AllyVillage
import twao.villages.TargetVillage
import twao.villages.Village
import java.util.*
import kotlin.Comparator

class NobleAssigner internal constructor(
        targets: MutableList<TargetVillage>,
        resources: MutableList<AllyVillage>,
        mainReferencePoint: Village,
        isAssigningFakes: Boolean,
        private val maxNobleRange: Int
) : Assigner (targets, resources, mainReferencePoint, isAssigningFakes) {

    override val targetsQueue = PriorityQueue< Pair<TargetVillage, Int> >(targets.size, setTargetsPriorities())
    override val resourcesQueue = PriorityQueue< Pair<AllyVillage, Int> >(resources.size, setResourcesPriorities())

    override val offAction = Player::putNobleAssignment
    override val fakeAction = Player::putFakeNobleAssignment

    private var resWithNobles = resources.filter { it.owner.hasNoble() }
                                         .toMutableList()

    override fun putResourcesToQueue(referencePoint: Village) {
        resWithNobles.forEach { resourcesQueue.offer( Pair(it, Village.distance(it, referencePoint)) ) }
    }

    override fun run() {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        for ((target, _) in targetsQueue) {
            if (resWithNobles.isEmpty()) break

            handleTarget(target)
        }

    }

    private fun handleTarget(target: TargetVillage) {
        putResourcesToQueue(referencePoint = target)

        for ((nearestAllyVillage, distance) in resourcesQueue) {
            if (target.isAssignCompleted())
                break
            if (distance > maxNobleRange)
                break

            assign(nearestAllyVillage, target, distance)
            resources.remove(nearestAllyVillage)

            nearestAllyVillage.owner.delegateNoble()
            if (!nearestAllyVillage.owner.hasNoble()) updateResWithNobles()
            else resWithNobles.remove(nearestAllyVillage)

            target.attack()
        }

        if (target.isAssignCompleted()) {
            targets.remove(target)
        }
    }

    private fun updateResWithNobles() {
        resWithNobles = resWithNobles.filter { it.owner.hasNoble() }
                                     .toMutableList()
    }

    private fun setTargetsPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)

    private fun setResourcesPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)
}