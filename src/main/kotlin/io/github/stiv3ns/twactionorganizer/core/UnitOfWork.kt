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

    fun getConcreteResources() = concreteResources ?: Resources.empty()

    fun getFakeResources() = fakeResources ?: Resources.empty()

    fun getDemolitionResources() = demolitionResources ?: Resources.empty()

    fun dropPlayer(player: Player) {
        concreteResources = concreteResources
            ?.copy(
                players = concreteResources?.players?.minusElement(player) ?: listOf(),
                villages = concreteResources?.villages?.filter { it.ownerNickname != player.nickname } ?: listOf()
            )

        fakeResources = fakeResources
            ?.copy(
                players = fakeResources?.players?.minusElement(player) ?: listOf(),
                villages = fakeResources?.villages?.filterNot { it.ownerNickname != player.nickname } ?: listOf()
            )

        demolitionResources = demolitionResources
            ?.copy(
                players = demolitionResources?.players?.minusElement(player) ?: listOf(),
                villages = demolitionResources?.villages?.filterNot { it.ownerNickname != player.nickname } ?: listOf()
            )
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