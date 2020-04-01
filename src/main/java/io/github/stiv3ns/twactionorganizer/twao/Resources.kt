package io.github.stiv3ns.twactionorganizer.twao

import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage

class Resources (
        val players: List<Player>,
        val villages: List<AllyVillage>
) {
    val villageCount
        get() = villages.size
}