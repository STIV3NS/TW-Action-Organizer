package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import kotlinx.serialization.Serializable

@Serializable
class PlayerAssignments private constructor(
    val nickname: String,
    val assignments: Map<AssignerType, List<Assignment>>
) {
    val offAssignments: List<Assignment> =
        getAssignmentsOfType(
            AssignerType.RAM,
            AssignerType.RANDOMIZED_RAM,
            AssignerType.REVERSED_RAM
        )

    val fakeAssignments: List<Assignment> =
        getAssignmentsOfType(
            AssignerType.FAKE_RAM,
            AssignerType.RANDOMIZED_FAKE_RAM
        )

    val nobleAssignments =
        getAssignmentsOfType(
            AssignerType.NOBLE
        )

    val fakeNobleAssignments =
        getAssignmentsOfType(
            AssignerType.FAKE_NOBLE
        )

    val demolitionAssignments =
        getAssignmentsOfType(
            AssignerType.DEMOLITION,
            AssignerType.RANDOMIZED_DEMOLITION
        )

    fun getAssignmentsOfType(vararg type: AssignerType): List<Assignment> =
        type.mapNotNull { t -> assignments[t] }.flatten()

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