package twao.villages

import twao.Player

data class AllyVillage(
        override val x: Int,
        override val y: Int,
        val owner: Player
) : Village(x, y)