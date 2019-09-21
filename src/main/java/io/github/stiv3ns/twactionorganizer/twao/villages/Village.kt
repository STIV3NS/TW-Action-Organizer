package io.github.stiv3ns.twactionorganizer.twao.villages

import io.github.stiv3ns.twactionorganizer.twao.World
import io.github.stiv3ns.twactionorganizer.twao.exceptions.VillageNotFoundException

open class Village(open val x: Int, open val y: Int) {
    var id: Int? = null
        private set

    @Throws(VillageNotFoundException::class)
    fun initID(world: World) {
        id = world.fetchVillageID(this)
    }

    override fun toString()
        = "$x|$y"


    companion object {
        fun Int.squared(): Int = this * this

        @JvmStatic fun distance(v1: Village, v2: Village): Int
            = (v1.x - v2.x).squared() + (v1.y - v2.y).squared()
    }
}