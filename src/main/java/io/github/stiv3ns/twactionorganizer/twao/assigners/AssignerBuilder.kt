package io.github.stiv3ns.twactionorganizer.twao.assigners

import io.github.stiv3ns.twactionorganizer.twao.villages.AllyVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.TargetVillage
import io.github.stiv3ns.twactionorganizer.twao.villages.Village

class AssignerBuilder {
    /**
     * Determines assigner behavior
     *
     * ---------------------------------------------------------------------------------------------------------------
     * [RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick ALLY village that is the farthest from referencePoint and link it with nearest target.
     *---------------------------------------------------------------------------------------------------------------
     * [REVERSED_RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick TARGET village that is closest to the referencePoint and link it with nearest ally village.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_RAM]:
     * output assigment: Fake attack without noble
     * behavior: Pick ALLY village that is the farthest from referencePoint and link it with nearest target.
     *---------------------------------------------------------------------------------------------------------------
     * [NOBLE]:
     * output assigment: Concrete attack with noble
     * behavior: Pick TARGET village that is closest to the relativityPoint and link it with nearest ally village.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_NOBKE]:
     * output assigment: Fake attack with noble
     * behavior: Pick TARGET village that is closest to the relativityPoint and link it with nearest ally village.
     * ---------------------------------------------------------------------------------------------------------------
     */
    enum class AssignerType {
        RAM,
        REVERSED_RAM,
        FAKE_RAM,
        NOBLE,
        FAKE_NOBLE
    }

    var targets: MutableList<TargetVillage>? = null
    var resources: MutableList<AllyVillage>? = null
    var mainReferencePoint: Village? = null
    var type: AssignerType? = null
    var maxNobleRange: Int? = null

    /**
     * Requires [targets], [resources], [mainReferencePoint], [type] to be set.
     * !!! If assigning (fake)nobles [maxNobleRange] is also obligatory. !!!
     */
    fun build(): Assigner? {
        if (targets == null || resources == null || mainReferencePoint == null || type == null)
            return null
        if (type!! in listOf(AssignerType.NOBLE, AssignerType.FAKE_NOBLE)
                && maxNobleRange == null)
            return null

        val assigner = when (type) {
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
            AssignerType.FAKE_RAM -> StandardRamAssigner(
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
            else -> null
        }

        clear()
        return assigner
    }


    fun targets(targets: MutableList<TargetVillage>)
        = apply { this.targets = targets }

    fun resources(resources: MutableList<AllyVillage>)
        = apply { this.resources = resources }

    fun mainReferencePoint(referencePoint: Village)
        = apply { this.mainReferencePoint = referencePoint }

    fun type(type: AssignerType)
        = apply { this.type = type }

    fun maxNobleRange(range: Int)
        = apply { this.maxNobleRange = range }

    fun clear() {
        apply {
            targets = null
            resources = null
            mainReferencePoint = null
            type = null
            maxNobleRange = null
        }
    }
}