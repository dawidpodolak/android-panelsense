package com.nspanel.sense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nspanel.data.icons.IconProvider
import com.nspanel.sense.ui.panel.GridPanel
import com.nspanel.sense.ui.theme.NsPanelSenseTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var iconProvider: IconProvider

    val viewModel: MainViewModel by viewModels<MainViewModel>()

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
                        0 -> GridPanel()
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

