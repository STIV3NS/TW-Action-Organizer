package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import java.lang.IllegalStateException

class TWAOUnitOfWork {
    private var world: World? = null

    private var concreteResources: Resources? = null
    private var additionalResources: Resources? = null

    private val targetGroups = mutableMapOf<AssignerType, MutableList<TargetGroup>>()

    fun setWorld(world: World) {
        this.world = world
    }

    fun getWorld(): World = world ?: throw MissingConfigurationException("TWAOUnitOfWork: world not set")

    fun setConcreteResources(resources: Resources) {
        concreteResources = resources
    }

    fun setAdditionalResources(resources: Resources) {
        additionalResources = resources
    }

    fun getConcreteResourceVillages(): MutableList<AllyVillage> = when (concreteResources) {
        null -> throw MissingConfigurationException("TWAOUnitOfWork: concreteResources not set")
        else -> concreteResources!!.villages
    }

    fun getAdditionalResourceVillages(): MutableList<AllyVillage> = when (additionalResources) {
        null -> mutableListOf()
        else -> additionalResources!!.villages
    }

    fun dropPlayer(player: Player) {
        concreteResources?.players?.remove(player)
        additionalResources?.players?.remove(player)

        concreteResources?.villages?.removeAll { it.owner == player }
        additionalResources?.villages?.removeAll { it.owner == player }
    }

    fun dropVillage(vil: AllyVillage) {
        val wasConcrete: Boolean? = concreteResources?.villages?.remove(vil)

        if (wasConcrete == true) vil.owner.unregisterVillage()
        else additionalResources?.villages?.remove(vil)
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

    fun getTargetGroups(type: AssignerType): List<TargetGroup> =
        targetGroups.getOrDefault(
            key = type,
            defaultValue = mutableListOf()
        ).toList()
}