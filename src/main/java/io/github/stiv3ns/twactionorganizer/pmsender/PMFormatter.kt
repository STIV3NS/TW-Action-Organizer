package io.github.stiv3ns.twactionorganizer.pmsender

import io.github.stiv3ns.twactionorganizer.twao.Player
import io.github.stiv3ns.twactionorganizer.twao.VillageAssignment
import io.github.stiv3ns.twactionorganizer.twao.World
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.sqrt

class PMFormatter(private val world: World, private val dateOfArrival: LocalDateTime) {

    private val OPEN_SPOILER = "[spoiler]\n"
    private val CLOSE_SPOILER = "[/spoiler]\n\n\n"

    private val bundle = ResourceBundle.getBundle("localization/pmformatter")

    private val REQUIREMENTS_HEADER by lazy { bundle.getString("REQUIREMENTS_HEADER") }
    private val NOBLE_HEADER        by lazy { bundle.getString("NOBLE_HEADER") }
    private val OFF_HEADER          by lazy { bundle.getString("OFF_HEADER") }
    private val FAKE_HEADER         by lazy { bundle.getString("FAKE_HEADER") }
    private val FAKENOBLE_HEADER    by lazy { bundle.getString("FAKENOBLE_HEADER") }
    private val EXECUTION_TEXT      by lazy { bundle.getString("EXECUTION_TEXT") }

    private val GROUP_SIZE = 5

    private enum class TroopType {
        RAM,
        NOBLE
    }

    fun getFormattedMsgFor(player: Player): String {
        val msg = StringBuilder()

        if (player.nobleAssignmentsCopy.isNotEmpty() || player.fakeNobleAssignmentsCopy.isNotEmpty()) {
            msg.append(REQUIREMENTS_HEADER)
            appendNobleRequirements(msg, player)
        }

        if (player.nobleAssignmentsCopy.isNotEmpty()) {
            msg.append(NOBLE_HEADER)
            appendAssignments(msg, player.nobleAssignmentsCopy, TroopType.NOBLE)
        }

        if (player.offAssignmentsCopy.isNotEmpty()) {
            msg.append(OFF_HEADER)
            appendAssignments(msg, player.offAssignmentsCopy, TroopType.RAM)
        }

        if (player.fakeNobleAssignmentsCopy.isNotEmpty()) {
            msg.append(FAKENOBLE_HEADER)
            appendAssignments(msg, player.fakeNobleAssignmentsCopy, TroopType.NOBLE)
        }

        if (player.fakeAssignmentsCopy.isNotEmpty()) {
            msg.append(FAKE_HEADER)
            appendAssignments(msg, player.fakeAssignmentsCopy, TroopType.RAM)
        }

        return msg.toString()
    }

    private fun appendNobleRequirements(msg: StringBuilder, player: Player) {
        val allNobleAssignments = player.nobleAssignmentsCopy + player.fakeNobleAssignmentsCopy

        val requirementsByVillage = numberOfNobleRequirementsByVillage(allNobleAssignments)
        val sortedRequirements = sortedRequirementsByVillage(requirementsByVillage)

        msg.append(OPEN_SPOILER)

        var iteratorCounter = 0
        for ((village, numberOfRequiredNobles) in sortedRequirements) {
            msg.append("$numberOfRequiredNobles __ $village\n")

            if (++iteratorCounter % GROUP_SIZE == 0) {
                msg.append("\n")
            }
        }

        msg.append(CLOSE_SPOILER)
    }

    private fun appendAssignments(msg: StringBuilder, assignments: List<VillageAssignment>, slowestTroop: TroopType) {
        msg.append(OPEN_SPOILER)

        val sortedAssignments = sortedAssignments(assignments)
        var counter = 0
        var previousDayOfMonth = -1
        for (assignment in sortedAssignments) {
            val departureTime = designateDepartureTime(assignment.squaredDistance, slowestTroop)

            val verticalSpaceIfNextDay = if (previousDayOfMonth != departureTime.dayOfMonth) "\n\n\n" else ""
            val verticalSpaceIfNextGroup = if (counter % GROUP_SIZE == 0) "\n" else ""

            msg.append("$verticalSpaceIfNextDay ${++counter}. ${formatDepartureTime(departureTime)} $assignment \n")
               .append("[url=${world.domain}/game.php")
               .append("?village=${assignment.departure.id ?: 0}")
               .append("&screen=place&target=${assignment.destination.id ?: 0}]")
               .append("$EXECUTION_TEXT[/url]\n")
               .append("$verticalSpaceIfNextGroup")

            previousDayOfMonth = departureTime.dayOfMonth
        }

        msg.append(CLOSE_SPOILER)
    }

    private fun numberOfNobleRequirementsByVillage(allNobleAssignments: List<VillageAssignment>): Map<Village, Int> {
        val assignmentsByDeparture = allNobleAssignments.groupBy { it.departure }
        val requirementsByVillage = assignmentsByDeparture.mapValues { it.value.size }

        return requirementsByVillage
    }

    private fun sortedRequirementsByVillage(requirementsByVillage: Map<Village, Int>): SortedMap<Village, Int>
        = requirementsByVillage.toSortedMap( compareBy { requirementsByVillage[it] } ) /* sort by values */

    private fun designateDepartureTime(squaredDistance: Int, slowestTroop: TroopType): LocalDateTime {
        val actualDistance = sqrt(squaredDistance.toDouble())
        val travelTime = actualDistance * troopTravelTime(slowestTroop)

        val departureTime = dateOfArrival.minusMinutes(travelTime.toLong())

        return departureTime
    }

    private fun formatDepartureTime(departureTime: LocalDateTime): String
        = departureTime.format(DateTimeFormatter.ofPattern("dd/MM/YY | HH:mm:ss"))

    private fun sortedAssignments(assignments: List<VillageAssignment>)
        = assignments.sortedByDescending { it.squaredDistance }

    private fun troopTravelTime(type: TroopType) = when (type) {
        TroopType.RAM -> 30
        TroopType.NOBLE -> 35
    } / world.speed
}