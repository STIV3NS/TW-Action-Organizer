package io.github.stiv3ns.twactionorganizer.core.assigners

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
enum class AssignerType {
    RAM,
    REVERSED_RAM,
    RANDOMIZED_RAM,
    FAKE_RAM,
    RANDOMIZED_FAKE_RAM,
    NOBLE,
    FAKE_NOBLE,
    DEMOLITION,
    RANDOMIZED_DEMOLITION
}
