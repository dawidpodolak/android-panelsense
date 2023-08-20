package com.nspanel.core.model.panelconfig

import com.nspanel.core.model.mqqt.PanelMqttTopic
import com.nspanel.core.model.state.PanelState

sealed class PanelConfiguration(open val id: String) {
    data class GridPanel(
        override val id: String,
        val columnCount: Int,
        val gridItems: List<PanelItem>
    ) : PanelConfiguration(id)

    data class HomePanel(
        override val id: String,
        val homeItems: List<PanelItem>
    ) : PanelConfiguration(id)

    sealed class PanelItem(
        open val id: String,
        open val mqttTopic: PanelMqttTopic? = null,
        open val state: PanelState
    ) {
        data class ButtonItem(
            override val id: String,
            val text: String,
            val textColor: String = "#ffffff",
            val icon: String,
            val backgroundColor: String?,
            override val mqttTopic: PanelMqttTopic? = null,
            override val state: PanelState.ButtonState = PanelState.ButtonState.Default
        ): PanelItem(id, mqttTopic, state)
    }
}
