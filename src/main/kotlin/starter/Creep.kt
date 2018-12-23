package starter

import types.base.global.*
import types.base.prototypes.*
import types.base.prototypes.structures.Structure
import types.base.prototypes.structures.StructureContainer

fun Creep.upgrade() {
    if (carry.energy == 0 || memory.modeLowEnergy) {
        findEnergyForWork()
    } else {
        this.memory.inTargetId = null
        if (upgradeController(this.room.controller!!) == ERR_NOT_IN_RANGE) {
            moveTo(this.room.controller!!.pos)
        }
    }
}

fun Creep.build() {

    if (carry.energy == 0 || memory.modeLowEnergy) {
        findEnergyForWork()
    } else {
        val targets = room.findConstructionSites()
        if (targets.isNotEmpty()) {
            if (build(targets[0]) == ERR_NOT_IN_RANGE) {
                moveTo(targets[0].pos)
            }
        }
    }
}

fun Creep.harvest() {
    if (carry.energy < carryCapacity) {
        val source = Game.getObjectById<Source>(memory.assignedEnergySource)
        if (source != null && harvest(source) == ERR_NOT_IN_RANGE) {
            moveTo(source.pos)
        }
    } else {
        var target: Structure? = null
        this.room.findStructures().filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
            if (room.memory.getcontainerTarget(it.id) == memory.assignedEnergySource)
                target = it
        }

        if (target != null) {
            if (transfer(target!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(target!!.pos)
            }
        } else if (room.getSpawn() != null){
            if (transfer(room.getSpawn()!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(room.getSpawn()!!.pos)
            }

        }
    }
}

fun Creep.findEnergyForWork() {
    if (this.memory.inTargetId == null) {
        this.memory.inTargetId = findHighPrioTarget()
    }
    if (this.memory.inTargetId != null) {
        val container = Game.getObjectById<Structure>(this.memory.inTargetId)
        if (container != null)
            if (this.withdraw(container, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(container.pos)
            }
    } else {
        if (memory.modeLowEnergy && room.getSpawn() != null && room.getSpawn()!!.energy > 200) {

            if (this.withdraw(room.getSpawn()!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(room.getSpawn()!!.pos)
            } else {
                memory.modeLowEnergy = false
            }
            return
        }
        memory.modeLowEnergy = true
        if (carry.energy == carryCapacity)
            memory.modeLowEnergy = false
        val sources = room.findEnergy()
        if (sources.isNotEmpty())
            if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
                moveTo(sources[0].pos)
            }
    }
}

fun Creep.findLowPrioTarget() {

}

fun Creep.findHighPrioTarget(): String? {
    var lowestPrio = -1
    var idTarget: String? = null
    this.room.findStructures().filter { it.structureType == STRUCTURE_CONTAINER }.map { it as StructureContainer }.forEach {
        if (it.store.energy > 0 && room.memory.getcontainerPriority(it.id) > lowestPrio) {
            lowestPrio = room.memory.getcontainerPriority(it.id) as Int
            idTarget = it.id
        }
    }
    return idTarget
}