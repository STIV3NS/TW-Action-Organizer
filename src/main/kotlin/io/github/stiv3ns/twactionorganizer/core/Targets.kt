package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlin.math.roundToInt

class Targets(val targetsList: MutableList<TargetVillage>)

    : MutableList<TargetVillage> by targetsList
{
    val villageCount: Int
        get() = targetsList.size
    val totalAttackCount: Int
        get() = targetsList.map{ it.numberOfAttacks }.sum()

    fun averagedCoordsAsVillage()
        = Village (
            x = targetsList.map{ it.x }.average().roundToInt(),
            y = targetsList.map{ it.y }.average().roundToInt()
        )
}