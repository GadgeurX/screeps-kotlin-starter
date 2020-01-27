package starter

import screeps.api.*
import screeps.api.structures.StructureTower

fun StructureTower.Update(room: Room) {
    val enemyCreeps = room.find(FIND_HOSTILE_CREEPS)
    if (enemyCreeps.isNotEmpty())
        this.attack(enemyCreeps[0])
    val repairTarget = room.find(FIND_STRUCTURES).filter { it.hits < it.hitsMax - (it.hitsMax / 10) && it.structureType != STRUCTURE_WALL }
    if (repairTarget.isNotEmpty())
        this.repair(repairTarget[0])
}
