package starter

import types.base.global.Game
import types.base.global.LOOK_FLAGS
import types.base.global.RoomMemory
import types.base.prototypes.Flag
import types.base.prototypes.RoomObject

var RoomMemory.containerPriority: MutableMap<String, Int>?
    get() = this.asDynamic().containerPriority as? MutableMap<String, Int>
    set(value) = run { this.asDynamic().containerPriority = value }

var RoomMemory.containerTarget: MutableMap<String, String>?
    get() = this.asDynamic().containerTarget as? MutableMap<String, String>
    set(value) = run { this.asDynamic().containerTarget = value }


fun RoomMemory.getcontainerTarget(id: String): String? {
    if (containerTarget == null)
        containerTarget = mutableMapOf()
    if (containerTarget?.contains(id) == true)
        return containerTarget?.get(id)
    else
    {
        setupContainerMemory(id)
        if (containerTarget?.contains(id) == true)
            return containerTarget?.get(id)
        return null
    }
}

fun RoomMemory.getcontainerPriority(id: String): Int? {
    if (containerPriority == null)
        containerPriority = mutableMapOf()
    if (containerPriority?.contains(id) == true)
        return containerPriority?.get(id)
    else
    {
        setupContainerMemory(id)
        if (containerPriority?.contains(id) == true)
            return containerPriority?.get(id)
        return null
    }
}

fun RoomMemory.setupContainerMemory(id: String) {
    val flags = Game.getObjectById<RoomObject>(id)?.pos?.lookFor<Flag>(LOOK_FLAGS)
    if (flags != null && flags.isNotEmpty()) {
        if (flags[0].memory.containerPriority != null) {
            containerPriority?.put(id, flags[0].memory.containerPriority!!)
        }
        if (flags[0].memory.containerTarget != null) {
            containerTarget?.put(id, flags[0].memory.containerTarget!!)
        }
        flags[0].remove()
    }
}