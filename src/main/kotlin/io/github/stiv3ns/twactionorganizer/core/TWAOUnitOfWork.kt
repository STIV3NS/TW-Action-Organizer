package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

class TWAOUnitOfWork {
    private var world: World? = null

    private var concreteResources: Resources? = null
    private var fakeResources: Resources? = null

    private val targetGroups = mutableMapOf<AssignerType, MutableList<TargetGroup>>()

    fun setWorld(world: World) {
        this.world = world
    }

    fun getWorld(): World =
        world ?: throw MissingConfigurationException("TWAOUnitOfWork: world not set")

    fun setConcreteResources(resources: Resources) {
        concreteResources = resources
    }

    fun setFakeResources(resources: Resources) {
        fakeResources = resources
    }

    fun getConcreteResourceVillages(): MutableList<AllyVillage> = when (concreteResources) {
        null -> mutableListOf()
        else -> concreteResources!!.villages
    }

    fun getFakeResourceVillages(): MutableList<AllyVillage> = when (fakeResources) {
        null -> mutableListOf()
        else -> fakeResources!!.villages
    }

    fun getAllPlayers(): List<Player> {
        val players = mutableListOf<Player>()

        concreteResources?.players?.let { players.addAll(it) }
        fakeResources?.players?.let { players.addAll(it) }

        return players
    }

    fun dropPlayer(player: Player) {
        concreteResources?.players?.remove(player)
        fakeResources?.players?.remove(player)

        concreteResources?.villages?.removeAll { it.owner == player }
        fakeResources?.villages?.removeAll { it.owner == player }
    }

    fun dropVillage(vil: AllyVillage) {
        val wasConcrete: Boolean? = concreteResources?.villages?.remove(vil)

        if (wasConcrete == true) vil.owner.unregisterVillage()
        else fakeResources?.villages?.remove(vil)
    }

    fun setPlayerNumberOfNobles(player: Player, newValue: Int) {
        player.numberOfNobles = newValue
    }

    fun addTargetGroup(group: TargetGroup) {
        targetGroups.getOrPut(
            key = group.type,
            defaultValue = { mutableListOf() }
        ).add(group)
    }

    fun dropTargetGroup(group: TargetGroup) {
        targetGroups[group.type]?.remove(group)
    }

    fun setDelayInMinutes(group: TargetGroup, delay: Long) {
        group.setDelayInMinutes(delay)
    }

    fun getTargetGroups(type: AssignerType): List<TargetGroup> = targetGroups.getOrDefault(
        key = type,
        defaultValue = mutableListOf()
    ).toList()
}