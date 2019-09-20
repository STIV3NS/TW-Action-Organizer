package io.github.stiv3ns.twactionorganizer.twao

data class Player(val nickname: String, private var _numberOfNobles: Int = 0) {
    private val offAssignments = mutableListOf<VillageAssignment>()
    private val fakeAssignments = mutableListOf<VillageAssignment>()
    private val nobleAssignments = mutableListOf<VillageAssignment>()
    private val fakeNobleAssignments = mutableListOf<VillageAssignment>()

    val offAssignmentsCopy
        get() = offAssignments.toList()

    val fakeAssignmentsCopy
        get() = fakeAssignments.toList()

    val nobleAssignmentsCopy
        get() = nobleAssignments.toList()

    val fakeNobleAssignmentsCopy
        get() = fakeNobleAssignments.toList()

    val numberOfNobles
        get() = _numberOfNobles

    var numberOfVillages: Int = 0
        private set

    fun hasNoble(): Boolean
        = numberOfNobles > 0

    fun delegateNoble()
        { _numberOfNobles-- }

    fun increaseNumberOfVillages()
        { numberOfVillages++ }

    fun decreaseNumberOfVillaes()
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