package starter

import screeps.api.*
import screeps.utils.toMap
import screeps.utils.unsafe.delete

fun gameLoop() {
    val creepsMap = Game.creeps.toMap()
    houseKeeping(creepsMap)
    val creeps = creepsMap.values
    val rooms = Game.rooms.toMap().values

    rooms.forEach {
        it.update()
    }
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
