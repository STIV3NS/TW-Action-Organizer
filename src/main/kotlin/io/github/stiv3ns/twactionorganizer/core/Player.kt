package io.github.stiv3ns.twactionorganizer.core

import kotlinx.serialization.Serializable

@Serializable
data class Player(val nickname: String) {
    var numberOfNobles: Int = 0
        internal set
    var numberOfVillages: Int = 0
        private set

    private val _offAssignments = mutableListOf<VillageAssignment>()
    private val _fakeAssignments = mutableListOf<VillageAssignment>()
    private val _nobleAssignments = mutableListOf<VillageAssignment>()
    private val _fakeNobleAssignments = mutableListOf<VillageAssignment>()

    val offAssignments get() = _offAssignments.toList()
    val fakeAssignments get() = _fakeAssignments.toList()
    val nobleAssignments get() = _nobleAssignments.toList()
    val fakeNobleAssignments get() = _fakeNobleAssignments.toList()

    constructor(nickname: String, numberOfNobles: Int = 0) : this(nickname) {
        this.numberOfNobles = numberOfNobles
    }

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
        _offAssignments.add(assignment)
    }

    @Synchronized
    fun putFakeAssignment(assignment: VillageAssignment) {
        _fakeAssignments.add(assignment)
    }

    fun putNobleAssignment(assignment: VillageAssignment) {
        _nobleAssignments.add(assignment)
    }

    fun putFakeNobleAssignment(assignment: VillageAssignment) {
        _fakeNobleAssignments.add(assignment)
    }
}