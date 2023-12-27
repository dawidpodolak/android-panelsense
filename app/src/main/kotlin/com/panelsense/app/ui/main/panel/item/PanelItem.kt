package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.toDomain

@Composable
fun PanelItemView(
    modifier: Modifier = Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    layoutRequest: PanelItemLayoutRequest = PanelItemLayoutRequest.Standard
) {
    when (panelItem.getItemViewType()) {
        PanelItemViewType.LIGHT,
        PanelItemViewType.SIMPLE -> SimplePanelItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = layoutRequest
        )

        PanelItemViewType.COVER -> CoverItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = layoutRequest
        )

        PanelItemViewType.NONE -> UnknownPanelItem(
            modifier,
            panelItem = panelItem
        )
    }
}

enum class PanelItemViewType {
    COVER,
    SIMPLE,
    LIGHT,
    NONE
}

fun PanelItem.getItemViewType(): PanelItemViewType = when (this.entity.toDomain) {
    EntityDomain.COVER -> PanelItemViewType.COVER
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

interface PanelItemLayoutRequest {
    object Standard : PanelItemLayoutRequest
    object Flex : PanelItemLayoutRequest {
        val SimplePanelHeight = 100.dp
        val CoverPanelHeight = 100.dp
    }

    companion object {
        fun Modifier.applySizeIfFlex(layoutRequest: PanelItemLayoutRequest, height: Dp): Modifier =
            when (layoutRequest) {
                is Flex -> this
                    .fillMaxWidth()
                    .requiredHeight(height)

                else -> this
            }
    }
}
