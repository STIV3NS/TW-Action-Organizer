package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.sqrt

class PMFormatter(private val world: World, private val dateOfArrival: LocalDateTime) {

    private val bundle = ResourceBundle.getBundle("localization/pmformatter")

    private val OPEN_SPOILER        by lazy { bundle.getString("OPEN_SPOILER") }
    private val CLOSE_SPOILER       by lazy { bundle.getString("CLOSE_SPOILER") }
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

    private fun troopTravelTime(type: TroopType) = when (type) {
        TroopType.RAM -> 30
        TroopType.NOBLE -> 35
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

            if (++iteratorCounter % GROUP_SIZE == 0)
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

            val verticalSpaceIfNextGroup = if (counter % GROUP_SIZE == 0) "\n" else ""

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
        departureTime.format(DateTimeFormatter.ofPattern("dd/MM/YY | HH:mm:ss"))
}