package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

class Resources(
    val players: MutableList<Player>,
    val villages: MutableList<AllyVillage>
) {
    val villageCount get() = villages.size
    val playerCount get() = players.size
}