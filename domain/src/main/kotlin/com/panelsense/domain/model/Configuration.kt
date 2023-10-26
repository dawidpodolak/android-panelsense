package com.panelsense.domain.model

data class Configuration(
    val system: ConfigurationSystem,
    val panelList: List<Panel>
)

data class ConfigurationSystem(
    val mainPanelId: String? = null
)

data class Panel(
    val id: String? = null,
    val type: PanelType,
    val columnCount: Int = 0,
    val name: String? = null,
    val itemList: List<PanelItem> = emptyList()
)

enum class PanelType(val type: String) {
    HOME("home"),
    GRID("grid"),
}

data class PanelItem(
    val id: String? = null,
    val entity: String,
    val title: String? = null,
    val icon: String? = null
)
