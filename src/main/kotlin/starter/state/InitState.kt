package starter.state

import screeps.api.FIND_STRUCTURES
import screeps.api.Game
import screeps.api.STRUCTURE_TOWER
import screeps.api.values
import starter.isMyRoom

class InitState: State() {

    override fun update(): State? {
        Game.rooms.values.filter { it.isMyRoom() ?: false }.forEach {room ->
            if (room.find(FIND_STRUCTURES).any { it.structureType == STRUCTURE_TOWER })
                return PrepareInvadeState()
        }
        return super.update()
    }
}