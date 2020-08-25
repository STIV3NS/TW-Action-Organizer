package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlin.math.roundToInt

class TargetGroup(
    val name: String,
    val type: AssignerType,
    val villages: Collection<TargetVillage>
) {
    val villageCount get() = villages.size
    val totalAttackCount get() = villages.map { it.numberOfAttacks }.sum()

    val averagedCoordsAsVillage get() = Village(
        x = villages.map { it.x }.average().roundToInt(),
        y = villages.map { it.y }.average().roundToInt()
    )

    fun setDelayInMinutes(delay: Long) {
        villages.map { it.delayInMinutes = delay }
    }

    fun withDelayInMinutes(delay: Long) = apply {
        setDelayInMinutes(delay)
    }
}