package com.nspanel.sense.ui.panel

import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.nspanel.core.model.IconSpec
import com.nspanel.core.model.PanelConfiguration.GridPanel
import com.nspanel.core.model.PanelConfiguration.PanelItem.ButtonItem
import com.nspanel.data.icons.IconProvider
import com.nspanel.sense.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

@Suppress("TopLevelPropertyNaming")
const val DefaultButtonBackground = "#88cecece"
val GridPadding = 25.dp
val ButtonShape = RoundedCornerShape(30.dp)
val ButtonMiddleSpace = 20.dp

@Composable
fun GridPanel(
    panelConfiguration: GridPanel,
    iconProvider: IconProvider
) {
    var parentHeight: Int = remember {
        200
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(panelConfiguration.columnCount),
        modifier = Modifier.onGloballyPositioned {
            parentHeight = it.size.height
        },
        userScrollEnabled = false,
        contentPadding = PaddingValues(GridPadding),
        verticalArrangement = Arrangement.spacedBy(GridPadding),
        horizontalArrangement = Arrangement.spacedBy(GridPadding)
    ) {

        items(count = panelConfiguration.gridItems.size) { index ->
            when (val item = panelConfiguration.gridItems[index]) {
                is ButtonItem -> PanelButton(
                    buttonConfig = item,
                    iconProvider = iconProvider,
                    parentHeight = parentHeight
                )
            }
        }
    }
}

@Composable
fun PanelButton(
    buttonConfig: ButtonItem,
    iconProvider: IconProvider,
    parentHeight: Int
) {
    Box(
        modifier = Modifier
            .height(parentHeight.dp)
            .clickable {

            }
            .background(
                color = Color(parseColor(buttonConfig.backgroundColor ?: DefaultButtonBackground)),
                shape = ButtonShape
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
        ) {

            val iconSpec = IconSpec(
                name = "weather-partly-cloudy",
                color = Color.White.value.toInt()
            )
            Timber.d("Icon spec: $iconSpec")
            val painter1 = painterResource(id = R.drawable.ic_launcher_background)
            val drawableState = iconProvider.getIcon(iconSpec = iconSpec).collectAsState(initial = null)
            Image(
                painter = rememberDrawablePainter(drawable = drawableState.value),
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .align(Alignment.CenterHorizontally),
                contentDescription = "Button 1",
                alignment = Alignment.Center
            )
            Spacer(modifier = Modifier.height(ButtonMiddleSpace))
            Text(
                text = buttonConfig.text,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = Color(parseColor(buttonConfig.textColor))
            )
        }
    }
}

@Suppress("StringLiteralDuplication")
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    val context = LocalContext.current
    GridPanel(
        GridPanel(
            id = "mock",
            columnCount = 2,
            gridItems = listOf(
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#cecece",
                    entity = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#ffcece",
                    entity = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#cecece",
                    entity = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#cecece",
                    entity = "test",
                ),
            )
        ),
        iconProvider = object : IconProvider {
            override fun getIcon(iconSpec: IconSpec): Flow<Drawable> =
                flow { ContextCompat.getDrawable(context, R.drawable.ic_launcher_background) }
        }
    )
}
