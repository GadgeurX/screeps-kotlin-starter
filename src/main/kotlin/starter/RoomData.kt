package starter

import screeps.api.MineralConstant

data class RoomData (
    val controllerLevel: Int? = null,
    val nbSources: Int = 0,
    val ownerPlayerId: String? = null,
    val nbCreeps: Int = 0,
    val resource: List<MineralConstant> = listOf()
)
