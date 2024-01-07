@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.panel.applyBackgroundForItem
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Companion.applySizeForRequestLayout
import com.panelsense.app.ui.theme.FontStyleH3
import com.panelsense.app.ui.theme.FontStyleLarge_Light
import com.panelsense.app.ui.theme.FontStyleXLarge_Light
import com.panelsense.app.ui.theme.FontStyleXXLarge_Light
import com.panelsense.domain.model.PanelItem
import kotlinx.coroutines.delay
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

data class ClockItemState(
    val time: String = "12:00",
    val timeAbbreviation: String? = null,
    val date: String = ""
)

const val TIME_FORMAT_24 = "HH:mm"
const val TIME_FORMAT_12 = "hh:mm"
const val TIME_ABBREVIATION = "a"
const val DATE_FORMAT = "EEEE, dd MMMM"

@Composable
fun ClockItemView(
    modifier: Modifier,
    panelItem: PanelItem = PanelItem(),
    layoutRequest: PanelItemLayoutRequest = PanelItemLayoutRequest.Standard,
    initState: ClockItemState = ClockItemState()
) {
    var state by remember { mutableStateOf(initState) }
    ClockItemLaunchEffect(time24h = panelItem.time24h ?: false) {
        state = it
    }

    Column(
        modifier = modifier
            .applySizeForRequestLayout(layoutRequest)
            .applyBackgroundForItem(panelItem, layoutRequest),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {

            Text(
                style = if (layoutRequest is PanelItemLayoutRequest.Flex) FontStyleXXLarge_Light else FontStyleXLarge_Light,
                color = Color.White,
                text = state.time
            )
            if (state.timeAbbreviation != null) {
                Text(
                    modifier = Modifier.offset(y = (-8).dp),
                    style = FontStyleLarge_Light,
                    color = Color.White,
                    text = state.timeAbbreviation!!,
                )
            }
        }
        Text(
            modifier = Modifier,
            style = FontStyleH3,
            color = Color.White,
            text = state.date
        )
    }
}

@Composable
fun ClockItemLaunchEffect(
    time24h: Boolean = false,
    callback: (ClockItemState) -> Unit
) = LaunchedEffect(key1 = time24h) {
    while (true) {
        val zonedDateTime = ZonedDateTime.now()
        val time =
            zonedDateTime.format(DateTimeFormatter.ofPattern(if (time24h) TIME_FORMAT_24 else TIME_FORMAT_12))
        val timeAbbreviation = zonedDateTime.format(DateTimeFormatter.ofPattern(TIME_ABBREVIATION))
            .takeIf { !time24h }?.lowercase()
        val date = zonedDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
        callback(ClockItemState(time, timeAbbreviation, date))
        delay(6000)
    }
}

@Preview
@Composable
fun ClockItemPreview() {
    ClockItemView(
        modifier = Modifier,
        panelItem = PanelItem(
            id = "1",
            type = "clock",
            entity = "sensor.time",
            time24h = false
        ),
        initState = ClockItemState(
            time = "12:00",
            timeAbbreviation = null,
            date = "Mon, 01 January 2021"
        )
    )
}
