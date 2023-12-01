package com.panelsense.app.ui.main.panel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.item.PanelItemView
import com.panelsense.domain.model.Panel

@Suppress("TopLevelPropertyNaming")
const val DefaultButtonBackground = "#88cecece"
val GridPadding = 15.dp
val ButtonShape = RoundedCornerShape(15.dp)

@Composable
@ExperimentalFoundationApi
fun GridPanelView(
    modifier: Modifier = Modifier,
    panelConfiguration: Panel.GridPanel,
    entityInteractor: EntityInteractor
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(panelConfiguration.columnCount),
        contentPadding = PaddingValues(GridPadding),
    ) {

        items(panelConfiguration.itemList) { item ->
            PanelItemView(
                modifier = Modifier
                    .padding(GridPadding)
                    .requiredHeight(120.dp),
                panelItem = item,
                entityInteractor = entityInteractor
            )
        }
    }
}
