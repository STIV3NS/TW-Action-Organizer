package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import io.github.stiv3ns.twactionorganizer.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch

@ObsoleteCoroutinesApi
fun <T : Village> CoroutineScope.idInitializer(villages: Collection<T>, world: World) = launch {
    villages.forEach { village ->
        try {
            village.initId(world)
        }
        catch (e: VillageNotFoundException) {
            Logger.warn(e.message ?: "Failed to initialize $village's id")
        }
    }
}