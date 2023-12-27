@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.ButtonShape
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemOrientation.HORIZONTAL
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemOrientation.VERTICAL
import com.panelsense.app.ui.main.panel.custom.SliderMotion
import com.panelsense.app.ui.main.panel.custom.VerticalSlider
import com.panelsense.app.ui.main.panel.effectClickable
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.getPanelSizeHelper
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Companion.applySizeIfFlex
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Flex
import com.panelsense.app.ui.theme.CoverItemButtonActive
import com.panelsense.app.ui.theme.FontStyleH2_SemiBold
import com.panelsense.app.ui.theme.FontStyleH3_Regular
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.MdiIcons
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.app.ui.theme.PanelSenseBottomSheet
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.model.entity.state.CoverEntityState
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.AWNING
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.BLIND
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.CURTAIN
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.DAMPER
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.DOOR
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.GARAGE
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.GATE
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.SHADE
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.SHUTTER
import com.panelsense.domain.model.entity.state.CoverEntityState.DeviceClass.WINDOW
import com.panelsense.domain.model.entity.state.CoverEntityState.Feature.SET_POSITION
import com.panelsense.domain.model.entity.state.CoverEntityState.Feature.SET_TILT_POSITION
import com.panelsense.domain.model.entity.state.CoverEntityState.State.OPEN
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

data class CoverItemState(
    val icon: Drawable? = null,
    val title: String = "",
    val position: String? = null,
    val entityState: CoverEntityState? = null
)

@Composable
fun CoverItemView(
    modifier: Modifier = Modifier,
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    initState: CoverItemState = CoverItemState(),
    layoutRequest: PanelItemLayoutRequest
) {
    var state by remember { mutableStateOf(initState) }
    val panelSizeHelper = getPanelSizeHelper()

    StateLaunchEffect(panelItem = panelItem, entityInteractor = entityInteractor) {
        state = it
    }
    Box(
        modifier = modifier
            .applySizeIfFlex(layoutRequest, Flex.CoverPanelHeight)
            .background(
                color = PanelItemBackgroundColor,
                shape = ButtonShape
            )
            .onGloballyPositioned(panelSizeHelper::onGlobalLayout)
    ) {
        when {
            panelSizeHelper.orientation == HORIZONTAL || layoutRequest is Flex -> HorizontalCoverItemView(
                entityInteractor = entityInteractor,
                state = state
            )

            panelSizeHelper.orientation == VERTICAL -> VerticalCoverItemView(
                entityInteractor = entityInteractor,
                state = state
            )
        }
    }
}

@Composable
fun HorizontalCoverItemView(
    modifier: Modifier = Modifier, entityInteractor: EntityInteractor, state: CoverItemState
) {
    ConstraintLayout(
        modifier.fillMaxSize()
    ) {
        val (image, title, position) = createRefs()
        val showBottomSheet = remember { mutableStateOf(false) }

        state.entityState?.let {
            CoverControlButtons(
                modifier = Modifier
                    .fillMaxSize(),
                state, entityInteractor, it,
                onLongClick = { showBottomSheet.value = true },
            )
        }

        if (state.entityState?.supportedFeatures?.contains(SET_POSITION) == true) {
            PanelSenseBottomSheet(showBottomSheet, state.title) {
                CoverControlView(
                    Modifier.align(Alignment.CenterHorizontally),
                    entityState = state.entityState!!,
                    entityInteractor = entityInteractor
                )
            }
        }

        Image(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .aspectRatio(1f)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(title.start)
                    start.linkTo(parent.start)
                },
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )

        Text(
            modifier = modifier
                .constrainAs(title) {
                    start.linkTo(parent.start, margin = 70.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 70.dp)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                },
            text = state.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )

        val supportedFeatures = state.entityState?.supportedFeatures ?: emptySet()
        if (state.position != null && (SET_POSITION in supportedFeatures || SET_TILT_POSITION in supportedFeatures)) {
            Text(
                modifier = modifier
                    .constrainAs(position) {
                        start.linkTo(title.end, margin = 5.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 5.dp)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                text = state.position,
                color = PanelItemTitleColor,
                style = FontStyleH3_Regular
            )
        }
    }
}

@Composable
fun VerticalCoverItemView(
    modifier: Modifier = Modifier, entityInteractor: EntityInteractor, state: CoverItemState
) {
    ConstraintLayout(
        modifier.fillMaxSize()
    ) {
        val (image, title, position) = createRefs()
        val showBottomSheet = remember { mutableStateOf(false) }

        state.entityState?.let {
            CoverControlButtons(
                modifier = Modifier
                    .fillMaxSize(),
                state, entityInteractor, it,
                useHorizontalLayout = false,
                onLongClick = { showBottomSheet.value = true },
            )
        }

        if (state.entityState?.supportedFeatures?.contains(SET_POSITION) == true) {
            PanelSenseBottomSheet(showBottomSheet, state.title) {
                CoverControlView(
                    Modifier.align(Alignment.CenterHorizontally),
                    entityState = state.entityState!!,
                    entityInteractor = entityInteractor
                )
            }
        }

        Image(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .aspectRatio(1f)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 5.dp)
                    bottom.linkTo(title.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
            painter = rememberDrawablePainter(drawable = state.icon),
            contentDescription = state.title
        )

        Text(
            modifier = modifier
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                },
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = state.title,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )

        val supportedFeatures = state.entityState?.supportedFeatures ?: emptySet()
        if (state.position != null && (SET_POSITION in supportedFeatures || SET_TILT_POSITION in supportedFeatures)) {
            Text(
                modifier = modifier
                    .constrainAs(position) {
                        start.linkTo(parent.start)
                        top.linkTo(title.bottom)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(parent.end)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    },
                text = state.position,
                color = PanelItemTitleColor,
                style = FontStyleH3_Regular
            )
        }
    }
}

@Composable
private fun CoverControlButtons(
    modifier: Modifier = Modifier,
    state: CoverItemState,
    entityInteractor: EntityInteractor,
    coverEntityState: CoverEntityState,
    useHorizontalLayout: Boolean = true,
    onLongClick: () -> Unit = {}
) {
    var openButton by remember { mutableStateOf<Drawable?>(null) }
    var stopButton by remember { mutableStateOf<Drawable?>(null) }
    var closeButton by remember { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(key1 = state.entityState?.entityId) {
        openButton = entityInteractor.getDrawable(MdiIcons.ARROW_UP_BOLD, CoverItemButtonActive)
        stopButton = entityInteractor.getDrawable(MdiIcons.STOP, CoverItemButtonActive)
        closeButton =
            entityInteractor.getDrawable(MdiIcons.ARROW_DOWN_BOLD, CoverItemButtonActive)
    }

    if (useHorizontalLayout) {
        Row {
            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = openButton,
                useDivider = true,
                onLongClick = { onLongClick() },
                onClick = { entityInteractor.sendCommand(coverEntityState.getOpenCoverCommand()) }
            )

            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = stopButton,
                useDivider = true,
                onLongClick = { onLongClick() },
                onClick = { entityInteractor.sendCommand(coverEntityState.getStopCoverCommand()) }
            )

            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = closeButton,
                onLongClick = { onLongClick() },
                onClick = { entityInteractor.sendCommand(coverEntityState.getCloseCoverCommand()) }
            )
        }
    } else {
        Column {
            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = openButton,
                useDivider = true,
                useHorizontalLayout = false,
                onLongClick = { onLongClick() },
                imageAlignment = Alignment.TopCenter,
                onClick = { entityInteractor.sendCommand(coverEntityState.getOpenCoverCommand()) }
            )

            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = stopButton,
                showIcon = false,
                useDivider = true,
                useHorizontalLayout = false,
                onLongClick = { onLongClick() },
                onClick = { entityInteractor.sendCommand(coverEntityState.getStopCoverCommand()) }
            )

            CoverButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                icon = closeButton,
                useHorizontalLayout = false,
                onLongClick = { onLongClick() },
                onClick = { entityInteractor.sendCommand(coverEntityState.getCloseCoverCommand()) }
            )
        }
    }
}

@Composable
fun CoverButton(
    modifier: Modifier = Modifier,
    icon: Drawable? = null,
    useDivider: Boolean = false,
    showIcon: Boolean = true,
    useHorizontalLayout: Boolean = true,
    onLongClick: () -> Unit,
    onClick: () -> Unit,
    imageAlignment: Alignment = Alignment.BottomCenter
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .effectClickable(
                hapticFeedback = LocalHapticFeedback.current,
                viewSoundEffect = LocalView.current,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onLongClick = onLongClick,
                onClick = onClick
            )
    ) {
        if (useHorizontalLayout) {
            if (useDivider) {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .requiredWidth(1.dp)
                        .align(Alignment.TopEnd)
                        .background(CoverItemButtonActive)
                )
            }
            if (showIcon) {
                Image(
                    modifier = Modifier
                        .requiredWidth(15.dp)
                        .aspectRatio(1f)
                        .align(imageAlignment),
                    painter = rememberDrawablePainter(drawable = icon),
                    contentDescription = "Button"
                )
            }
        } else {
            if (useDivider) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(1.dp)
                        .align(Alignment.BottomStart)
                        .background(CoverItemButtonActive)
                )
            }
            if (showIcon)
                Image(
                    modifier = Modifier
                        .requiredHeight(15.dp)
                        .aspectRatio(1f)
                        .align(imageAlignment),
                    painter = rememberDrawablePainter(drawable = icon),
                    contentDescription = "Button"
                )
        }
    }
}

@Composable
private fun StateLaunchEffect(
    panelItem: PanelItem,
    entityInteractor: EntityInteractor,
    callback: (CoverItemState) -> Unit
) = LaunchedEffect(key1 = panelItem) {
    entityInteractor.listenOnState(panelItem.entity, CoverEntityState::class).collect {
        callback.invoke(
            CoverItemState(
                icon = entityInteractor.getDrawable(
                    panelItem.icon ?: it.getMdiIconName(),
                    CoverItemButtonActive
                ),
                title = panelItem.title ?: it.friendlyName ?: it.entityId,
                entityState = it,
                position = it.position?.toString() ?: it.tiltPosition?.toString()
            )
        )
    }
}

@Composable
private fun CoverControlView(
    modifier: Modifier,
    entityState: CoverEntityState,
    entityInteractor: EntityInteractor
) {

    var positionText by remember { mutableStateOf((entityState.position?.toString() + "%") ?: "") }
    LaunchedEffect(entityState.position) {
        positionText = (entityState.position?.toString() + "%") ?: ""
    }
    Text(
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp),
        text = positionText,
        color = Color.White,
        style = FontStyleH2_SemiBold
    )

    VerticalSlider(
        modifier
            .fillMaxHeight(0.7f)
            .requiredWidth(300.dp),
        maxValue = 100f,
        initValue = 100f - (entityState.position?.toFloat() ?: 0f),
        aspectRatioOn = false,
        upSideDown = true,
        colorFlow = flowOf(CoverItemButtonActive)
    ) { value, motion ->
        positionText = "${100 - value.toInt()}%"
        if (motion == SliderMotion.UP) {
            entityInteractor.sendCommand(
                entityState.getPositionCoverCommand(100 - value.toInt())
            )
        }
    }
}

fun CoverEntityState.getMdiIconName(): String = when (deviceClass) {
    AWNING -> if (state == OPEN) MdiIcons.WINDOW_OPEN else MdiIcons.WINDOW_CLOSE
    BLIND -> if (state == OPEN) MdiIcons.BLINDS_OPEN else MdiIcons.BLINDS_CLOSE
    CURTAIN -> if (state == OPEN) MdiIcons.CURTAINS_OPEN else MdiIcons.CURTAINS_CLOSE
    DAMPER -> if (state == OPEN) MdiIcons.DAMPER_OPEN else MdiIcons.DAMPER_CLOSE
    DOOR -> if (state == OPEN) MdiIcons.DOOR_OPEN else MdiIcons.DOOR_CLOSE
    GARAGE -> if (state == OPEN) MdiIcons.GARAGE_OPEN else MdiIcons.GARAGE_CLOSE
    GATE -> if (state == OPEN) MdiIcons.GATE_OPEN else MdiIcons.GATE_CLOSE
    SHADE -> if (state == OPEN) MdiIcons.SHADE_OPEN else MdiIcons.SHADE_CLOSE
    SHUTTER -> if (state == OPEN) MdiIcons.SHUTTER_OPEN else MdiIcons.SHUTTER_CLOSE
    WINDOW -> if (state == OPEN) MdiIcons.WINDOW_OPEN else MdiIcons.WINDOW_CLOSE
    else -> "mdi:window-shutter"
}
