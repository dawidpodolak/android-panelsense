@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.ButtonShape
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemOrientation
import com.panelsense.app.ui.main.panel.custom.CircleColorPickerView
import com.panelsense.app.ui.main.panel.custom.KelvinVerticalSlider
import com.panelsense.app.ui.main.panel.custom.SliderMotion
import com.panelsense.app.ui.main.panel.custom.VerticalSlider
import com.panelsense.app.ui.main.panel.effectClickable
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.getPanelSizeHelper
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.app.ui.theme.FontStyleH2_SemiBold
import com.panelsense.app.ui.theme.FontStyleH3_Regular
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.MdiIcons
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.app.ui.theme.PanelSenseBottomSheet
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import com.panelsense.domain.model.entity.state.LightEntityState
import com.panelsense.domain.model.entity.state.SwitchEntityState
import com.panelsense.domain.toDomain
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.sample
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
    val panelSizeHelper = getPanelSizeHelper()
    val showBottomSheet = remember { mutableStateOf(false) }

    if (state.entityState is LightEntityState && (state.entityState as LightEntityState).hasFeatures) {
        PanelSenseBottomSheet(showBottomSheet, state.title) {
            LightControlView(
                Modifier.align(Alignment.CenterHorizontally),
                lightEntityState = state.entityState as LightEntityState,
                entityInteractor = entityInteractor
            )
        }
    }

    Box(
        modifier = modifier
            .effectClickable(
                hapticFeedback = LocalHapticFeedback.current,
                viewSoundEffect = LocalView.current,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onLongClick = {
                    if (state.entityState is LightEntityState && (state.entityState as LightEntityState).on) {
                        showBottomSheet.value = true
                    }
                },
                onClick = {
                    state.toggleCommand?.let(entityInteractor::sendCommand)
                },
            )
            .background(
                color = PanelItemBackgroundColor,
                shape = ButtonShape
            )
            .onGloballyPositioned(panelSizeHelper::onGlobalLayout)
    ) {

        StateLaunchEffect(panelItem = panelItem, entityInteractor = entityInteractor) {
            state = it
        }

        when (panelSizeHelper.orientation) {
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
        icon = entityInteractor.getDrawable(
            panelItem.icon ?: icon ?: MdiIcons.LIGHT_BULB,
            on,
            color
        ),
        title = panelItem.title ?: friendlyName ?: entityId,
        entityState = this
    )
}

private suspend fun SwitchEntityState.toState(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor
): SimplePanelItemState {
    return SimplePanelItemState(
        icon = entityInteractor.getDrawable(panelItem.icon ?: icon ?: MdiIcons.LIGHT_BULB, on),
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
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = state.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold,
        )

        if (state.entityState is LightEntityState && state.entityState.brightness != null) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = state.entityState.brightness?.getBrightnessPercent()
                    .toString()
                    .plus("%"),
                color = PanelItemTitleColor,
                style = FontStyleH3_Regular
            )
        }
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

        val (image, text, brightness) = createRefs()
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
            style = FontStyleH3_SemiBold,
        )

        if (state.entityState is LightEntityState && state.entityState.brightness != null) {
            Text(
                modifier = Modifier
                    .constrainAs(brightness) {
                        start.linkTo(text.end, margin = 5.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 5.dp)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                text = state.entityState.brightness?.getBrightnessPercent()
                    .toString()
                    .plus("%"),
                color = PanelItemTitleColor,
                style = FontStyleH3_Regular
            )
        }
    }
}

@Composable
fun LightControlView(
    modifier: Modifier = Modifier,
    lightEntityState: LightEntityState,
    entityInteractor: EntityInteractor
) {
    var entityState by remember { mutableStateOf(lightEntityState) }
    var brightness by remember { mutableStateOf<Int?>(entityState.brightness ?: 0) }
    val coroutineScope = rememberCoroutineScope()
    val colorFlow = remember { MutableSharedFlow<Color>(replay = 1) }

    LaunchedEffect(key1 = lightEntityState.entityId) {
        entityInteractor.listenOnState(lightEntityState.entityId, LightEntityState::class)
            .collect {
                entityState = it
                brightness = it.brightness
                colorFlow.emit(it.rgbColor?.toColor() ?: Color.White)
            }
    }
    LaunchedEffect(key1 = entityState.entityId) {
        colorFlow
            .sample(500)
            .collect {
                if (it == entityState.rgbColor?.toColor()) return@collect
                entityInteractor.sendCommand(
                    entityState.getRGBCommand(
                        (255 * it.red).toInt(),
                        (255 * it.green).toInt(),
                        (255 * it.blue).toInt()
                    )
                )
            }
    }

    val brightnessText: String = when {
        entityState.on && brightness != null -> brightness.getBrightnessPercent().toString()
            .plus("%")

        entityState.on && brightness == null -> ""
        else -> stringResource(id = R.string.off)
    }
    Text(
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp),
        text = brightnessText,
        color = Color.White,
        style = FontStyleH2_SemiBold
    )
    Row(
        modifier = modifier
            .fillMaxSize(0.8f),
        horizontalArrangement = Arrangement.Center,
    ) {
        VerticalSlider(
            Modifier.fillMaxHeight(0.7f),
            initOn = lightEntityState.on,
            initValue = entityState.brightness?.toFloat() ?: 0f,
            maxValue = 255f,
            colorFlow = colorFlow,
            onUpdateValue = { value, motion ->
                brightness = value.toInt()
                if (motion == SliderMotion.UP) {
                    entityInteractor.sendCommand(entityState.getBrightnessCommand(value.toInt()))
                }
            }
        )

        Spacer(modifier = Modifier.width(50.dp))
        CircleColorPickerView(Modifier.fillMaxHeight(0.7f), lightEntityState) {
            coroutineScope.launch {
                colorFlow.emit(it)
            }
        }

        if (entityState.colorTempKelvinRange != null) {

            Spacer(modifier = Modifier.width(50.dp))
            KelvinVerticalSlider(
                Modifier.fillMaxHeight(0.7f),
                maxValue = entityState.colorTempKelvinRange!!.max,
                minValue = entityState.colorTempKelvinRange!!.min,
                initValue = entityState.colorTempKelvin
            ) {
                entityInteractor.sendCommand(entityState.getTempCommand(it))
            }
        }
    }
}

fun Int?.getBrightnessPercent(): Int? =
    this?.let { (it / 255f * 100f).toInt() }

@Preview(widthDp = 1900, heightDp = 1200, showBackground = true)
@Composable
fun LightControlPreview() {
    Box(

    ) {
        LightControlView(
            Modifier.align(Alignment.Center),
            lightEntityState = LightEntityState(
                entityId = "someEntity",
                brightness = 240,
                on = true,
                colorMode = null,
                rgbColor = null,
                rgbwwColor = null,
                colorTempKelvin = null,
                colorTempKelvinRange = null,
                supportedColorModes = emptyList(),
                friendlyName = "Lampa",
                icon = "lamp",
                supportedFeatures = emptySet()
            ),
            mockEntityInteractor(LocalContext.current)
        )
    }
}

private fun LightEntityState.Color.toColor(): Color =
    Color(red, green, blue)

@Preview
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
