package io.github.stiv3ns.twactionorganizer.core.villages

class TargetVillage(
    x: Int,
    y: Int,
    id: Int,
    ownerNickname: String,
    val numberOfAttacks: Int,
    val delayInMinutes: Long = 0
) : Village(x, y, id, ownerNickname)
{
    constructor(village: Village, numberOfAttacks: Int, delayInMinutes: Long = 0)
        : this(village.x, village.y, village.id, village.ownerNickname, numberOfAttacks, delayInMinutes)

    fun withDelayInMinutes(delay: Long): TargetVillage =
        TargetVillage(village = this, numberOfAttacks, delay)

    fun withNumberOfAttacks(newNumber: Int): TargetVillage =
        TargetVillage(village = this, numberOfAttacks = newNumber, delayInMinutes = delayInMinutes)
}