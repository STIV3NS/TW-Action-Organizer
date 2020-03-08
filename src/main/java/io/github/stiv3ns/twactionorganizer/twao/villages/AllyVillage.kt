package io.github.stiv3ns.twactionorganizer.twao.villages

import io.github.stiv3ns.twactionorganizer.twao.Player
import kotlinx.serialization.Serializable

data class AllyVillage(
        override val x: Int,
        override val y: Int,
        val owner: Player
) : Village {
    override var id: Int? = null
    override fun toString() = "$x|$y"
}