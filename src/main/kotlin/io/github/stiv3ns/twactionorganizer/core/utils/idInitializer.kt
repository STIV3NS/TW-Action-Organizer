package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun <T : Village> CoroutineScope.idInitializer(villages: MutableCollection<T>, world: World) = launch {
    val invalidVillages = mutableListOf<T>()

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