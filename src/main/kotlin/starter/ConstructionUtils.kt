package starter

import types.base.get
import types.base.global.Game
import types.base.global.STRUCTURE_CONTAINER
import types.base.global.STRUCTURE_EXTENSION
import types.base.prototypes.Room
import types.base.prototypes.RoomPosition
import types.base.prototypes.findEnergy
import types.base.prototypes.findStructures

object ConstructionUtils {

    fun constructExtension(room: Room): Boolean {
        if (room.isMyRoom() == true &&
                room.getRoomExtension().size < extensionControllerStructure[room.controller?.level] ?: 0 &&
                room.canConstructSite()) {
            val sitePos = findConstructionPlace(room, room.getSpawn()?.pos)
            if (sitePos != null) {
                room.createConstructionSite(sitePos, STRUCTURE_EXTENSION)
                return true
            }
        }
        return false
    }

    fun constructSourceContainer(room: Room): Boolean {
        if (room.isMyRoom() == true &&
                room.canConstructSite()) {

            room.findEnergy().forEach { source ->
                var hasContainer = false
                room.findStructures().filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
                    if (room.memory.getcontainerTarget(it.id) == source.id)
                        hasContainer = true
                }
                if (!hasContainer) {
                    val sitePos = findConstructionPlace(room, source.pos)
                    if (sitePos != null) {
                        room.createConstructionSite(sitePos, STRUCTURE_CONTAINER)
                        val flags = room.createFlag(sitePos) as? String
                        if (flags != null) {
                            Game.flags[flags]?.memory?.containerPriority = 0
                            Game.flags[flags]?.memory?.containerTarget = source.id
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    fun constructSpawnContainer(room: Room): Boolean {
        if (room.isMyRoom() == true &&
                room.canConstructSite()) {

            var hasContainer = false

            room.findStructures().filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
                if (room.memory.getcontainerTarget(it.id) == room.getSpawn()?.id)
                    hasContainer = true
            }
            if (!hasContainer) {
                val sitePos = findConstructionPlace(room, room.getSpawn()?.pos)
                if (sitePos != null) {
                    room.createConstructionSite(sitePos, STRUCTURE_CONTAINER)
                    val flags = room.createFlag(sitePos) as? String
                    if (flags != null) {
                        Game.flags[flags]?.memory?.containerPriority = 1
                        Game.flags[flags]?.memory?.containerTarget = room.getSpawn()?.id
                    }
                    return true
                }
            }
        }
        return false
    }

    private fun findConstructionPlace(room: Room, pos: RoomPosition?): RoomPosition? {
        if (pos == null)
            return null
        var x: Int
        var y: Int
        var i = 0
        while (i < 50) {
            x = pos.x - i
            if (x < 0)
                x = 0
            while (x < pos.x + i && x < 50) {
                y = pos.y - i
                if (y < 0)
                    y = 0
                while (y < pos.y + i && y < 50) {
                    if (canSpawnStructAt(room, x, y))
                        return RoomPosition(x, y, room.name)
                    y++
                }
                x++
            }
            i++
        }
        return null
    }

    private fun canSpawnStructAt(room: Room, x: Int, y: Int): Boolean {
        var tx = x - 1
        if (tx < 0)
            tx = 0
        while (tx <= x + 1 && tx < 50) {
            var ty = y - 1
            if (ty < 0)
                ty = 0
            while (ty <= y + 1 && ty < 50) {
                if (!isEmptyCase(room, tx, ty))
                    return false
                ty++
            }
            tx++
        }
        return true
    }

    private fun isEmptyCase(room: Room, x: Int, y: Int): Boolean {
        room.lookAt(x, y).forEach {
            console.log("terrain = " + it.terrain)
            console.log("cSite = " + it.constructionSite)
            console.log("struct = " + it.structure)
            if (it.terrain == "wall" || it.constructionSite != null || it.structure != null)
                return false
        }
        return true
    }

    private val extensionControllerStructure = mapOf(0 to 0, 1 to 0, 2 to 5, 3 to 10, 4 to 20, 5 to 30, 6 to 40, 7 to 50, 8 to 60)
}