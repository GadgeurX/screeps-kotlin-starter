package starter

import screeps.api.CreepMemory
import screeps.api.Game
import screeps.api.RoomPosition
import screeps.utils.memory.memory

var CreepMemory.role: String by memory { Role.UNASSIGNED.name }

var CreepMemory.modeLowEnergy: Boolean by memory { false }

var CreepMemory.inTargetId: String?
    get() = this.asDynamic().inTargetId as? String
    set(value) = run { this.asDynamic().inTargetId = value }

var CreepMemory.outTargetId: String?
    get() = this.asDynamic().outTargetId as? String
    set(value) = run { this.asDynamic().outTargetId = value }

var CreepMemory.assignedEnergySource: String?
    get() = this.asDynamic().assignedEnergySource as? String
    set(value) = run { this.asDynamic().assignedEnergySource = value }

var CreepMemory.targetPosition: Pair<Int, Int> by memory { Pair(-1, -1) }
var CreepMemory.currentRoomName: String by memory { "" }
