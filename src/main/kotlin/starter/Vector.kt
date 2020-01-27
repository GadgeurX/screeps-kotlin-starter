package starter

import screeps.api.RoomPosition

data class Vector(var x: Int = 0, var y: Int = 0) {

    constructor(roomPosition: RoomPosition) : this(roomPosition.x, roomPosition.y)

    operator fun minus(v: Vector) = Vector(x - v.x, y - v.y)
    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y)

//    operator fun minusAssign(v: Vector) {
//        this.x -= v.x
//        this.y -= v.y
//    }
//    operator fun plusAssign(v: Vector) {
//        this.x += v.x
//        this.y += v.y
//    }

    fun toRoomPosition(roomName: String): RoomPosition {
        return RoomPosition(x, y, roomName)
    }

}