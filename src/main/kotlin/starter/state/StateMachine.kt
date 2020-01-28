package starter.state

import screeps.api.Memory
import starter.currentStateName
import starter.nextStateName

class StateMachine(initState: State) {

    private val states: MutableList<State> = mutableListOf(InitState(),
            PrepareInvadeState(),
            CreateClaimerState(),
            ClaimRoomState()o)

    init {
        if (Memory.currentStateName.isBlank())
            Memory.currentStateName = initState::class.simpleName ?: ""
    }

    fun update() {
        val currentState = states.find { it::class.simpleName == Memory.currentStateName }
        val nextState = states.find { it::class.simpleName == Memory.nextStateName }
        if (nextState != null) {
            currentState?.stop()
            nextState.start()
            Memory.currentStateName = nextState::class.simpleName ?: ""
        } else {
            val returnedState = currentState?.update()
            if (returnedState != null)
                Memory.nextStateName = returnedState::class.simpleName ?: ""
        }
    }
}