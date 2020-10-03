package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import kotlinx.serialization.Serializable

@Serializable
class PlayerAssignments private constructor(
    val nickname: String,
    private val assignments: Map<AssignerType, List<Assignment>>
) {
    private fun List<AssignerType>.assignmentsFlatMap(): List<Assignment> =
        mapNotNull { type -> assignments[type] }
            .flatten()

    val offAssignments: List<Assignment> get() =
        listOf(
            AssignerType.RAM,
            AssignerType.RANDOMIZED_RAM,
            AssignerType.REVERSED_RAM
        ).assignmentsFlatMap()

    val fakeAssignments: List<Assignment> get() =
        listOf(
            AssignerType.FAKE_RAM,
            AssignerType.RANDOMIZED_FAKE_RAM
        ).assignmentsFlatMap()

    val nobleAssignments get() =
        listOf(
            AssignerType.NOBLE
        ).assignmentsFlatMap()

    val fakeNobleAssignments get() =
        listOf(
            AssignerType.FAKE_NOBLE
        ).assignmentsFlatMap()

    val demolitionAssignments get() =
        listOf(
            AssignerType.DEMOLITION,
            AssignerType.RANDOMIZED_DEMOLITION
        ).assignmentsFlatMap()

    companion object {
        fun fromAssignments(assignments: Collection<Assignment>): Collection<PlayerAssignments> =
            assignments
                .groupBy { it.departure.ownerNickname }
                .map { (nickname, allAssignments) ->
                    PlayerAssignments(nickname,
                                      allAssignments.groupBy { it.type })
                }
    }
}