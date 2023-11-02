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
        val itemLeft: PanelItem? = null,
        val itemRight: PanelItem? = null,
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
}

enum class PanelType(val type: String) {
    HOME("home"),
    GRID("grid"),
}

data class PanelItem(
    val id: String? = null,
    val entity: String,
    val title: String? = null,
    val icon: String? = null
) {
    val domain: EntityDomain
        get() = EntityDomain.valueOf(entity.substringBefore(".").uppercase())
}
