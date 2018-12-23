package starter

import types.base.global.FlagMemory

var FlagMemory.containerPriority: Int?
    get() = this.asDynamic().containerPriority as? Int
    set(value) = run { this.asDynamic().containerPriority = value }

var FlagMemory.containerTarget: String?
    get() = this.asDynamic().containerTarget as? String
    set(value) = run { this.asDynamic().containerTarget = value }