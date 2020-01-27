package starter

import screeps.api.CARRY
import screeps.api.MOVE
import screeps.api.WORK

enum class Role {
    UNASSIGNED,
    HARVESTER,
    BUILDER,
    TRANSPORTER,
    SCOOT,
    UPGRADER;

    companion object {
        val minPopulations = mapOf(Role.HARVESTER to 2, Role.UPGRADER to 1, Role.BUILDER to 2, Role.TRANSPORTER to 2, SCOOT to 1)

        val partRatio = mapOf(Role.HARVESTER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.BUILDER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.UPGRADER to mapOf(MOVE to 0.5, WORK to 0.75, CARRY to 1.0),
                Role.SCOOT to mapOf(MOVE to 1.0),
                Role.TRANSPORTER to mapOf(MOVE to 0.5, CARRY to 1.0))

        val partCost = mapOf(WORK to 100, MOVE to 50, CARRY to 50)
    }
}