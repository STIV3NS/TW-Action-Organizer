package io.github.stiv3ns.twactionorganizer.twao.villages

import kotlinx.serialization.Serializable

data class TargetVillage(
        override val x: Int,
        override val y: Int,
        private var _numberOfAttacks: Int
) : Village {
    override var id: Int? = null

    val numberOfAttacks: Int
        get() = _numberOfAttacks

    fun attack()
        { _numberOfAttacks-- }

    fun isAssignCompleted(): Boolean
            = _numberOfAttacks <= 0

    override fun toString() = "$x|$y"
}