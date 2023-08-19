package com.nspanel.sense.ui.panel

import android.content.Context
import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import android.media.AudioManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@Suppress("TopLevelPropertyNaming")
const val DefaultButtonBackground = "#88cecece"
val GridPadding = 25.dp
val ButtonShape = RoundedCornerShape(30.dp)
val ButtonMiddleSpace = 20.dp

@Composable
@ExperimentalFoundationApi
fun GridPanel(
    panelConfiguration: GridPanel,
    iconProvider: IconProvider
) {
    var gridParentHeight: Int = remember {
        200
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(panelConfiguration.columnCount),
        modifier = Modifier
            .onGloballyPositioned {
                gridParentHeight = it.size.height
            },
        contentPadding = PaddingValues(GridPadding),
        verticalArrangement = Arrangement.spacedBy(GridPadding),
        horizontalArrangement = Arrangement.spacedBy(GridPadding)
    ) {

        items(count = panelConfiguration.gridItems.size) { index ->
            when (val item = panelConfiguration.gridItems[index]) {
                is ButtonItem -> PanelButton(
                    buttonConfig = item,
                    iconProvider = iconProvider,
                    parentHeight = gridParentHeight,
                    onClick = {}
                )
            }
        }
    }
}


@Composable
fun PanelButton(
    buttonConfig: ButtonItem,
    iconProvider: IconProvider,
    parentHeight: Int,
    onClick: () -> Unit
) {
    val buttonColor = Color(
        parseColor(
            buttonConfig.backgroundColor ?: DefaultButtonBackground
        )
    )

    Button(
        onClick = onClick.withSound(LocalContext.current),
        shape = ButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
        ),
        modifier = Modifier
            .height(parentHeight.dp)
    ) {

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                val drawableState = remember { mutableStateOf<Drawable?>(null) }

                LaunchedEffect(key1 = buttonConfig.icon) {
                    val iconSpec = IconSpec(
                        name = buttonConfig.icon,
                        color = Color.White.value.toInt()
                    )
                    drawableState.value = iconProvider.getIcon(iconSpec = iconSpec)
                }

                Image(
                    painter = rememberDrawablePainter(drawable = drawableState.value),
                    modifier = Modifier
                        .fillMaxSize(0.4f)
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
                    textAlign = TextAlign.Center,
                    color = Color(parseColor(buttonConfig.textColor))
                )
            }
        }
    }
}

fun (() -> Unit).withSound(context: Context): () -> Unit = {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
    this()
}

@Suppress("StringLiteralDuplication")
@Preview(showSystemUi = true)
@Composable
@ExperimentalFoundationApi
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
            override suspend fun getIcon(iconSpec: IconSpec) =
                ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
        }
    )
}
