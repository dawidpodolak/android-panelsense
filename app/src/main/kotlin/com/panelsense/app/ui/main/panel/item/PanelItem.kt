package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.domain.entityToDomain
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.ItemTypeDomain
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.typeToDomain

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

        PanelItemViewType.WEATHER -> WeatherItemView(
            modifier,
            panelItem = panelItem,
            weatherEntity = panelItem.entity!!,
            entityInteractor = entityInteractor,
            layoutRequest = layoutRequest
        )

        PanelItemViewType.CLOCK -> ClockItemView(
            modifier,
            panelItem = panelItem,
            layoutRequest = layoutRequest,
        )

        PanelItemViewType.SENSOR -> SensorItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = layoutRequest
        )

        PanelItemViewType.BINARY_SENSOR -> BinarySensorItemView(
            modifier,
            panelItem = panelItem,
            entityInteractor = entityInteractor,
            layoutRequest = layoutRequest
        )

        PanelItemViewType.GRID -> GridItemView(
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
    WEATHER,
    CLOCK,
    NONE,
    SENSOR,
    BINARY_SENSOR,
    GRID
}

fun PanelItem.getItemViewType(): PanelItemViewType {
    val itemTypeFromType = when (this.type?.typeToDomain) {
        ItemTypeDomain.CLOCK -> PanelItemViewType.CLOCK
        ItemTypeDomain.GRID -> PanelItemViewType.GRID
        else -> null
    }

    val itemTypeFromEntity = when (this.entity?.entityToDomain) {
        EntityDomain.COVER -> PanelItemViewType.COVER
        EntityDomain.LIGHT -> PanelItemViewType.SIMPLE
        EntityDomain.SWITCH -> PanelItemViewType.SIMPLE
        EntityDomain.WEATHER -> PanelItemViewType.WEATHER
        EntityDomain.SENSOR -> PanelItemViewType.SENSOR
        EntityDomain.BINARY_SENSOR -> PanelItemViewType.BINARY_SENSOR
        else -> null
    }

    return itemTypeFromType ?: itemTypeFromEntity ?: PanelItemViewType.NONE
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

    object Grid : PanelItemLayoutRequest {
        val ItemHeight = 110.dp
        val ItemWidth = 100.dp
    }

    companion object {
        fun Modifier.applySizeForRequestLayout(
            layoutRequest: PanelItemLayoutRequest,
            height: Dp? = null
        ): Modifier =
            when (layoutRequest) {
                is Grid -> this
                    .requiredHeight(Grid.ItemHeight)
                    .requiredWidth(Grid.ItemWidth)

                is Flex -> this
                    .fillMaxWidth()
                    .run { if (height != null) requiredHeight(height) else this }

                else -> this
            }
    }
}
