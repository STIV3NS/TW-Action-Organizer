package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Player
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class RandomizedDemolitionAssigner internal constructor(
    targets: Collection<TargetVillage>,
    resources: Collection<AllyVillage>,
    mainReferencePoint: Village
) : RandomizedRamAssigner(targets, resources, mainReferencePoint, isAssigningFakes = true)
{
    override val fakeAction = Player::putDemolitionAssignment
    override val offAction = Player::putDemolitionAssignment
}