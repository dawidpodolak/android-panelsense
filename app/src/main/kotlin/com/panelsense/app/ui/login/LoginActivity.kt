package com.panelsense.app.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import com.panelsense.app.disableSystemUI
import com.panelsense.app.ui.main.MainActivity
import com.panelsense.app.ui.main.theme.PanelSenseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableSystemUI()
        setContent {
            PanelSenseTheme {
                val state = viewModel.stateFlow.collectAsState()
                val errorState = viewModel.errorStatFlow.collectAsState()
                LoginScreen(state, errorState, viewModel::login, viewModel::clearError)
            }
        }
        lifecycleScope.launch {
            viewModel.collectNavCommand { navCommand ->
                when (navCommand) {
                    is LoginNavCommand.NavigateToMain -> {
                        MainActivity.start(this@LoginActivity)
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
}
