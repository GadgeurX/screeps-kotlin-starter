package starter

import types.base.global.CARRY
import types.base.global.MOVE
import types.base.global.WORK

enum class Role {
    UNASSIGNED,
    HARVESTER,
    BUILDER,
    TRANSPORTER,
    UPGRADER;

    companion object {
        val minPopulations = mapOf(Role.HARVESTER to 1, Role.UPGRADER to 1, Role.BUILDER to 2, Role.TRANSPORTER to 2)

        val partRatio = mapOf(Role.HARVESTER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.BUILDER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.UPGRADER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.TRANSPORTER to mapOf(MOVE to 0.5, CARRY to 1.0))

        val partCost = mapOf(WORK to 100, MOVE to 50, CARRY to 50)
    }
}