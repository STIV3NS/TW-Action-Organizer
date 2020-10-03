package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.localization.interfaces.PMFormatterLocalization
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.sqrt

class PMFormatter(
    private val world: World,
    private val dateOfArrival: LocalDateTime,
    private val localization: PMFormatterLocalization,
    var batchSize: Int = 5
) : PMFormatterLocalization by localization
{
    private enum class TroopType {
        RAM,
        NOBLE,
        CATAPULT
    }

    private fun troopTravelTime(type: TroopType) =
        when (type) {
            TroopType.RAM -> 30
            TroopType.NOBLE -> 35
            TroopType.CATAPULT -> 30
        } / world.speed

    fun format(playerAssignments: PlayerAssignments): String {
        val msg = StringBuilder()

        if (playerAssignments.nobleAssignments.isNotEmpty() || playerAssignments.fakeNobleAssignments.isNotEmpty()) {
            appendNobleRequirements(msg, playerAssignments)
        }


        class Description(val slowestTroop: TroopType, val header: String)

        mapOf(
            playerAssignments.offAssignments to Description(TroopType.RAM, OFF_HEADER),
            playerAssignments.fakeAssignments to Description(TroopType.RAM, FAKE_HEADER),
            playerAssignments.nobleAssignments to Description(TroopType.NOBLE, NOBLE_HEADER),
            playerAssignments.fakeNobleAssignments to Description(TroopType.NOBLE, FAKENOBLE_HEADER),
            playerAssignments.demolitionAssignments to Description(TroopType.CATAPULT, DEMOLITION_HEADER)
        )
        .forEach { (assignmentClass, description) ->
            if (assignmentClass.isNotEmpty()) {
                appendAssignmentClass(msg, assignmentClass, description.header, description.slowestTroop)
            }
        }

        return msg.toString()
    }

    private fun appendNobleRequirements(msg: StringBuilder, playerAssignments: PlayerAssignments) {
        val allNobleAssignments =
            playerAssignments.nobleAssignments + playerAssignments.fakeNobleAssignments

        val requirementsByVillage =
            allNobleAssignments.groupBy { it.departure }.mapValues { it.value.size }

        val sortedRequirements =
            requirementsByVillage.toList().sortedByDescending { (_, cnt) -> cnt }.toMap()

        msg.append(REQUIREMENTS_HEADER, "\n", OPEN_SPOILER, "\n")

        var iteratorCounter = 0
        for ((village, numberOfRequiredNobles) in sortedRequirements) {
            msg.append("$numberOfRequiredNobles __ $village\n")

            if (++iteratorCounter % batchSize == 0)
                msg.append("\n")
        }

        msg.append(CLOSE_SPOILER, "\n\n\n")
    }

    private fun appendAssignmentClass(msg: StringBuilder, assignments: List<Assignment>, header: String, slowestTroop: TroopType) {
        msg.append(header, "\n", OPEN_SPOILER, "\n")

        val sortedAssignments = assignments.sortedByDescending { it.squaredDistance }
        var previousDayOfMonth = -1

        for ((counter, assignment) in sortedAssignments.withIndex()) {
            val departureTime = designateDepartureTime(
                assignment.squaredDistance,
                slowestTroop,
                assignment.delayInMinutes
            )

            val verticalSpaceIfNextDay =
                if (previousDayOfMonth !in listOf(departureTime.dayOfMonth, -1))
                    "\n\n\n\n\n!"
                else ""

            val verticalSpaceIfNextBatch = if (counter % batchSize == 0) "\n" else ""

            msg.append("$verticalSpaceIfNextDay ${counter + 1}. ${formatDepartureTime(departureTime)} $assignment \n")
                .append("[url=${world.domain}/game.php")
                .append("?village=${assignment.departure.id}")
                .append("&screen=place&target=${assignment.destination.id}]")
                .append("$EXECUTION_TEXT[/url]\n")
                .append(verticalSpaceIfNextBatch)

            previousDayOfMonth = departureTime.dayOfMonth
        }

        msg.append(CLOSE_SPOILER, "\n\n\n")
    }

    private fun designateDepartureTime(squaredDistance: Int, slowestTroop: TroopType, delayInMinutes: Long): LocalDateTime {
        val actualDistance = sqrt(squaredDistance.toDouble())
        val travelTime = actualDistance * troopTravelTime(slowestTroop)

        return dateOfArrival.minusMinutes(travelTime.toLong()).plusMinutes(delayInMinutes)
    }

    private fun formatDepartureTime(departureTime: LocalDateTime): String =
        departureTime.format(DateTimeFormatter.ofPattern("dd/MM/YY (E) | HH:mm:ss"))
}
