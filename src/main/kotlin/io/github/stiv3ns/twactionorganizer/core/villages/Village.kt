package io.github.stiv3ns.twactionorganizer.core.villages

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import kotlinx.serialization.Serializable

@Serializable
open class Village(
    open val x: Int,
    open val y: Int
) {
    var id: Int? = null
        private set

    @Throws(VillageNotFoundException::class)
    fun initID(world: World) {
        id = world.fetchVillageID(this)
    }

    infix fun distanceTo(v2: Village): Int {
        fun Int.squared(): Int = this * this

        return (this.x - v2.x).squared() + (this.y - v2.y).squared()
    }

    override fun toString() = "$x|$y"
}