package com.panelsense.app.ui.main.panel

import android.content.Context
import android.graphics.Color.parseColor
import android.media.AudioManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.domain.model.Panel
import com.panelsense.domain.model.PanelItem

@Suppress("TopLevelPropertyNaming")
const val DefaultButtonBackground = "#88cecece"
val GridPadding = 25.dp
val ButtonShape = RoundedCornerShape(30.dp)
val ButtonMiddleSpace = 20.dp

@Composable
@ExperimentalFoundationApi
fun GridPanelView(
    panelConfiguration: Panel.GridPanel,
    entityInteractor: EntityInteractor
) {
    var gridParentHeight: Int = remember {
        200
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .applyBackground(panelConfiguration.background)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = panelConfiguration.id ?: "Grid panel"
        )
    }
    /*  LazyVerticalGrid(
          columns = GridCells.Fixed(panelConfiguration.columnCount),
          modifier = Modifier
              .onGloballyPositioned {
                  gridParentHeight = it.size.height
              },
          contentPadding = PaddingValues(GridPadding),
          verticalArrangement = Arrangement.spacedBy(GridPadding),
          horizontalArrangement = Arrangement.spacedBy(GridPadding)
      ) {

          items(count = panelConfiguration.itemList.size) { index ->
              when (val item = panelConfiguration.itemList[index]) {
                  is PanelItem -> PanelButtonView(
                      buttonConfig = item,
                      entityInteractor = entityInteractor,
                      parentHeight = gridParentHeight,
                  )
              }
          }
      }*/
}


@Composable
fun PanelButtonView(
    buttonConfig: PanelItem,
    entityInteractor: EntityInteractor,
    parentHeight: Int,
) {
    val buttonColor = Color(parseColor(DefaultButtonBackground))

//    val buttonState: MutableState<PanelState.ButtonState> =
//        entityInteractor.listenOnState(buttonConfig.entity, LightEntityState::class).collectAsState(
//            initial =
//        )

    /*    Button(
            onClick = {
                onClick(buttonConfig.copy(state = buttonState.value))
            }.withSound(context = LocalContext.current),
            shape = ButtonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
            ),
            contentPadding = PaddingValues(0.dp),
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
                    LaunchedEffect(key1 = buttonState.value) {
                        val iconColor = if (buttonState.value.enabled) {
                            AndroidColor.YELLOW
                        } else {
                            AndroidColor.BLACK
                        }

                        val iconSpec = IconSpec(
                            name = buttonConfig.icon,
                            color = iconColor
                        )
                        drawableState.value = iconProvider.getIcon(iconSpec = iconSpec)
                    }

                    Image(
                        painter = rememberDrawablePainter(drawable = drawableState.value),
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .align(Alignment.CenterHorizontally),
                        contentDescription = buttonConfig.text,
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(ButtonMiddleSpace))
                    Text(
                        text = buttonConfig.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = Color(parseColor(buttonConfig.textColor))
                    )
                }
            }
        }*/
}

fun (() -> Unit).withSound(context: Context): (() -> Unit) = {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
    audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK)
    this()
}

/*
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
                    topic = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#ffcece",
                    topic = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#cecece",
                    topic = "test",
                ),
                ButtonItem(
                    id = "button1",
                    text = "Kitchen",
                    icon = "test",
                    backgroundColor = "#cecece",
                    topic = "test",
                ),
            )
        ),
        iconProvider = object : IconProvider {
            override suspend fun getIcon(iconSpec: IconSpec) =
                ContextCompat.getDrawable(context, R.drawable.ic_launcher_background)
        }
    )
}
*/
