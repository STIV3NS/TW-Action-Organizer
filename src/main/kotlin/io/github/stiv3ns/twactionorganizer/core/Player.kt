package io.github.stiv3ns.twactionorganizer.core

import kotlinx.serialization.Serializable

@Serializable
data class Player(val nickname: String) {

    var numberOfNobles: Int = 0
        internal set

    var numberOfVillages: Int = 0
        private set

    private val _offAssignments = mutableListOf<Assignment>()
    private val _fakeAssignments = mutableListOf<Assignment>()
    private val _nobleAssignments = mutableListOf<Assignment>()
    private val _fakeNobleAssignments = mutableListOf<Assignment>()
    private val _demolitionAssignments = mutableListOf<Assignment>()

    val offAssignments get() = _offAssignments.toList()
    val fakeAssignments get() = _fakeAssignments.toList()
    val nobleAssignments get() = _nobleAssignments.toList()
    val fakeNobleAssignments get() = _fakeNobleAssignments.toList()
    val demolitionAssignments get() = _demolitionAssignments.toList()

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

    fun putOffAssignment(assignment: Assignment) {
        _offAssignments.add(assignment)
    }

    @Synchronized fun putFakeAssignment(assignment: Assignment) {
        _fakeAssignments.add(assignment)
    }

    fun putNobleAssignment(assignment: Assignment) {
        _nobleAssignments.add(assignment)
    }

    fun putFakeNobleAssignment(assignment: Assignment) {
        _fakeNobleAssignments.add(assignment)
    }

    fun putDemolitionAssignment(assignment: Assignment) {
        _demolitionAssignments.add(assignment)
    }
}