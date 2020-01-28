package starter.state

import screeps.api.Memory
import screeps.api.get
import starter.map
import starter.targetRoom

class PrepareInvadeState: State() {

    override fun update(): State? {
        var x = 0
        var y = 0
        var i = 0
        while (i < 20) {
            x = -i
            while (x <= i) {
                y = -i
                while (y <= i) {
                    val roomData = Memory.map[Pair(x, y)]
                    if (roomData != null && roomData.ownerPlayerId == null && roomData.nbSources > 1) {
                        Memory.targetRoom = roomData.roomId
                        return CreateClaimerState()
                    }
                    y++
                }
                x++
            }
            i++
        }
        return super.update()
    }
}