package io.github.stiv3ns.twactionorganizer.twao.villages

import kotlinx.serialization.Serializable

data class TargetVillage(
        override val x: Int,
        override val y: Int
) : Village {
    override var id: Int? = null

    var numberOfAttacks: Int = 0
        private set

    constructor(x: Int, y: Int, numberOfAttacks: Int) : this(x, y) {
        this.numberOfAttacks = numberOfAttacks
    }

    fun attack() {
        numberOfAttacks--;
    }

    fun isAssignCompleted(): Boolean = (numberOfAttacks <= 0)

    override fun toString() = "$x|$y"
}