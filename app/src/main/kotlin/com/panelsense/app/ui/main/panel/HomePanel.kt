package com.panelsense.app.ui.main.panel

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.item.ClockItemView
import com.panelsense.app.ui.main.panel.item.PanelItemView
import com.panelsense.app.ui.main.panel.item.WeatherItemView
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
            .applyBackground(homePanel.background)
            .padding(30.dp)
    ) {

        var bottomPanel: ConstrainedLayoutReference? = null
        val (timePanel, weatherPanel) = createRefs()
        ClockItemView(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .constrainAs(timePanel) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                },
            homePanel = homePanel
        )

        if (homePanel.itemList.isNotEmpty()) {
            bottomPanel = createRef()
            ButtonsPanelView(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .constrainAs(bottomPanel) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }, homePanel, entityInteractor
            )
        }

        if (homePanel.weatherEntity != null) {
            WeatherItemView(
                modifier = Modifier
                    .constrainAs(weatherPanel) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(timePanel.start, margin = 30.dp)
                        if (bottomPanel != null) {
                            bottom.linkTo(bottomPanel.top, margin = 30.dp)
                        } else {
                            bottom.linkTo(parent.bottom, margin = 30.dp)
                        }
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                        centerHorizontallyTo(parent, 0f)
                    },
                homePanel = homePanel,
                entityInteractor = entityInteractor
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
        val weight = 1 / homePanel.itemList.size.toFloat()
        homePanel.itemList.forEachIndexed { index, item ->
            PanelItemView(
                modifier = Modifier
                    .weight(weight),
                panelItem = item, entityInteractor = entityInteractor
            )

            if ((index >= 0 && homePanel.itemList.size > 1) && index < homePanel.itemList.size - 1) {
                Spacer(modifier = Modifier.requiredWidth(30.dp))
            }
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
