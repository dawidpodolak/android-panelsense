package com.nspanel.core.model

data class SenseConfiguration (
    val systemConfiguration: SystemConfiguration? = null,
    val panelList: List<PanelConfiguration>
)
