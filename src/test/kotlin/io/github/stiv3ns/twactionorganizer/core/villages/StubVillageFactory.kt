package io.github.stiv3ns.twactionorganizer.core.villages

import kotlin.random.Random

object StubVillageFactory {
    val random = Random(1337)

    fun getVillage(
        x: Int = random.nextInt(100, 900),
        y: Int = random.nextInt(100, 900),
        id: Int = random.nextInt(),
        ownerNickname: String = random.nextBytes(20).toString()
    ): Village =
        Village(x, y, id, ownerNickname)
}