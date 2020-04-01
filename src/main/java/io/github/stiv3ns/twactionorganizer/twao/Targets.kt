package io.github.stiv3ns.twactionorganizer.twao

import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village
import kotlin.math.roundToInt

class Targets(val targetsList: MutableList<TargetVillage>)
    : List<TargetVillage> by targetsList
{
    val villageCount: Int
        get() = targetsList.size
    val totalAttackCount: Int
        get() = targetsList.map{ it.numberOfAttacks }.sum()

    fun averagedCoordsAsVillage()
        = object : Village {
            override val x: Int = targetsList.map{ it.x }.average().roundToInt()
            override val y: Int = targetsList.map{ it.y }.average().roundToInt()
            override var id: Int? = null
            override fun toString(): String = "$x|$y"
        }
}