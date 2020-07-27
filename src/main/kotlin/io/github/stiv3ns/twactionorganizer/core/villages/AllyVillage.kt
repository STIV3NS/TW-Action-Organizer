package io.github.stiv3ns.twactionorganizer.core.villages

import io.github.stiv3ns.twactionorganizer.core.Player

data class AllyVillage(
        override val x: Int,
        override val y: Int,
        val owner: Player
) : Village {
    override var id: Int? = null
    override fun toString() = "$x|$y"
}