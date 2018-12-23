package starter

import types.base.global.CreepMemory
import types.extensions.memory.memoryOrDefault

var CreepMemory.role: String by memoryOrDefault(Role.UNASSIGNED.name)

var CreepMemory.modeLowEnergy: Boolean by memoryOrDefault(false)

var CreepMemory.inTargetId: String?
    get() = this.asDynamic().inTargetId as? String
    set(value) = run { this.asDynamic().inTargetId = value }

var CreepMemory.outTargetId: String?
    get() = this.asDynamic().outTargetId as? String
    set(value) = run { this.asDynamic().outTargetId = value }

var CreepMemory.assignedEnergySource: String?
    get() = this.asDynamic().assignedEnergySource as? String
    set(value) = run { this.asDynamic().assignedEnergySource = value }
