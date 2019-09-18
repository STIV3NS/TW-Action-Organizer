package twao.villages

import twao.World
import twao.exceptions.VillageNotFoundException
import kotlin.math.pow

open class Village(open val x: Int, open val y: Int) {
    var id: Int? = null
        private set
    var relativeDistance: Int? = null
        private set

    @Throws(VillageNotFoundException::class)
    fun initID(world: World) {
        id = world.fetchVillageID(this)
    }

    fun setRelativeDistance(vil: Village) {
        relativeDistance = distance(this, vil)
    }

    override fun toString()
        = "$x|$y"

    companion object {
        fun Int.squared(): Int = this * this

        @JvmStatic fun distance(v1: Village, v2: Village): Int
            = (v1.x - v2.x).squared() + (v1.y - v2.y).squared()
    }
}