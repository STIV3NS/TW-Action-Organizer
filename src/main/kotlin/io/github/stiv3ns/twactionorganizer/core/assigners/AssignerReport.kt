package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Assignment
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage

class AssignerReport(
    val result: List<Assignment>,
    val unusedResources: Resources,
    val unassignedTargets: Collection<TargetVillage>,
    var name: String = ""
)
{
    val numberOfUnassignedTargets get() =
        unassignedTargets.size

    val numberOfMissingResources get() =
        unassignedTargets.map { it.numberOfAttacks }.sum()
}