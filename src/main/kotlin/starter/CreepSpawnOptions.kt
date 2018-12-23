package starter

import types.base.global.CreepMemory
import types.base.prototypes.structures.SpawnOptions

class CreepSpawnOptions(role: Role, assignedEnergySource: String? = null) : SpawnOptions {
    override val memory = object : CreepMemory {
        val role: String = role.name
        val assignedEnergySource: String? = assignedEnergySource
    }
}