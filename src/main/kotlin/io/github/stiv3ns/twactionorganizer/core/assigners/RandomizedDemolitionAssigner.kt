package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class RandomizedDemolitionAssigner internal constructor(
    name: String,
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
) : RandomizedRamAssigner(name, targets, resources, mainReferencePoint, type = AssignerType.RANDOMIZED_DEMOLITION)
