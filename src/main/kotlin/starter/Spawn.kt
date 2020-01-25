package starter

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.unsafe.jsObject
import kotlin.js.Math


fun StructureSpawn.updateCreeps(room: Room, creeps: Array<Creep>) {

    for ((role, min) in Role.minPopulations) {
        if (role == Role.HARVESTER) {
            val roleCreeps = creeps.filter { creep ->
                creep.my == true &&
                creep.memory.role == role.name }
            room.find(FIND_SOURCES).forEach { source ->
                var count = 0
                roleCreeps.forEach {
                    if (it.memory?.assignedEnergySource == source.id)
                        count++
                }
                if (count < min) {
                    spawnCreep(role, CreepSpawnOptions(role, source.id))
                    return
                }
            }
        } else {
            val roleCreeps = creeps.filter { creep -> creep.my == true && creep.memory.role == role.name }
            if (roleCreeps.size < min) {
                spawnCreep(role)
                return
            }
        }
    }
}

private fun StructureSpawn.spawnCreep(role: Role, spawnOption: CreepSpawnOptions = CreepSpawnOptions(role)) {
    val newName = "${role.name}_${Game.time}"
    val body = this.createCreepBody(role)
    val code = this.spawnCreep(body, newName, options {memory = spawnOption.memory})

    when (code) {
        OK -> console.log("spawning $newName with body $body")
        ERR_BUSY, ERR_NOT_ENOUGH_ENERGY -> run { } // do nothing
        else -> {console.log("unhandled error code $code \n body : $body \n options: ${spawnOption.memory?.role}"); console.log(spawnOption.memory?.assignedEnergySource)}
    }
}

fun StructureSpawn.createCreepBody(role: Role): Array<BodyPartConstant> {
    val body = mutableListOf<BodyPartConstant>(WORK, MOVE, CARRY)
    val energyAvailable = this.room.energyAvailable
    var energyUse = 200
    var currentPart: BodyPartConstant? = null

    while (energyUse < energyAvailable) {
        if (currentPart != null)
            body.add(currentPart)
        val random = Math.random()
        val ratioEntries = Role.partRatio[role]?.entries?:setOf()
        for (index in 0..ratioEntries.size) {
            if (random < ratioEntries.elementAt(index).component2()) {
                currentPart = ratioEntries.elementAt(index).component1()
                energyUse += Role.partCost[currentPart]?:0
                break
            }
        }
    }
    return body.toTypedArray()
}