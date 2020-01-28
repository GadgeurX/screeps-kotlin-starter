package starter.state

import screeps.api.*
import starter.claimerId
import starter.targetRoom
import starter.targetRoute

class ClaimRoomState : State() {

    override fun update(): State? {
        val claimer = Game.getObjectById<Creep>(Memory.claimerId) ?: return CreateClaimerState()
        if (claimer.room.name == Memory.targetRoom) {
            Memory.targetRoute = listOf<Game.Map.RouteResult>().toTypedArray()
            if (claimer.pos.isNearTo(claimer.room.controller!!))
                claimer.claimController(claimer.room.controller!!)
            else
                claimer.moveTo(claimer.room.controller!!.pos)
        } else {
            if (Memory.targetRoute.isEmpty()) {
                Memory.targetRoute = Game.map.findRoute(claimer.room.name, Memory.targetRoom).value ?: return PrepareInvadeState()
            } else {
                val route = Memory.targetRoute.find { it.room == claimer.room.name }
                if (route == null) {
                    Memory.targetRoute = listOf<Game.Map.RouteResult>().toTypedArray()
                    return PrepareInvadeState()
                }
                claimer.moveTo(claimer.room.find(route.exit)[0])
            }
        }
        return super.update()
    }
}
