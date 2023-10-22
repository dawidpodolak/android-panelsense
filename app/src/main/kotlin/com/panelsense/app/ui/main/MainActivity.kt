package com.panelsense.app.ui.main

import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.panelsense.app.R
import com.panelsense.app.ui.login.LoginActivity
import com.panelsense.app.ui.main.panel.GridPanel
import com.panelsense.app.ui.main.panel.HomePanel
import com.panelsense.app.ui.main.theme.PanelSenseTheme
import com.panelsense.core.model.mqqt.MqttMessage
import com.panelsense.core.model.panelconfig.PanelConfiguration
import com.panelsense.core.model.panelconfig.PanelConfiguration.GridPanel
import com.panelsense.core.model.panelconfig.PanelConfiguration.HomePanel
import com.panelsense.data.icons.IconProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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
        Log.d("MainActivity", "onCreate")
        Timber.d("onCreate")
        setContent {
            PanelSenseTheme {

                val uiState = viewModel.stateFlow.collectAsState()
//                Timber.d("Sense config ui: $uiState")

                val senseConfig = uiState.value.senseConfiguration ?: return@PanelSenseTheme
//                Timber.d("Sense config: $senseConfig")
                Background(senseConfig.systemConfiguration?.backgroundImageUrl)
                if (senseConfig.panelList.isEmpty()) {
                    NoPanels()
                } else {
                    PagerPanels(
                        panels = senseConfig.panelList,
                        iconProvider = iconProvider,
                        stateFlow = viewModel.messageFlow,
                        onClick = viewModel::onItemClick
                    )
                }
            }
        }
        setupNavigation()
    }

    private fun setupNavigation() {
        lifecycleScope.launch {
            viewModel.collectNavCommand { navCommand ->
                when (navCommand) {
                    is MainNavCommand.NavigateToLogin -> {
                        LoginActivity.start(this@MainActivity)
                    }
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
    iconProvider: IconProvider,
    stateFlow: Flow<MqttMessage>,
    onClick: (PanelConfiguration.PanelItem.ButtonItem) -> Unit
) {

    val pagerState = object : PagerState() {
            override val pageCount: Int
                get() = panels.size
        }

    LaunchedEffect(key1 = true) {
        delay(2000)
        pagerState.animateScrollToPage( 1)
    }
    HorizontalPager(state = pagerState) { panelIndex ->
        when (val panelConfig = panels[panelIndex]) {
            is HomePanel -> HomePanel(panelConfig, iconProvider)
            is GridPanel -> GridPanel(panelConfig, iconProvider, stateFlow, onClick)
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
    PanelSenseTheme {
        Greeting("Android")
    }
}

