package io.github.stiv3ns.twactionorganizer.core.villages

data class TargetVillage(
    override val x: Int,
    override val y: Int,
    override val id: Int,
    override val ownerNickname: String,
    val numberOfAttacks: Int,
    val delayInMinutes: Long = 0
) : Village(x, y, id, ownerNickname)
{
    constructor(village: Village, numberOfAttacks: Int, delayInMinutes: Long = 0)
        : this(village.x, village.y, village.id, village.ownerNickname, numberOfAttacks, delayInMinutes)

    override fun toString() = "$x|$y"
}