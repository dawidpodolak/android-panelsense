@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.ButtonShape
import com.panelsense.app.ui.main.panel.GridPadding
import com.panelsense.app.ui.main.panel.PanelItemOrientation
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.getOrientationHelper
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.MdiIcons
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import com.panelsense.domain.model.entity.state.LightEntityState
import com.panelsense.domain.model.entity.state.SwitchEntityState
import com.panelsense.domain.toDomain
import kotlinx.coroutines.launch

data class SimplePanelItemState(
    val icon: Drawable? = null,
    val title: String = "",
    val entityState: EntityState? = null
) {
    val toggleCommand: EntityCommand?
        get() = when (entityState) {
            is LightEntityState -> entityState.getToggleCommand()
            is SwitchEntityState -> entityState.getToggleCommand()
            else -> null
        }
}

@Composable
fun SimplePanelItemView(
    modifier: Modifier = Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    initState: SimplePanelItemState = SimplePanelItemState()
) {

    var state by remember { mutableStateOf(initState) }
    val orientationHelper = getOrientationHelper()

    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = {
                    state.toggleCommand?.let(entityInteractor::sendCommand)
                },
            )
            .background(
                color = PanelItemBackgroundColor,
                shape = ButtonShape
            )
            .padding(GridPadding)
            .onSizeChanged(orientationHelper::onSizeChanged)
    ) {

        StateLaunchEffect(panelItem = panelItem, entityInteractor = entityInteractor) {
            state = it
        }

        when (orientationHelper.orientation) {
            PanelItemOrientation.HORIZONTAL -> HorizontalSimplePanelItemView(state = state)
            PanelItemOrientation.VERTICAL -> VerticalSimplePanelItemView(state = state)
        }
    }
}

@Composable
private fun StateLaunchEffect(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    callback: (SimplePanelItemState) -> Unit
) =
    LaunchedEffect(key1 = panelItem) {
        launch {
            if (panelItem.entity.toDomain == EntityDomain.LIGHT) {
                updateState<LightEntityState>(panelItem, entityInteractor) {
                    callback(it.toState(panelItem, entityInteractor))
                }
            } else if (panelItem.entity.toDomain == EntityDomain.SWITCH) {
                updateState<SwitchEntityState>(panelItem, entityInteractor) {
                    callback(it.toState(panelItem, entityInteractor))
                }
            }
        }
    }

private suspend inline fun <reified ENTITY_STATE : EntityState> updateState(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    noinline callback: suspend (ENTITY_STATE) -> Unit
) {
    entityInteractor.listenOnState(panelItem.entity, ENTITY_STATE::class)
        .collect { entityState ->
            callback(entityState)
        }
}

private suspend fun LightEntityState.toState(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor
): SimplePanelItemState {
    val color = rgbColor?.run { Color(red, green, blue) }
    return SimplePanelItemState(
        icon = entityInteractor.getDrawable(icon ?: MdiIcons.LIGHT_BULB, on, color),
        title = panelItem.title ?: friendlyName ?: entityId,
        entityState = this
    )
}

private suspend fun SwitchEntityState.toState(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor
): SimplePanelItemState {
    return SimplePanelItemState(
        icon = entityInteractor.getDrawable(icon ?: MdiIcons.LIGHT_BULB, on),
        title = panelItem.title ?: friendlyName ?: entityId,
        entityState = this
    )
}

@Composable
fun VerticalSimplePanelItemView(
    state: SimplePanelItemState
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            alignment = Alignment.Center,
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            text = state.title,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )
    }
}

@Composable
fun HorizontalSimplePanelItemView(
    state: SimplePanelItemState
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
    ) {


        val (image, text) = createRefs()
        Image(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .aspectRatio(1f)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(text.start)
                    start.linkTo(parent.start)
                },
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )
        Text(
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            text = state.title,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )
    }
}

@Preview(widthDp = 390, heightDp = 200)
@Composable
fun SimplePanelItemPreview() {

    SimplePanelItemView(
        Modifier,
        panelItem = PanelItem(entity = "light.test"),
        mockEntityInteractor(LocalContext.current),
        initState = SimplePanelItemState(
            icon = LocalContext.current.getDrawable(R.drawable.ic_launcher_background),
            title = "Test"
        )
    )
}
