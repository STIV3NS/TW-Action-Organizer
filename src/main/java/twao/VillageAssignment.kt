package twao

import twao.villages.Village

data class VillageAssignment(
        val departure: Village,
        val destination: Village,
        val squaredDistance: Int
) {
    override fun toString()
        = "$departure -> $destination"
}