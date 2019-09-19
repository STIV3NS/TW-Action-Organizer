package twao.assigners

import twao.villages.AllyVillage
import twao.villages.TargetVillage
import twao.villages.Village

class AssignerBuilder {
    /**
     * Determines assigner behavior
     *
     * ---------------------------------------------------------------------------------------------------------------
     * [RAM]:
     * output assigment: Concrete attack without noble
     * behavior: Pick ALLY village that is the farthest from referencePoint and link it with nearest target. Repeat.
     *---------------------------------------------------------------------------------------------------------------
     * [REVERSED_RAM]:
     * output assigment: Concrete attack without noble
     * Pick TARGET village that is closest to the referencePoint and link it with nearest ally village. Repeat.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_RAM]:
     * output assigment: Fake attack without noble
     * Pick ALLY village that is the farthest from referencePoint and link it with nearest target. Repeat.
     *---------------------------------------------------------------------------------------------------------------
     * [NOBLE]:
     * output assigment: Concrete attack with noble
     * Pick TARGET village that is closest to the relativityPoint and link it with nearest ally village. Repeat.
     *---------------------------------------------------------------------------------------------------------------
     * [FAKE_NOBKE]:
     * output assigment: Fake attack with noble
     * Pick TARGET village that is closest to the relativityPoint and link it with nearest ally village. Repeat.
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

        return when (type) {
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
}