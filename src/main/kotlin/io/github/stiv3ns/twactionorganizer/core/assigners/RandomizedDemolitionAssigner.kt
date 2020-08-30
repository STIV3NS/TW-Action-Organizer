package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class RandomizedDemolitionAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
) : RandomizedRamAssigner(targets, resources, mainReferencePoint, isAssigningFakes = true, type = AssignerType.RANDOMIZED_DEMOLITION)
