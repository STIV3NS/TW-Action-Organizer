package io.github.stiv3ns.twactionorganizer.core.utils

import io.github.stiv3ns.twactionorganizer.core.World
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException
import io.github.stiv3ns.twactionorganizer.core.villages.Village
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.logging.Logger

fun <T : Village> CoroutineScope.idInitializer(villages: Collection<T>, world: World) = launch {
    villages.forEach { village ->
        try {
            village.initId(world)
        }
        catch (e: VillageNotFoundException) {
            Logger.getAnonymousLogger().warning(e.toString())
        }
    }
}