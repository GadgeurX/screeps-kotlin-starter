package starter

import screeps.api.CreepMemory

class CreepSpawnOptions(private val role: Role,
                        private val assignedEnergySource: String? = null,
                        private val worldPosition: Pair<Int, Int>? = null) {
    val memory: CreepMemory?
        get() = object : CreepMemory {}.apply {
            this.role = this@CreepSpawnOptions.role.name
            this.assignedEnergySource = this@CreepSpawnOptions.assignedEnergySource
            this.worldPosition = this@CreepSpawnOptions.worldPosition ?: Pair(0, 0)
        }
}