package com.panelsense.app.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.panelsense.app.R
import com.panelsense.app.disableSystemUI
import com.panelsense.app.ui.login.LoginActivity
import com.panelsense.app.ui.main.panel.FlexPanelView
import com.panelsense.app.ui.main.panel.GridPanelView
import com.panelsense.app.ui.main.panel.HomePanelView
import com.panelsense.app.ui.main.panel.NavigationBar
import com.panelsense.app.ui.main.panel.NavigationBarHeight
import com.panelsense.app.ui.main.panel.applyBackground
import com.panelsense.app.ui.theme.FontStyleH3_Medium
import com.panelsense.app.ui.theme.PanelSenseTheme
import com.panelsense.data.icons.IconProvider
import com.panelsense.domain.model.Configuration
import com.panelsense.domain.model.Panel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var iconProvider: IconProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disableSystemUI()
        setContent {

            PanelSenseTheme {
                val uiState = viewModel.stateFlow.collectAsState()

                val senseConfig = uiState.value.panelConfiguration ?: return@PanelSenseTheme

                Box(
                    modifier = Modifier
                        .applyBackground(
                            senseConfig.system.background,
                            senseConfig.system.foreground
                        )
                        .fillMaxSize()
                ) {
                    if (senseConfig.panelList.isEmpty()) {
                        NoPanels()
                    } else {
                        PagerPanels(
                            configuration = senseConfig,
                            entityInteractor = viewModel
                        )

                    }

                    if (!uiState.value.serverConnected) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .requiredHeight(30.dp)
                                .align(Alignment.BottomCenter)
                                .background(Color.Red),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = FontStyleH3_Medium,
                            text = stringResource(id = R.string.serverDisconnected)
                        )
                    }
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
                        finish()
                    }
                }
            }

        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
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
    configuration: Configuration,
    entityInteractor: EntityInteractor
) {

    val pagerState = object : PagerState() {
        override val pageCount: Int
            get() = configuration.panelList.size
    }

    val mainPanelId = configuration.getMainPanelPosition()

    LaunchedEffect(key1 = true) {
        delay(2000)
        pagerState.animateScrollToPage(mainPanelId)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = configuration.panelList.size
        ) { panelIndex ->
            when (val panelConfig = configuration.panelList[panelIndex]) {
                is Panel.HomePanel -> HomePanelView(
                    modifier = Modifier
                        .applyBackground(panelConfig.background, panelConfig.foreground)
                        .applyBottomPaddingIfNeeded(configuration.system.showNavBar),
                    panelConfig, entityInteractor
                )

                is Panel.GridPanel -> GridPanelView(
                    modifier = Modifier
                        .applyBackground(panelConfig.background, panelConfig.foreground)
                        .applyBottomPaddingIfNeeded(configuration.system.showNavBar),
                    panelConfig, entityInteractor
                )

                is Panel.FlexPanel -> FlexPanelView(
                    modifier = Modifier
                        .applyBackground(panelConfig.background, panelConfig.foreground)
                        .applyBottomPaddingIfNeeded(configuration.system.showNavBar),
                    panelConfig, entityInteractor
                )

                is Panel.UnknownPanel -> {}
            }
        }
        if (configuration.system.showNavBar) {
            NavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                pagerState,
                mainPanelId
            )
        }
    }
}

private fun Modifier.applyBottomPaddingIfNeeded(showNavBar: Boolean): Modifier {
    return if (showNavBar) {
        this.padding(bottom = NavigationBarHeight)
    } else {
        this
    }
}

private fun Configuration.getMainPanelPosition(): Int {
    val mainPanelId = system.mainPanelId
    return if (mainPanelId != null) {
        panelList.indexOfFirst { it.panelId == system.mainPanelId }
    } else {
        0
    }
}

val Panel.panelId: String?
    get() = when (this) {
        is Panel.HomePanel -> this.id
        is Panel.GridPanel -> this.id
        is Panel.FlexPanel -> this.id
        is Panel.UnknownPanel -> this.panelId
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

