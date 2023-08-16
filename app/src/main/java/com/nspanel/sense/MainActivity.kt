package com.nspanel.sense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.nspanel.data.icons.IconProvider
import com.nspanel.sense.ui.theme.NsPanelSenseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var iconProvider: IconProvider

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        setContent {
            NsPanelSenseTheme {
                val state = object : PagerState() {
                    override val pageCount: Int
                        get() = 3
                }

                HorizontalPager(state = state) { panelIndex ->
                    when (panelIndex) {
                        0 -> ButtonPanel(this@MainActivity.lifecycleScope)
                        1 -> CameraPanel()
                        2 -> PhotoPanel()
                    }
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Greeting("Android")
                    }
                }
            }
        }
    }

    @Composable
    fun ButtonPanel(lifecycleScope: LifecycleCoroutineScope) {
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {

            items(count = 2) {
                Button(onClick = { }) {
                    Text("Button 1")

                    val drawable =
                        iconProvider.getIcon(this@MainActivity, "weather-partly-cloudy")
                            .collectAsState(
                                initial = null
                            )

                    Image(
                        painter = rememberDrawablePainter(drawable = drawable.value),
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = "Button 1"
                    )
                }
            }
        }
    }
}

@Composable
fun CameraPanel() {
    Surface {
        Text("Camera panel")
    }
}

@Composable
fun PhotoPanel() {
    Surface {
        Text("Photo panel")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
    Text(text = "To jest test!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NsPanelSenseTheme {
        Greeting("Android")
    }
}

