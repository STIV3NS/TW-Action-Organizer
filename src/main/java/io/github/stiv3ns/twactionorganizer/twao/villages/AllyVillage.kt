package io.github.stiv3ns.twactionorganizer.twao.villages

import io.github.stiv3ns.twactionorganizer.twao.Player

data class AllyVillage(
        override val x: Int,
        override val y: Int,
        val owner: Player
) : Village(x, y)