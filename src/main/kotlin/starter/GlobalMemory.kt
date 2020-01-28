package starter

import screeps.api.*
import screeps.utils.memory.memory
import screeps.utils.mutableRecordOf


var Memory.map: MutableRecord<Pair<Int, Int>, RoomData> by memory { mutableRecordOf<Pair<Int, Int>, RoomData>() }
var Memory.currentStateName: String by memory { "" }
var Memory.nextStateName: String by memory { "" }

var Memory.targetRoom: String by memory { "" }
var Memory.claimerId: String by memory { "" }
var Memory.targetRoute: Array<Game.Map.RouteResult> by memory { listOf<Game.Map.RouteResult>().toTypedArray() }