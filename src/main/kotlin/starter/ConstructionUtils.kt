package starter

import screeps.api.*

object ConstructionUtils {

    val containerList = mutableListOf<String>()

    fun constructRoadArroundContainer(room: Room): Boolean {
        var construct = false
        if (room.isMyRoom() == true &&
                room.canConstructSite()) {

            room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.forEach { container_start ->
                if (!containerList.contains(container_start.id)) {
                    containerList.add(container_start.id)
                    room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.forEach { container_finish ->
                        if (!containerList.contains(container_finish.id)) {
                            val path = room.findPath(container_start.pos, container_finish.pos)

                            path.forEach { step ->
                                if (isEmptyCaseRoad(room, step.x, step.y)) {
                                    room.createConstructionSite(RoomPosition(step.x, step.y, room.name), STRUCTURE_ROAD)
                                    construct = true
                                }
                            }
                        }
                    }
                }
            }
        }
        return construct
    }

    fun constructRoadArroundStruct(room: Room): Boolean {
        if (room.isMyRoom() == true &&
                room.canConstructSite()) {


            room.find(FIND_STRUCTURES).filter { it.structureType != STRUCTURE_WALL && it.structureType != STRUCTURE_ROAD }.forEach { structure ->

                var tx = structure.pos.x - 1
                if (tx < 0)
                    tx = 0
                while (tx <= structure.pos.x + 1 && tx < 50) {
                    var ty = structure.pos.y - 1
                    if (ty < 0)
                        ty = 0
                    while (ty <= structure.pos.y + 1 && ty < 50) {
                        if (isEmptyCaseRoad(room, tx, ty)) {
                            room.createConstructionSite(RoomPosition(tx, ty, room.name), STRUCTURE_ROAD)
                            return true
                        }
                        ty++
                    }
                    tx++
                }

            }
        }
        return false
    }

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
            console.log("Can construct")
            room.find(FIND_SOURCES).forEach { source ->
                var hasContainer = false
                room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
                    console.log("find struct to construct")
                    if (room.memory.getContainerTarget(it.id) == source.id)
                        hasContainer = true
                }
                if (!hasContainer) {
                    console.log("no container build it")
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
            console.log("Can construct")
            var hasContainer = false

            room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.forEach {
                console.log("find struct to construct")
                if (room.memory.getContainerTarget(it.id) == room.getSpawn()?.id)
                    hasContainer = true
            }
            if (!hasContainer) {
                val sitePos = findConstructionPlace(room, room.getSpawn()?.pos)
                if (sitePos != null) {
                    room.createConstructionSite(sitePos, STRUCTURE_CONTAINER)
                    val flags = room.createFlag(sitePos) as? String
                    if (flags != null) {
                        Game.flags[flags]?.memory?.containerPriority = 1
                        Game.flags[flags]?.memory?.containerTarget = room.getSpawn()?.id ?: ""
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
            while (x <= pos.x + i && x < 50) {
                y = pos.y - i
                if (y < 0)
                    y = 0
                while (y <= pos.y + i && y < 50) {
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
            if (it.terrain == TERRAIN_WALL || it.constructionSite != null || (it.structure != null && it.structure?.structureType != STRUCTURE_ROAD))
                return false
        }
        return true
    }

    private fun isEmptyCaseRoad(room: Room, x: Int, y: Int): Boolean {
        room.lookAt(x, y).forEach {
            console.log("terrain = " + it.terrain)
            console.log("cSite = " + it.constructionSite)
            console.log("struct = " + it.structure)
            if (it.terrain == TERRAIN_WALL || it.constructionSite != null || it.structure != null)
                return false
        }
        return true
    }

    private val extensionControllerStructure = mapOf(0 to 0, 1 to 0, 2 to 5, 3 to 10, 4 to 20, 5 to 30, 6 to 40, 7 to 50, 8 to 60)
}