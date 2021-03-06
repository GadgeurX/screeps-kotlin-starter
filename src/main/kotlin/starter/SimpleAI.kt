package starter

import screeps.api.*
import screeps.utils.contains
import screeps.utils.containsKey
import screeps.utils.toMap
import screeps.utils.unsafe.delete
import starter.state.InitState
import starter.state.StateMachine

fun gameLoop() {

    val creepsMap = Game.creeps.toMap()
    houseKeeping(creepsMap)
    val creeps = creepsMap.values
    val rooms = Game.rooms.toMap().values.toList()

    if (!Memory.map.containsKey(Pair(0, 0))) {
        Memory.map[Pair(0, 0)] = RoomData(
                rooms[0].controller?.level,
                rooms[0].find(FIND_SOURCES).size,
                rooms[0].controller?.owner?.username,
                rooms[0].find(FIND_HOSTILE_CREEPS).size,
                rooms[0].find(FIND_MINERALS).map { it.mineralType },
                rooms[0].name
        )
    }

    rooms.forEach {
        it.update()
    }

    StateMachine(InitState()).update()
}

private fun houseKeeping(creeps: Map<String, Creep>) {
    if (Memory.creeps.asDynamic() != null) {
        for ((creepName, _) in Memory.creeps) {
            if (creeps[creepName] == null) {
                console.log("deleting obselete memory entry for creep $creepName")
                delete(Memory.asDynamic().creeps[creepName])
            }
        }
    }
}
