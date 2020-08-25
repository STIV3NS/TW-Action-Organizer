package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

data class Resources(
    val players: Collection<Player>,
    val villages: Collection<AllyVillage>
) {
    val villageCount get() = villages.size
    val playerCount get() = players.size
    val nobleCount get() = players.map { it.numberOfNobles }.sum()
}