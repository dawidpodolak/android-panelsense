package com.panelsense.domain.model

data class Configuration(
    val system: ConfigurationSystem,
    val panelList: List<Panel>
)

data class ConfigurationSystem(
    val mainPanelId: String? = null,
    val showNavBar: Boolean = false,
    val background: String? = null
)


sealed class Panel {

    data class HomePanel(
        val id: String? = null,
        val type: PanelType = PanelType.HOME,
        val name: String? = null,
        val weatherEntity: String? = null,
        val time24h: Boolean = false,
        val itemList: List<PanelItem> = emptyList(),
        val background: String? = null
    ) : Panel()

    data class GridPanel(
        val id: String? = null,
        val type: PanelType = PanelType.GRID,
        val name: String? = null,
        val columnCount: Int = 0,
        val itemList: List<PanelItem> = emptyList(),
        val background: String? = null
    ) : Panel()

    data class FlexPanel(
        val id: String? = null,
        val type: PanelType = PanelType.FLEX,
        val name: String? = null,
        val columns: List<List<PanelItem>> = emptyList(),
        val rows: List<List<PanelItem>> = emptyList(),
        val background: String? = null
    ) : Panel()

    data class UnknownPanel(
        val type: PanelType = PanelType.UNKNOWN,
    ) : Panel()
}

enum class PanelType(val type: String) {
    HOME("home"),
    GRID("grid"),
    FLEX("flex"),
    UNKNOWN("unknown"),
}

data class PanelItem(
    val id: String? = null,
    val entity: String? = null,
    val type: String? = null,
    val title: String? = null,
    val icon: String? = null,
    val time24h: Boolean? = null, // Only for ItemPanelType.CLOCK
    val itemList: List<PanelItem>? = null, // Only for ItemPanelType.GRID
    val background: String? = null,
    val foreground: String? = null
)
