package com.nspanel.core.model.state

sealed class PanelState {

    data class ButtonState(val state: State): PanelState() {
        val enabled: Boolean
            get() = state == State.ON
        enum class State {
            ON, OFF
        }

        companion object {
            val Default = ButtonState(State.OFF)
        }
    }

    companion object {
        inline fun <reified T: PanelState> getDefault(): PanelState =
            when (T::class) {
                ButtonState::class -> ButtonState.Default
                else -> throw IllegalArgumentException("Unknown PanelState type: ${T::class}")
            }
    }
}
