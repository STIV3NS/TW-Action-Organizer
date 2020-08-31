package io.github.stiv3ns.twactionorganizer.core.assigners

/**
 * Determines assigner behaviour
 */
enum class AssignerType {
    /**
     * output assigment: Concrete attack without noble.
     * behaviour: Pick an ALLY village that is the farthest from the referencePoint and link it with nearest target.
     */
    RAM,

    /**
     * output assigment: Concrete attack without noble.
     * behaviour: Pick an TARGET village that is the closest to the referencePoint and link it with nearest ally village.
     */
    REVERSED_RAM,

    /**
     * output assigment: Concrete attack without noble.
     * behaviour: Pick an ALLY village that is the farthest from the referencePoint and link it with RANDOM target.
     */
    RANDOMIZED_RAM,

    /**
     * output assigment: Fake attack without noble.
     * behaviour: Pick an ALLY village that is the farthest from the referencePoint and link it with nearest target.
     */
    FAKE_RAM,

    /**
     * output assigment: Fake attack without noble.
     * behaviour: Pick an ALLY village that is the farthest from the referencePoint and link it with RANDOM target.
     */
    RANDOMIZED_FAKE_RAM,

    /**
     * output assigment: Concrete attack with noble.
     * behaviour: Pick an TARGET village that is the closest to the referencePoint and link it with nearest ally village.
     */
    NOBLE,

    /**
     * output assigment: Fake attack with noble.
     * behaviour: Pick an TARGET village that is the closest to the referencePoint and link it with nearest ally village.
     */
    FAKE_NOBLE,

    /**
     * output assigment: Demolition attack.
     * behaviour: Pick an ALLY village that is the farthest from referencePoint and link it with nearest target.
     */
    DEMOLITION,

    /**
     * output assigment: Demolition attack.
     * behaviour: Pick an ALLY village that is the farthest from referencePoint and link it with RANDOM target.
     */
    RANDOMIZED_DEMOLITION
}
