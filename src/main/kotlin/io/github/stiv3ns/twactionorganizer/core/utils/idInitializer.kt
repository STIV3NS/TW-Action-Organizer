package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.idInitializer(villages: MutableCollection<AllyVillage>, world: World) = launch {
    val invalidVillages = mutableListOf<AllyVillage>()

    villages.forEach { village ->
        try {
            village.initId(world)
        }
        catch (e: VillageNotFoundException) {
            invalidVillages.add(village)
        }
    }

    villages.removeAll(invalidVillages)
}