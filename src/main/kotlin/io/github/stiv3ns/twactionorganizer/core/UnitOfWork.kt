package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

class UnitOfWork {
    private var world: World? = null

    private var concreteResources: Resources? = null
    private var fakeResources: Resources? = null
    private var demolitionResources: Resources? = null

    private val targetGroups = mutableMapOf<AssignerType, MutableList<TargetGroup>>()

    fun setWorld(world: World) {
        this.world = world
    }

    fun getWorld(): World =
        world ?: throw MissingConfigurationException("UnitOfWork: world not set")

    fun setConcreteResources(resources: Resources) {
        concreteResources = resources
    }

    fun setFakeResources(resources: Resources) {
        fakeResources = resources
    }

    fun setDemolitionResources(resources: Resources) {
        demolitionResources = resources
    }

    fun getConcreteResourceVillages(): Collection<AllyVillage> = when (concreteResources) {
        null -> listOf()
        else -> concreteResources!!.villages
    }

    fun getFakeResourceVillages(): Collection<AllyVillage> = when (fakeResources) {
        null -> listOf()
        else -> fakeResources!!.villages
    }

    fun getDemolitionResourceVillages(): Collection<AllyVillage> = when (demolitionResources) {
        null -> listOf()
        else -> demolitionResources!!.villages
    }

    fun getAllPlayers(): Collection<Player> {
        val players = mutableListOf<Player>()

        concreteResources?.players?.let { players.addAll(it) }
        fakeResources?.players?.let { players.addAll(it) }
        demolitionResources?.players?.let { players.addAll(it) }

        return players
    }

    fun dropPlayer(player: Player) {
        concreteResources = concreteResources
            ?.copy(
                players = concreteResources?.players?.minusElement(player) ?: listOf(),
                villages = concreteResources?.villages?.filter { it.owner != player } ?: listOf()
            )

        fakeResources = fakeResources
            ?.copy(
                players = fakeResources?.players?.minusElement(player) ?: listOf(),
                villages = fakeResources?.villages?.filterNot { it.owner != player } ?: listOf()
            )

        demolitionResources = demolitionResources
            ?.copy(
                players = demolitionResources?.players?.minusElement(player) ?: listOf(),
                villages = demolitionResources?.villages?.filterNot { it.owner != player } ?: listOf()
            )
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

    fun getTargetGroups(type: AssignerType): Collection<TargetGroup> =
        targetGroups.getOrDefault(
            key = type,
            defaultValue = listOf()
        ).toList()

    fun getTargetGroups(vararg types: AssignerType): Collection<TargetGroup> =
        types.flatMap { type -> getTargetGroups(type) }
}