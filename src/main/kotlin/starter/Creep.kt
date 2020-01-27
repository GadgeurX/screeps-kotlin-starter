package starter

import screeps.api.*
import screeps.api.structures.Structure
import screeps.api.structures.StructureContainer
import screeps.api.structures.StructureExtension
import screeps.api.structures.StructureTower
import kotlin.math.absoluteValue
import kotlin.random.Random


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
        this.memory.inTargetId = null

        if (this.memory.outTargetId == null) {
            val repairTarget = room.find(FIND_STRUCTURES).filter { it.hits < it.hitsMax - (it.hitsMax / 10) && it.structureType != STRUCTURE_WALL }
            if (repairTarget.isNotEmpty())
                this.memory.outTargetId = repairTarget[0].id
            if (this.memory.outTargetId == null) {
                val buildTargets = room.find(FIND_CONSTRUCTION_SITES)
                if (buildTargets.isNotEmpty())
                    this.memory.outTargetId = buildTargets[0].id
            }
        } else {
            val repareTarget = Game.getObjectById<Structure>(this.memory.outTargetId)
            val buildTarget = Game.getObjectById<ConstructionSite>(this.memory.outTargetId)
            if (repareTarget == null && buildTarget == null)
                this.memory.outTargetId = null
            else {
                if (repareTarget != null) {
                    if (repareTarget.hits == repareTarget.hitsMax)
                        this.memory.outTargetId = null
                    if (repair(repareTarget) == ERR_NOT_IN_RANGE) {
                        moveTo(repareTarget.pos)
                    }
                }
                if (buildTarget != null) {
                    if (build(buildTarget) == ERR_NOT_IN_RANGE) {
                        moveTo(buildTarget.pos)
                    }
                }
            }
        }
    }
}

fun Creep.transport() {

    if (carry.energy == 0) {
        findEnergyForTansport()
    } else {
        findStructureForTansport()
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
        this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
            if (room.memory.getContainerTarget(it.id) == memory.assignedEnergySource)
                target = it
        }

        if (target != null) {
            if (transfer(target!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(target!!.pos)
            }
        } else if (room.getSpawn() != null) {
            if (transfer(room.getSpawn()!!, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(room.getSpawn()!!.pos)
            }

        }
    }
}
fun Creep.scoot() {
    if (this.memory.currentRoomName != room.name) {
        val exits = room.find(FIND_EXIT);
        val exit = exits[Random.nextInt().absoluteValue % exits.size]
        this.memory.targetPosition = Pair(exit.x, exit.y)
        this.memory.currentRoomName = room.name
    }
    this.moveTo(RoomPosition(this.memory.targetPosition.first, this.memory.targetPosition.second, room.name))
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
        val sources = room.find(FIND_SOURCES)
        if (sources.isNotEmpty())
            if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
                moveTo(sources[0].pos)
            }
    }
}

fun Creep.findEnergyForTansport() {
    this.memory.outTargetId = null
    if (this.memory.inTargetId == null) {
        this.memory.inTargetId = findLowPrioTarget()
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
        memory.modeLowEnergy = carry.energy != carryCapacity
        val sources = room.find(FIND_SOURCES)
        if (sources.isNotEmpty())
            if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
                moveTo(sources[0].pos)
            }
    }
}

fun Creep.findStructureForTansport() {
    this.memory.inTargetId = null
    if (this.memory.outTargetId == null) {
        if (this.room.getSpawn()?.energy ?: 0 < this.room.getSpawn()?.energyCapacity ?: 0)
            this.memory.outTargetId = this.room.getSpawn()?.id
        else {
            this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_EXTENSION }.map { it as StructureExtension }.forEach {
                if (this.memory.outTargetId == null && it.energy < it.energyCapacity)
                    this.memory.outTargetId = it.id
            }
            this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_TOWER }.map {it as StructureTower }.forEach {
                if (this.memory.outTargetId == null && it.energy < it.energyCapacity / 2)
                    this.memory.outTargetId = it.id
            }
            if (this.memory.outTargetId == null)
                this.memory.outTargetId = findHighPrioTargetForTransport()
        }
    }
    if (this.memory.outTargetId != null) {
        val container = Game.getObjectById<Structure>(this.memory.outTargetId)
        if ((container as? EnergyContainer)?.energy ?: 0 >= (container as? EnergyContainer)?.energyCapacity ?: 0)
            this.memory.outTargetId = null
        if (container != null)
            if (this.transfer(container, RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(container.pos)
            }
    }
}


fun Creep.findLowPrioTarget(): String? {
    var lowestPrio = Int.MAX_VALUE
    var idTarget: String? = null
    var target: StructureContainer? = null
    this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.map { it as StructureContainer }.forEach {
        if (it.store.energy > 0 && room.memory.getContainerPriority(it.id) ?: Int.MAX_VALUE <= lowestPrio) {
            if (target == null || room.memory.getContainerPriority(it.id) != room.memory.getContainerPriority(target!!.id) || it.store.energy > target?.store?.energy ?: 0) {
                lowestPrio = room.memory.getContainerPriority(it.id) ?: Int.MAX_VALUE
                idTarget = it.id
                target = it
            }
        }
    }
    return idTarget
}

fun Creep.findHighPrioTarget(): String? {
    var lowestPrio = -1
    var idTarget: String? = null
    var target: StructureContainer? = null
    this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.map { it as StructureContainer }.forEach {
        if (it.store.energy > 0 && room.memory.getContainerPriority(it.id) ?: 0 >= lowestPrio) {
            if (target == null || room.memory.getContainerPriority(it.id) != room.memory.getContainerPriority(target!!.id) || it.store.energy > target?.store?.energy ?: 0) {
                lowestPrio = room.memory.getContainerPriority(it.id) ?: 0
                idTarget = it.id
                target = it
            }
        }
    }
    return idTarget
}

fun Creep.findHighPrioTargetForTransport(): String? {
    var lowestPrio = -1
    var idTarget: String? = null
    this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.map { it as StructureContainer }.forEach {
        if (it.store.energy < it.storeCapacity && room.memory.getContainerPriority(it.id) ?: 0 > lowestPrio) {
            lowestPrio = room.memory.getContainerPriority(it.id) ?: 0
            idTarget = it.id
        }
    }
    return idTarget
}