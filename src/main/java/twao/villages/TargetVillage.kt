package twao.villages

data class TargetVillage(
        override val x: Int,
        override val y: Int,
        private var _numberOfAttacks: Int
) : Village(x, y) {
    val numberOfAttacks
        get() = _numberOfAttacks

    fun attack()
        { _numberOfAttacks-- }

    fun isAssignCompleted(): Boolean
        = _numberOfAttacks <= 0
}