package twao.villages

data class TargetVillage(
        override val x: Int,
        override val y: Int,
        private var _numberOfAttacks: Int
) : Village(x, y) {
    val numberOfAttacks
        get() = _numberOfAttacks

    /** Decreases numbersOfAttacks by 1 */
    fun attack() {
        _numberOfAttacks--
    }

    /** Returns true only if there is no more attacks needed */
    fun isAssignCompleted(): Boolean
        = _numberOfAttacks <= 0
}