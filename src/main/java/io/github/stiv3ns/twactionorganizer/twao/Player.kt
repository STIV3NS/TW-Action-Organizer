package io.github.stiv3ns.twactionorganizer.twao

import kotlinx.serialization.Serializable

@Serializable
data class Player(val nickname: String) {
    var numberOfNobles: Int = 0
        private set
    var numberOfVillages: Int = 0
        private set

    constructor(nickname: String, numberOfNobles: Int = 0) : this(nickname) {
        this.numberOfNobles = numberOfNobles
    }

    val offAssignmentsCopy: List<VillageAssignment>
        get() = offAssignments.toList()

    val fakeAssignmentsCopy: List<VillageAssignment>
        get() = fakeAssignments.toList()

    val nobleAssignmentsCopy: List<VillageAssignment>
        get() = nobleAssignments.toList()

    val fakeNobleAssignmentsCopy: List<VillageAssignment>
        get() = fakeNobleAssignments.toList()

    private val offAssignments       = mutableListOf<VillageAssignment>()
    private val fakeAssignments      = mutableListOf<VillageAssignment>()
    private val nobleAssignments     = mutableListOf<VillageAssignment>()
    private val fakeNobleAssignments = mutableListOf<VillageAssignment>()


    fun hasNoble(): Boolean = (numberOfNobles > 0)

    fun delegateNoble() {
        numberOfNobles--
    }


    fun registerVillage() {
        numberOfVillages++
    }

    fun unregisterVillage() {
        numberOfVillages--
    }


    fun putOffAssignment(assignment: VillageAssignment) {
        offAssignments.add(assignment)
    }

    @Synchronized fun putFakeAssignment(assignment: VillageAssignment) {
        fakeAssignments.add(assignment)
    }

    fun putNobleAssignment(assignment: VillageAssignment) {
        nobleAssignments.add(assignment)
    }

    fun putFakeNobleAssignment(assignment: VillageAssignment) {
        fakeNobleAssignments.add(assignment)
    }
}