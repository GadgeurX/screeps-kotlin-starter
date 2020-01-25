package starter

import screeps.api.*
import screeps.utils.contains
import screeps.utils.memory.memory
import screeps.utils.mutableRecordOf

var RoomMemory.RcontainerPriority: MutableRecord<String, Int> by memory { mutableRecordOf<String, Int>("" to 0)}

var RoomMemory.RcontainerTarget: MutableRecord<String, String> by memory { mutableRecordOf<String, String>("" to "")}


fun RoomMemory.getContainerTarget(id: String): String? {
    if (RcontainerTarget.contains(id))
        return RcontainerTarget[id]
    else
    {
        setupContainerMemory(id)
        if (RcontainerTarget.contains(id))
            return RcontainerTarget[id]
        return null
    }
}

fun RoomMemory.getContainerPriority(id: String): Int? {
    if (RcontainerPriority.contains(id))
        return RcontainerPriority[id]
    else
    {
        setupContainerMemory(id)
        if (RcontainerPriority.contains(id))
            return RcontainerPriority[id]
        return null
    }
}

fun RoomMemory.setupContainerMemory(id: String) {
    val flags = (Game.getObjectById<Identifiable>(id) as? RoomObject)?.pos?.lookFor<Flag>(LOOK_FLAGS)
    if (flags != null && flags.isNotEmpty()) {
        if (flags[0].memory.containerPriority != null) {
            RcontainerPriority[id] = flags[0].memory.containerPriority
        }
        if (flags[0].memory.containerTarget != null) {
            RcontainerTarget[id] = flags[0].memory.containerTarget
        }
        flags[0].remove()
    }
}