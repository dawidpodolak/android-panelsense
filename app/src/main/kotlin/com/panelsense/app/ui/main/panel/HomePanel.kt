package com.panelsense.app.ui.main.panel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.item.PanelItemView
import com.panelsense.domain.model.Panel
import com.panelsense.domain.model.PanelItem

@Suppress("EmptyFunctionBlock")
@Composable
fun HomePanelView(
    modifier: Modifier = Modifier,
    homePanel: Panel.HomePanel,
    entityInteractor: EntityInteractor
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
            .applyBackground(homePanel.background)
    ) {

        val (bottomPanel, timePanel, weatherPanel) = createRefs()

        if (homePanel.itemLeft != null || homePanel.itemRight != null) {
            ButtonsPanelView(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .constrainAs(timePanel) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, homePanel, entityInteractor
            )
        }
    }
}

@Composable
private fun ButtonsPanelView(
    modifier: Modifier,
    homePanel: Panel.HomePanel,
    entityInteractor: EntityInteractor
) {
    Row(
        modifier = modifier,
    ) {
        if (homePanel.itemLeft != null) {
            PanelItemView(
                modifier = Modifier
                    .weight(0.3f),
                panelItem = homePanel.itemLeft!!, entityInteractor = entityInteractor
            )
        }

        if (homePanel.itemLeft != null && homePanel.itemRight != null) {
            Spacer(modifier = Modifier.requiredWidth(30.dp))
        }

        if (homePanel.itemRight != null) {
            PanelItemView(
                modifier = Modifier
                    .weight(0.3f),
                panelItem = homePanel.itemRight!!, entityInteractor = entityInteractor
            )
        }
    }
}

@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE, widthDp = 1920, heightDp = 1080)
@Composable
fun PreviewHome() {
    HomePanelView(
        homePanel = Panel.HomePanel(
            background = "#ffffff",
            itemLeft = PanelItem(
                id = "1",
                title = "Light",
                icon = "lightbulb",
                entity = "light.biuri",
            ),

            itemRight = PanelItem(
                id = "1",
                title = "Light",
                icon = "lightbulb",
                entity = "light.biuro",
            ),
        ),
        entityInteractor = mockEntityInteractor(LocalContext.current)
    )
}
