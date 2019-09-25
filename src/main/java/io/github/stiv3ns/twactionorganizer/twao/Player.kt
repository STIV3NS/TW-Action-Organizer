package io.github.stiv3ns.twactionorganizer.twao

import kotlinx.serialization.Serializable

@Serializable
data class Player(val nickname: String, private var _numberOfNobles: Int = 0)  {

    var numberOfVillages: Int = 0
        private set

    val numberOfNobles: Int
        get() = _numberOfNobles

    val offAssignmentsCopy:       List<VillageAssignment>
        get() = offAssignments.toList()
    val fakeAssignmentsCopy:      List<VillageAssignment>
        get() = fakeAssignments.toList()
    val nobleAssignmentsCopy:     List<VillageAssignment>
        get() = nobleAssignments.toList()
    val fakeNobleAssignmentsCopy: List<VillageAssignment>
        get() = fakeNobleAssignments.toList()

    private val offAssignments       = mutableListOf<VillageAssignment>()
    private val fakeAssignments      = mutableListOf<VillageAssignment>()
    private val nobleAssignments     = mutableListOf<VillageAssignment>()
    private val fakeNobleAssignments = mutableListOf<VillageAssignment>()


    fun hasNoble(): Boolean
        = _numberOfNobles > 0

    fun delegateNoble()
        { _numberOfNobles-- }


    fun registerVillage()
        { numberOfVillages++ }

    fun unregisterVillage()
        { numberOfVillages-- }


    fun putOffAssignment(assignment: VillageAssignment)
        { offAssignments.add(assignment) }

    @Synchronized
    fun putFakeAssignment(assignment: VillageAssignment)
        { fakeAssignments.add(assignment) }

    fun putNobleAssignment(assignment: VillageAssignment)
        { nobleAssignments.add(assignment) }

    fun putFakeNobleAssignment(assignment: VillageAssignment)
        { fakeNobleAssignments.add(assignment) }
}