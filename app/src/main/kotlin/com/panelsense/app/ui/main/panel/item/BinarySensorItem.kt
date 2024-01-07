@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemOrientation.HORIZONTAL
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemOrientation.VERTICAL
import com.panelsense.app.ui.main.panel.StateLaunchEffect
import com.panelsense.app.ui.main.panel.applyBackgroundForItem
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.getPanelSizeHelper
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Companion.applySizeForRequestLayout
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Flex
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Grid
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.FontStyleH4
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.model.entity.state.BinarySensorEntityState

data class BinarySensorItemState(
    val icon: Drawable? = null,
    val title: String = "",
    val entityValue: String? = null,
    val entityState: BinarySensorEntityState? = null
)

@Composable
fun BinarySensorItemView(
    modifier: Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    initState: BinarySensorItemState = BinarySensorItemState(),
    layoutRequest: PanelItemLayoutRequest
) {
    var state by remember { mutableStateOf(initState) }
    val panelSizeHelper = getPanelSizeHelper()

    val onText = stringResource(id = R.string.on_short)
    val offText = stringResource(id = R.string.off_short)

    StateLaunchEffect<BinarySensorEntityState, BinarySensorItemState>(
        panelItem = panelItem,
        entityInteractor = entityInteractor,
        mapper = ::entityToBinarySensorItemState
    ) {
        state = it.copy(entityValue = if (it.entityState?.state == true) onText else offText)
    }

    Box(
        modifier = modifier
            .applySizeForRequestLayout(layoutRequest)
            .applyBackgroundForItem(panelItem, layoutRequest)
            .onGloballyPositioned(panelSizeHelper::onGlobalLayout)
    ) {
        when {
            layoutRequest is Grid -> VerticalBinarySensorItemView(
                modifier = Modifier.fillMaxSize(),
                state = state
            )

            panelSizeHelper.orientation == HORIZONTAL || layoutRequest is Flex -> HorizontalBinarySensorItemView(
                modifier = Modifier.fillMaxSize(),
                state = state
            )

            panelSizeHelper.orientation == VERTICAL -> VerticalBinarySensorItemView(
                modifier = Modifier.fillMaxSize(),
                state = state
            )
        }
    }
}

@Composable
fun HorizontalBinarySensorItemView(
    modifier: Modifier,
    state: BinarySensorItemState
) {
    ConstraintLayout(modifier = modifier) {
        val (title, icon, valueText) = createRefs()
        Image(
            modifier = Modifier.constrainAs(icon) {
                end.linkTo(valueText.start, margin = 2.dp)
                top.linkTo(valueText.top)
                bottom.linkTo(valueText.bottom)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            },
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )
        if (state.entityValue != null) {
            Text(
                modifier = Modifier.constrainAs(valueText) {
                    end.linkTo(parent.end, margin = 30.dp)
                    top.linkTo(parent.top, margin = 15.dp)
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                },
                text = state.entityValue,
                color = PanelItemTitleColor,
                style = FontStyleH3_SemiBold,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start, margin = 30.dp)
                top.linkTo(parent.top, margin = 15.dp)
                bottom.linkTo(parent.bottom, margin = 15.dp)
                end.linkTo(valueText.start, margin = 30.dp)
                width = Dimension.fillToConstraints
            },
            text = state.title,
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )
    }
}

@Composable
fun VerticalBinarySensorItemView(
    modifier: Modifier,
    state: BinarySensorItemState
) {
    ConstraintLayout(modifier = modifier) {
        val (title, icon, valueText) = createRefs()
        Image(
            modifier = Modifier.constrainAs(icon) {
                end.linkTo(parent.end)
                linkTo(parent.top, valueText.top, bottomMargin = 2.dp, bias = 1f)
                start.linkTo(parent.start)
            },
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )
        if (state.entityValue != null) {
            Text(
                modifier = Modifier.constrainAs(valueText) {
                    end.linkTo(parent.end, margin = 30.dp)
                    start.linkTo(parent.start, margin = 30.dp)
                    top.linkTo(parent.top, margin = 15.dp)
                    bottom.linkTo(parent.bottom, margin = 15.dp)
                },
                text = state.entityValue,
                color = PanelItemTitleColor,
                style = FontStyleH3_SemiBold,
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(valueText.start)
                top.linkTo(valueText.bottom, margin = 5.dp)
                bottom.linkTo(parent.bottom, margin = 5.dp)
                end.linkTo(valueText.end)
            },
            text = state.title,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = PanelItemTitleColor,
            overflow = TextOverflow.Ellipsis,
            style = FontStyleH4
        )
    }
}

private suspend fun entityToBinarySensorItemState(
    entityState: BinarySensorEntityState,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor
): BinarySensorItemState = BinarySensorItemState(
    icon = entityInteractor.getDrawable(
        mdiName = panelItem.icon ?: entityState.icon ?: entityState.mdiIcon,
        enable = entityState.state,
    ),
    title = panelItem.title ?: entityState.friendlyName ?: "",
    entityState = entityState,
)
