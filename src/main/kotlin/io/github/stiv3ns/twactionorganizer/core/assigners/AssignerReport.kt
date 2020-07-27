package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage

class AssignerReport (
    val unusedResourceVillages: List<AllyVillage>,
    val unassignedTargetVillages: List<TargetVillage>,
    var name: String = ""
) {
    val numberOfUnusedResources
        get() = unusedResourceVillages.size

    val numberOfUnassignedTargets
        get() = unassignedTargetVillages.size

    val numberOfMissingResources
        get() = unassignedTargetVillages.map{ it.numberOfAttacks }.sum()
}