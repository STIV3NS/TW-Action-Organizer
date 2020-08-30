package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

data class Resources(
    val players: Collection<Player>,
    val villages: Collection<AllyVillage>
) {
    val villageCount get() = villages.size
    val playerCount get() = players.size
    val nobleCount get() = players.map { it.numberOfNobles }.sum()

    companion object {
        fun empty(): Resources =
            Resources(players = listOf(), villages = listOf())

        fun fromVillageCollection(villages: Collection<AllyVillage>): Resources =
            Resources(
                villages = villages,
                players =
                    villages
                    .groupBy { it.ownerNickname }
                    .keys
                    .map { nick -> Player(nick) }
            )
    }
}