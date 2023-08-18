package com.nspanel.core.model

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

    sealed class PanelItem(open val id: String) {
        data class ButtonGridItem(
            override val id: String,
            val text: String,
            val icon: String,
            val backgroundColor: String?,
            val entity: String?
        ): PanelItem(id)
    }
}
