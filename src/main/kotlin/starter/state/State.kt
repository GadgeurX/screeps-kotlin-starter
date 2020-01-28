package starter.state

abstract class State {
    open fun start() {}
    open fun stop() {}
    open fun update(): State? = null
}