package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlin.math.roundToInt

class TargetGroup(
        val name: String,
        val villageList: MutableList<TargetVillage>
) : MutableList<TargetVillage> by villageList
{
    val villageCount: Int
        get() = villageList.size
    val totalAttackCount: Int
        get() = villageList.map{ it.numberOfAttacks }.sum()

    fun averagedCoordsAsVillage()
        = Village (
            x = villageList.map{ it.x }.average().roundToInt(),
            y = villageList.map{ it.y }.average().roundToInt()
        )
}