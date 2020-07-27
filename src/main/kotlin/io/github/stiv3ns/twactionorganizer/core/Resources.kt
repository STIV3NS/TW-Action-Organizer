package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

class Resources (
        val players: List<Player>,
        val villages: List<AllyVillage>
) {
    val villageCount
        get() = villages.size
    val playerCount
        get() = players.size
}