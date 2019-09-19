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

    private var resourcesWithAvailableNobles = resources.filter { it.owner.hasNoble() }

    override fun putResourcesToQueue(referencePoint: Village) {
        resourcesWithAvailableNobles.forEach { resourcesQueue.offer( Pair(it, Village.distance(it, referencePoint)) ) }
    }

    override fun run() {
        putTargetsToQueue(referencePoint = mainReferencePoint)

        for (targetPair in targetsQueue) {
            if (resourcesWithAvailableNobles.isEmpty()) break

            handleTarget(targetPair.first)
        }

    }

    private fun handleTarget(target: TargetVillage) {
        while (!target.isAssignCompleted()) {
            if (resourcesWithAvailableNobles.isEmpty())
                break

            putResourcesToQueue(referencePoint = target)

            if (targetsQueue.poll().second > maxNobleRange)
                break

            val (nearestAllyVillage, distance) = resourcesQueue.poll()
            assign(nearestAllyVillage, target, distance)

            target.attack()

            nearestAllyVillage.owner.delegateNoble()
            updateResourcesWithAvailableNobles(villageToRemove = nearestAllyVillage)
        }

        if (target.isAssignCompleted()) {
            targets.remove(target)
        }
    }

    private fun updateResourcesWithAvailableNobles(villageToRemove: AllyVillage) {
        resourcesWithAvailableNobles = resourcesWithAvailableNobles
                                        .filter { it.owner.hasNoble() && it != villageToRemove }
    }

    private fun setTargetsPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)

    private fun setResourcesPriorities()
            = Comparator.comparing(Pair<Village, Int>::second)
}