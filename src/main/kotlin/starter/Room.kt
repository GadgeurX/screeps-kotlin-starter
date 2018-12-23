package starter

import types.base.global.STRUCTURE_CONTAINER
import types.base.global.STRUCTURE_EXTENSION
import types.base.global.STRUCTURE_SPAWN
import types.base.prototypes.*
import types.base.prototypes.structures.StructureExtension
import types.base.prototypes.structures.StructureSpawn

fun Room.update() {
    val creeps = this.findCreeps()
    val spawn = getSpawn()

    if (spawn?.my == true) {
        spawn.updateCreeps(this, creeps)
        var construct: Boolean
        construct = ConstructionUtils.constructExtension(this)
        if (!hasEnoughContainer() && !construct) {
            construct = ConstructionUtils.constructSpawnContainer(this)
            if (!construct)
                construct = ConstructionUtils.constructSourceContainer(this)
        }
    }

    for (creep in creeps) {
        when (creep.memory.role) {
            Role.HARVESTER.name -> creep.harvest()
            Role.BUILDER.name -> creep.build()
            Role.UPGRADER.name -> creep.upgrade()
        }
    }
}

fun Room.isMyRoom(): Boolean? {
    return this.controller?.my
}

fun Room.getSpawn(): StructureSpawn? {
    val spawns = this.findStructures()
            .filter { (it.structureType == STRUCTURE_SPAWN) }

    return if (spawns.isNotEmpty())
        spawns[0] as StructureSpawn
    else
        null
}

fun Room.getRoomExtension(): List<StructureExtension> {
    return this.findStructures()
            .filter { (it.structureType == STRUCTURE_EXTENSION) }
            .map { it as StructureExtension }
}

fun Room.canConstructSite(): Boolean {
    return this.findConstructionSites().isEmpty()
}

fun Room.hasEnoughContainer(): Boolean {
    return this.findStructures().filter { it.structureType == STRUCTURE_CONTAINER }.size >= findEnergy().size + 1
}