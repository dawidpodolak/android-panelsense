package com.panelsense.core.model.panelconfig

data class SenseConfiguration (
    val systemConfiguration: SystemConfiguration? = null,
    val panelList: List<PanelConfiguration>
)
