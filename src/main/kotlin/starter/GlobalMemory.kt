package starter

import screeps.api.Memory
import screeps.api.MutableRecord
import screeps.utils.memory.memory
import screeps.utils.mutableRecordOf


var Memory.map: MutableRecord<Vector, RoomData> by memory { mutableRecordOf<Vector, RoomData>() }
