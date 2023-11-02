package com.panelsense.app.ui.main.panel.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.PanelItem

@Composable
fun PanelItemView(
    modifier: Modifier = Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor
) {
    when (panelItem.getItemViewType()) {
        PanelItemViewType.LIGHT,
        PanelItemViewType.SIMPLE -> SimplePanelItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor
        )

        PanelItemViewType.NONE -> Unit
    }
}

enum class PanelItemViewType {
    SIMPLE,
    LIGHT,
    NONE
}

fun PanelItem.getItemViewType(): PanelItemViewType = when (this.domain) {
    EntityDomain.LIGHT -> PanelItemViewType.SIMPLE
    EntityDomain.SWITCH -> PanelItemViewType.SIMPLE
    else -> PanelItemViewType.NONE
}

@Preview
@Composable
fun PanelItemPreview() {
    PanelItemView(
        Modifier,
        panelItem = PanelItem(entity = "light.test"),
        mockEntityInteractor(LocalContext.current)
    )
}