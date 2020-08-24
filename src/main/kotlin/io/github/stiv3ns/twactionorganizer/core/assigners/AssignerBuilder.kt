package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

class AssignerBuilder {
    /**
     * Determines assigner behavior
     *
     * ---------------------------------------------------------------------------------------------------------------
     * [RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with nearest target.
     *---------------------------------------------------------------------------------------------------------------
     * [REVERSED_RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick an TARGET village that is closest to the referencePoint and link it with nearest ally village.
     *---------------------------------------------------------------------------------------------------------------
     * [RANDOMIZED_RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with RANDOM target.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_RAM]:
     * output assigment: Fake attack without noble
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with nearest target.
     *---------------------------------------------------------------------------------------------------------------
     * [RANDOMIZED_FAKE_RAM]:
     * output assigment: Fake attack without noble
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with RANDOM target.
     *---------------------------------------------------------------------------------------------------------------
     * [NOBLE]:
     * output assigment: Concrete attack with noble
     * behavior: Pick an TARGET village that is closest to the relativityPoint and link it with nearest ally village.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_NOBKE]:
     * output assigment: Fake attack with noble
     * behavior: Pick an TARGET village that is closest to the relativityPoint and link it with nearest ally village.
     * ---------------------------------------------------------------------------------------------------------------
     * [DEMOLITION]:
     * output assigment: Demolition attack
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with nearest target.
     *---------------------------------------------------------------------------------------------------------------
     * [RANDOMIZED_DEMOLITION]:
     * output assigment: Demolition attack
     * behavior: Pick an ALLY village that is the farthest from referencePoint and link it with RANDOM target.
     *---------------------------------------------------------------------------------------------------------------
     */

    var targets: MutableList<TargetVillage>? = null
    var resources: MutableList<AllyVillage>? = null
    var mainReferencePoint: Village? = null
    var type: AssignerType? = null
    var maxNobleRange: Int? = null

    /**
     * Requires [targets], [resources], [mainReferencePoint], [type] to be set.
     * When assigning (fake)nobles [maxNobleRange] is also obligatory.
     * When using randomized assigner [mainReferencePoint] is not obligatory.
     */
    @Throws(MissingConfigurationException::class)
    fun build(): Assigner {
        if (obligatoryHeadersAreNotSet()
            || (requestedNobleAssigner() && maxNobleRangeIsNotSet())
        ) {
            throwException()
        }

        val assigner = when (type!!) {
            AssignerType.RAM -> StandardRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false
            )
            AssignerType.REVERSED_RAM -> ReversedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false
            )
            AssignerType.RANDOMIZED_RAM -> RandomizedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false
            )
            AssignerType.FAKE_RAM -> StandardRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true
            )
            AssignerType.RANDOMIZED_FAKE_RAM -> RandomizedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true
            )
            AssignerType.NOBLE -> NobleAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false,
                maxNobleRange = maxNobleRange!!
            )
            AssignerType.FAKE_NOBLE -> NobleAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true,
                maxNobleRange = maxNobleRange!!
            )
            AssignerType.DEMOLITION -> StandardDemolitionAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!
            )
            AssignerType.RANDOMIZED_DEMOLITION -> RandomizedDemolitionAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!
            )
        }

        clear()
        return assigner
    }


    fun targets(targets: MutableList<TargetVillage>) =
        apply { this.targets = targets }

    fun resources(resources: MutableList<AllyVillage>) =
        apply { this.resources = resources }

    fun mainReferencePoint(referencePoint: Village) =
        apply { this.mainReferencePoint = referencePoint }

    fun type(type: AssignerType) =
        apply { this.type = type }

    fun maxNobleRange(range: Int) =
        apply { this.maxNobleRange = range }

    fun clear() =
        apply {
            targets = null
            resources = null
            mainReferencePoint = null
            type = null
            maxNobleRange = null
        }


    private fun obligatoryHeadersAreNotSet() =
        targets == null
        || resources == null
        || type == null
        || mainReferencePoint == null

    private fun requestedNobleAssigner() =
        type!! in listOf(AssignerType.NOBLE, AssignerType.FAKE_NOBLE)

    private fun maxNobleRangeIsNotSet() =
        maxNobleRange == null

    private fun throwException() {
        var exceptionMsg = "AssignerBuilder missing: "
        if (!obligatoryHeadersAreNotSet()) {
            if (targets == null) exceptionMsg += "targets, "
            if (resources == null) exceptionMsg += "resources, "
            if (mainReferencePoint == null) exceptionMsg += "mainReferencePoint, "
            if (type == null) exceptionMsg += "type, "
        }

        if (requestedNobleAssigner() && maxNobleRangeIsNotSet()) exceptionMsg += "maxNobleRange"
    }
}