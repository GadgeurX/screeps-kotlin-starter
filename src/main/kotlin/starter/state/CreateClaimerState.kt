package starter.state

import screeps.api.*
import starter.claimerId
import starter.getSpawn
import starter.isMyRoom

class CreateClaimerState : State() {

    override fun update(): State? {

        val claimers = Game.creeps.entries.filter { it.component2().body.find { it.type == CLAIM } != null }
        if (claimers.isEmpty()) {
            val spawnRoom = Game.rooms.values.filter { it.isMyRoom() == true }.find { it.getSpawn()?.energy ?: 0 > 700 }
            spawnRoom?.getSpawn()?.spawnCreep(mutableListOf(CLAIM, MOVE).toTypedArray(), "Claimer")
        }
        else {
            Memory.claimerId = claimers[0].component1()
            return ClaimRoomState()
        }
        return super.update()
    }

}
