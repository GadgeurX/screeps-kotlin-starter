package starter

import screeps.api.FlagMemory
import screeps.utils.memory.memory

var FlagMemory.containerPriority: Int by memory { 0 }

var FlagMemory.containerTarget: String by memory { "" }