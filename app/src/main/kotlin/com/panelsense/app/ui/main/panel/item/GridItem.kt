package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.applyBackgroundForItem
import com.panelsense.app.ui.main.panel.scrollReset
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.domain.model.PanelItem


@Composable
fun GridItemView(
    modifier: Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    layoutRequest: PanelItemLayoutRequest
) {
    if (layoutRequest is PanelItemLayoutRequest.Flex) {
        FixedGridItemView(modifier, panelItem, layoutRequest, entityInteractor)
    } else {
        DynamicGridItemView(modifier, panelItem, layoutRequest, entityInteractor)
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FixedGridItemView(
    modifier: Modifier,
    panelItem: PanelItem,
    layoutRequest: PanelItemLayoutRequest,
    entityInteractor: EntityInteractor
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .applyBackgroundForItem(panelItem, layoutRequest)
            .padding(8.dp)
    ) {
        if (panelItem.title?.isNotEmpty() == true) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                color = PanelItemTitleColor,
                text = panelItem.title ?: "", style = FontStyleH3_SemiBold
            )
        }

        FlowRow(
            modifier = modifier
                .fillMaxSize()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            val itemModifier = Modifier
                .padding(4.dp)
            panelItem.itemList?.forEach { panelItem ->
                BindItem(panelItem, itemModifier, entityInteractor)
            }
        }
    }
}

@Composable
private fun DynamicGridItemView(
    modifier: Modifier,
    panelItem: PanelItem,
    layoutRequest: PanelItemLayoutRequest,
    entityInteractor: EntityInteractor
) {
    val state: LazyGridState = rememberLazyGridState()
    val rowScrollConnection = scrollReset(state)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(PanelItemLayoutRequest.Grid.ItemWidth),
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .applyBackgroundForItem(panelItem, layoutRequest)
            .nestedScroll(rowScrollConnection),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(panelItem.itemList ?: emptyList()) { panelItem ->
            BindItem(panelItem, Modifier, entityInteractor)
        }
    }
}

@Composable
private fun BindItem(
    panelItem: PanelItem,
    modifier: Modifier,
    entityInteractor: EntityInteractor
) {
    when (panelItem.getItemViewType()) {
        PanelItemViewType.LIGHT,
        PanelItemViewType.SIMPLE -> SimplePanelItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = PanelItemLayoutRequest.Grid
        )

        PanelItemViewType.COVER -> CoverItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = PanelItemLayoutRequest.Grid
        )

        PanelItemViewType.SENSOR -> SensorItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = PanelItemLayoutRequest.Grid
        )

        PanelItemViewType.BINARY_SENSOR -> BinarySensorItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = PanelItemLayoutRequest.Grid
        )

        else -> UnknownPanelItem(
            modifier = modifier,
            panelItem = panelItem,
            layoutRequest = PanelItemLayoutRequest.Grid
        )
    }
}
