package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.localization.PMFormatterLocalization
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.sqrt

class PMFormatter(
    private val world: World,
    private val dateOfArrival: LocalDateTime,
    private val localization: PMFormatterLocalization
) : PMFormatterLocalization by localization
{
    var batchSize = 5

    private enum class TroopType {
        RAM,
        NOBLE,
        CATAPULT
    }

    private fun troopTravelTime(type: TroopType) = when (type) {
        TroopType.RAM -> 30
        TroopType.NOBLE -> 35
        TroopType.CATAPULT -> 30
    } / world.speed

    fun getFormattedMsgFor(player: Player): String {
        val msg = StringBuilder()

        if (player.nobleAssignments.isNotEmpty() || player.fakeNobleAssignments.isNotEmpty()) {
            msg.append(REQUIREMENTS_HEADER)
            appendNobleRequirements(msg, player)
        }

        if (player.nobleAssignments.isNotEmpty()) {
            msg.append(NOBLE_HEADER)
            appendAssignments(msg, player.nobleAssignments, TroopType.NOBLE)
        }

        if (player.offAssignments.isNotEmpty()) {
            msg.append(OFF_HEADER)
            appendAssignments(msg, player.offAssignments, TroopType.RAM)
        }

        if (player.fakeNobleAssignments.isNotEmpty()) {
            msg.append(FAKENOBLE_HEADER)
            appendAssignments(msg, player.fakeNobleAssignments, TroopType.NOBLE)
        }

        if (player.fakeAssignments.isNotEmpty()) {
            msg.append(FAKE_HEADER)
            appendAssignments(msg, player.fakeAssignments, TroopType.RAM)
        }

        if (player.demolitionAssignments.isNotEmpty()) {
            msg.append(DEMOLITION_HEADER)
            appendAssignments(msg, player.demolitionAssignments, TroopType.CATAPULT)
        }

        return msg.toString()
    }

    private fun appendNobleRequirements(msg: StringBuilder, player: Player) {
        val allNobleAssignments =
            player.nobleAssignments + player.fakeNobleAssignments

        val requirementsByVillage =
            allNobleAssignments.groupBy { it.departure }.mapValues { it.value.size }

        val sortedRequirements =
            requirementsByVillage.toList().sortedByDescending { (_, cnt) -> cnt }.toMap()

        msg.append(OPEN_SPOILER)

        var iteratorCounter = 0
        for ((village, numberOfRequiredNobles) in sortedRequirements) {
            msg.append("$numberOfRequiredNobles __ $village\n")

            if (++iteratorCounter % batchSize == 0)
                msg.append("\n")
        }

        msg.append(CLOSE_SPOILER)
    }

    private fun appendAssignments(msg: StringBuilder, assignments: List<Assignment>, slowestTroop: TroopType) {
        msg.append(OPEN_SPOILER)

        val sortedAssignments = assignments.sortedByDescending { it.squaredDistance }
        var counter = 0
        var previousDayOfMonth = -1

        for (assignment in sortedAssignments) {
            val departureTime = designateDepartureTime(
                assignment.squaredDistance,
                slowestTroop,
                assignment.delayInMinutes
            )

            val verticalSpaceIfNextDay =
                if (previousDayOfMonth !in listOf(departureTime.dayOfMonth, -1))
                    "\n\n\n\n\n!"
                else ""

            val verticalSpaceIfNextGroup = if (counter % batchSize == 0) "\n" else ""

            msg.append("$verticalSpaceIfNextDay ${++counter}. ${formatDepartureTime(departureTime)} $assignment \n")
                .append("[url=${world.domain}/game.php")
                .append("?village=${assignment.departure.id ?: 0}")
                .append("&screen=place&target=${assignment.destination.id ?: 0}]")
                .append("$EXECUTION_TEXT[/url]\n")
                .append(verticalSpaceIfNextGroup)

            previousDayOfMonth = departureTime.dayOfMonth
        }

        msg.append(CLOSE_SPOILER)
    }

    private fun designateDepartureTime(squaredDistance: Int, slowestTroop: TroopType, delayInMinutes: Long): LocalDateTime {
        val actualDistance = sqrt(squaredDistance.toDouble())
        val travelTime = actualDistance * troopTravelTime(slowestTroop)

        return dateOfArrival.minusMinutes(travelTime.toLong()).plusMinutes(delayInMinutes)
    }

    private fun formatDepartureTime(departureTime: LocalDateTime): String =
        departureTime.format(DateTimeFormatter.ofPattern("dd/MM/YY (E) | HH:mm:ss"))
}