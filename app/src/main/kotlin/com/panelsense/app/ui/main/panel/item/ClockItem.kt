@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.panelsense.app.ui.theme.FontStyleH3
import com.panelsense.app.ui.theme.FontStyleLarge_Light
import com.panelsense.app.ui.theme.FontStyleXLarge_Light
import com.panelsense.domain.model.Panel
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
    homePanel: Panel.HomePanel,
    initState: ClockItemState = ClockItemState()
) {
    var state by remember { mutableStateOf(initState) }
    ClockItemLaunchEffect(homePanel = homePanel) {
        state = it
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row(verticalAlignment = Alignment.Bottom) {

            Text(
                style = FontStyleXLarge_Light,
                color = Color.White,
                text = state.time
            )
            if (state.timeAbbreviation != null) {
                Text(
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
    homePanel: Panel.HomePanel,
    callback: (ClockItemState) -> Unit
) = LaunchedEffect(key1 = homePanel) {
    while (true) {
        val zonedDateTime = ZonedDateTime.now()
        val time =
            zonedDateTime.format(DateTimeFormatter.ofPattern(if (homePanel.time24h) TIME_FORMAT_24 else TIME_FORMAT_12))
        val timeAbbreviation = zonedDateTime.format(DateTimeFormatter.ofPattern(TIME_ABBREVIATION))
            .takeIf { !homePanel.time24h }?.lowercase()
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
        homePanel = Panel.HomePanel(),
        initState = ClockItemState(
            time = "12:00",
            timeAbbreviation = null,
            date = "Mon, 01 January 2021"
        )
    )
}
