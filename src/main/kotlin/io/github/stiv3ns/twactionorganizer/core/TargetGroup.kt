package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlin.math.roundToInt

class TargetGroup(
    val name: String,
    val type: AssignerType,
    val villageList: List<TargetVillage>
) {
    val villageCount: Int get() = villageList.size

    val totalAttackCount: Int get() = villageList.map { it.numberOfAttacks }.sum()

    val averagedCoordsAsVillage get() = Village(
        x = villageList.map { it.x }.average().roundToInt(),
        y = villageList.map { it.y }.average().roundToInt()
    )
}