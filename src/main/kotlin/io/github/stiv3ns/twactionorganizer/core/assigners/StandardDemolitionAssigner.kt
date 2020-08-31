package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

open class StandardDemolitionAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Resources,
    mainReferencePoint: Village,
) : StandardRamAssigner(targets, resources, mainReferencePoint, type = AssignerType.DEMOLITION)