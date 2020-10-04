package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage

class AssignerReport(
    val name: String,
    val result: List<Assignment>,
    val unusedResources: Resources,
    val unassignedTargets: Collection<TargetVillage>
) {
    val numberOfUnassignedTargets = unassignedTargets.size
    val numberOfMissingResources = unassignedTargets.map { it.numberOfAttacks }.sum()
}