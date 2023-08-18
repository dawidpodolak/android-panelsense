package com.nspanel.sense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.nspanel.core.model.PanelConfiguration
import com.nspanel.core.model.PanelConfiguration.GridPanel
import com.nspanel.core.model.PanelConfiguration.HomePanel
import com.nspanel.data.icons.IconProvider
import com.nspanel.sense.ui.panel.GridPanel
import com.nspanel.sense.ui.panel.HomePanel
import com.nspanel.sense.ui.theme.NsPanelSenseTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var iconProvider: IconProvider

    val viewModel: MainViewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NsPanelSenseTheme {

                val uiState = viewModel.stateFlow.collectAsState()
                Timber.d("Sense config ui: $uiState")
                val senseConfig = uiState.value.senseConfiguration ?: return@NsPanelSenseTheme
                Timber.d("Sense config: $senseConfig")
                Background(senseConfig.systemConfiguration?.backgroundImageUrl)
                if (senseConfig.panelList.isEmpty()) {
                    NoPanels()
                } else {
                    PagerPanels(
                        panels = senseConfig.panelList,
                        iconProvider = iconProvider
                    )
                }
            }
        }
    }
}

@Composable
@Suppress("EmptyFunctionBlock")
fun NoPanels() {

}

@Composable
@ExperimentalFoundationApi
fun PagerPanels(
    panels: List<PanelConfiguration>,
    iconProvider: IconProvider
) {

    val pagerState = object : PagerState() {
            override val pageCount: Int
                get() = panels.size
        }

    HorizontalPager(state = pagerState) { panelIndex ->
        when (val panelConfig = panels[panelIndex]) {
            is HomePanel -> HomePanel(panelConfig)
            is GridPanel -> GridPanel(panelConfig, iconProvider)
        }
    }
}

@Composable
fun Background(imageUrl: String?) {
    val painter = if (imageUrl != null) {
        rememberAsyncImagePainter(model = imageUrl)
    } else {
        painterResource(id = R.drawable.background)
    }
    Image(
        painter = painter,
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
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

