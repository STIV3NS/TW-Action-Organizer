package io.github.stiv3ns.twactionorganizer.core

import io.github.stiv3ns.twactionorganizer.core.assigners.AssignerType
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage

class TWAOUnitOfWork {
    private var world: World? = null

    private var concreteResources: Resources? = null
    private var additionalResources: Resources? = null

    private val targetGroups = mutableMapOf< AssignerType, MutableList<TargetGroup> >()

    fun setWorld(world: World) {
        this.world = world
    }

    fun getWorld(): World
        = when(world) {
            null -> throw MissingConfigurationException("TWAOUnitOfWork: world not set.")
            else -> world!!
        }

    fun setConcreteResources(res: Resources) {
        concreteResources = res
    }

    fun setAdditionalResources(res: Resources) {
        additionalResources = res
    }

    fun getConcreteResourceVillages(): MutableList<AllyVillage>
        = when(concreteResources) {
            null -> throw MissingConfigurationException("TWAOUnitOfWork: concreteResources not set.")
            else -> concreteResources!!.villages.toMutableList()
        }

    fun getAdditionalResourceVillages(): MutableList<AllyVillage>
        = when(additionalResources) {
            null -> mutableListOf()
            else -> additionalResources!!.villages.toMutableList()
        }

    fun dropPlayer(player: Player) {
        concreteResources?.players?.remove(player)
        additionalResources?.players?.remove(player)

        concreteResources?.villages?.removeAll { it.owner == player }
        additionalResources?.villages?.removeAll { it.owner == player }
    }

    fun dropConcreteVillage(vil: AllyVillage) {
        val wasRemoved: Boolean? = concreteResources?.villages?.remove(vil)

        when(wasRemoved) {
            true -> vil.owner.unregisterVillage()
        }
    }

    fun dropAdditionalVillage(vil: AllyVillage) {
        additionalResources?.villages?.remove(vil)
    }

    fun setPlayerNoblesNumber(player: Player, newValue: Int) {
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

    fun getTargetGroups(type: AssignerType): MutableList<TargetGroup>
        = targetGroups.getOrDefault(
            key = type,
            defaultValue = mutableListOf()
        ).toMutableList()
}