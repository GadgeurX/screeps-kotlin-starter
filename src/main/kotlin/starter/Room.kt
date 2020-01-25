package starter

import screeps.api.*
import screeps.api.Room
import screeps.api.STRUCTURE_CONTAINER
import screeps.api.STRUCTURE_EXTENSION
import screeps.api.STRUCTURE_SPAWN
import screeps.api.structures.StructureExtension
import screeps.api.structures.StructureSpawn

fun Room.update() {

    val creeps = this.find(FIND_CREEPS)
    val spawn = getSpawn()

    if (spawn?.my == true) {
        spawn.updateCreeps(this, creeps)
        var construct = false
        if (!hasEnoughContainer()) {
            construct = ConstructionUtils.constructSourceContainer(this)
            if (!construct)
                construct = ConstructionUtils.constructSpawnContainer(this)
        }
        if (!construct)
            construct = ConstructionUtils.constructExtension(this)
        if (!construct)
            construct = ConstructionUtils.constructRoadArroundStruct(this)
        if (!construct)
            construct = ConstructionUtils.constructRoadArroundContainer(this)
    }

    for (creep in creeps.filter { creep ->
        creep.my == true }) {
        when (creep.memory.role) {
            Role.HARVESTER.name -> creep.harvest()
            Role.BUILDER.name -> creep.build()
            Role.UPGRADER.name -> creep.upgrade()
            Role.TRANSPORTER.name -> creep.transport()
        }
    }
}

fun Room.isMyRoom(): Boolean? {
    return this.controller?.my
}

fun Room.getSpawn(): StructureSpawn? {
    val spawns = this.find(FIND_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_SPAWN) }

    return if (spawns.isNotEmpty())
        spawns[0] as StructureSpawn
    else
        null
}

fun Room.getRoomExtension(): List<StructureExtension> {
    return this.find(FIND_STRUCTURES)
            .filter { (it.structureType == STRUCTURE_EXTENSION) }
            .map { it as StructureExtension }
}

fun Room.canConstructSite(): Boolean {
    return this.find(FIND_CONSTRUCTION_SITES).isEmpty()
}

fun Room.hasEnoughContainer(): Boolean {
    return this.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.size >= find(FIND_SOURCES).size + 1
}