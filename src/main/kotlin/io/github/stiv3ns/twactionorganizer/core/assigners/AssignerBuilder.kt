package io.github.stiv3ns.twactionorganizer.core.assigners

import io.github.stiv3ns.twactionorganizer.core.Resources
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.MissingConfigurationException
import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.core.villages.Village

data class AssignerBuilder(
    val targets: Collection<TargetVillage>? = null,
    val resources: Resources? = null,
    val mainReferencePoint: Village? = null,
    val type: AssignerType? = null,
    val maxNobleRange: Int? = null,
    val minNobleRange: Int = -1
) {
    /**
     * Requires [targets], [resources], [mainReferencePoint], [type] to be set.
     * When assigning (fake)nobles [maxNobleRange] is also obligatory.
     */
    private val obligatoryHeadersAreNotSet
        get() =
               targets == null
            || resources == null
            || type == null
            || mainReferencePoint == null

    private val requestedNobleAssigner
        get() = type!! in listOf(AssignerType.NOBLE, AssignerType.FAKE_NOBLE)

    private val maxNobleRangeIsNotSet
        get() = maxNobleRange == null

    @Throws(MissingConfigurationException::class)
    fun build(): Assigner {
        if (obligatoryHeadersAreNotSet
            or (requestedNobleAssigner and maxNobleRangeIsNotSet)
        ) throwException()

        val assigner = when (type!!) {
            AssignerType.RAM -> StandardRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false,
                type = type,
            )
            AssignerType.REVERSED_RAM -> ReversedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false,
                type = type,
            )
            AssignerType.RANDOMIZED_RAM -> RandomizedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false,
                type = type,
            )
            AssignerType.FAKE_RAM -> StandardRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true,
                type = type,
            )
            AssignerType.RANDOMIZED_FAKE_RAM -> RandomizedRamAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true,
                type = type,
            )
            AssignerType.NOBLE -> NobleAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = false,
                type = type,
                maxNobleRange = maxNobleRange!!,
                minNobleRange = minNobleRange,
            )
            AssignerType.FAKE_NOBLE -> NobleAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
                isAssigningFakes = true,
                type = type,
                maxNobleRange = maxNobleRange!!,
                minNobleRange = minNobleRange,
            )
            AssignerType.DEMOLITION -> StandardDemolitionAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
            )
            AssignerType.RANDOMIZED_DEMOLITION -> RandomizedDemolitionAssigner(
                targets!!,
                resources!!,
                mainReferencePoint!!,
            )
        }

        return assigner
    }


    fun targets(newTargets: Collection<TargetVillage>) =
        copy(targets = newTargets)

    fun resources(newResources: Resources) =
        copy(resources = newResources)

    fun mainReferencePoint(newReferencePoint: Village) =
        copy(mainReferencePoint = newReferencePoint)

    fun type(newType: AssignerType) =
        copy(type = newType)

    fun maxNobleRange(newRange: Int) =
        copy(maxNobleRange = newRange)

    fun clear() =
        AssignerBuilder()

    private fun throwException(): Nothing {
        var exceptionMsg = "AssignerBuilder missing: "

        if (obligatoryHeadersAreNotSet) {
            if (targets == null) exceptionMsg += "targets, "
            if (resources == null) exceptionMsg += "resources, "
            if (mainReferencePoint == null) exceptionMsg += "mainReferencePoint, "
            if (type == null) exceptionMsg += "type, "
        }

        if (requestedNobleAssigner and maxNobleRangeIsNotSet) exceptionMsg += "maxNobleRange"

        throw MissingConfigurationException(exceptionMsg)
    }
}