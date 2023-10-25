package com.panelsense.app.ui.login

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panelsense.app.R
import com.panelsense.app.ui.main.theme.BackgroundColor
import com.panelsense.app.ui.main.theme.FontStyleButton
import com.panelsense.app.ui.main.theme.FontStyleH3
import com.panelsense.app.ui.main.theme.FontStyleH4
import com.panelsense.app.ui.main.theme.FontStyleH4_SemiBold
import com.panelsense.app.ui.util.ErrorScreen
import com.panelsense.domain.model.ServerConnectionData

@Composable
fun LoginScreen(
    state: State<LoginViewState>,
    errorState: State<Throwable?>,
    loginRequest: (ServerConnectionData) -> Unit,
    clearError: () -> Unit = {}
) {
    Box(modifier = Modifier.background(BackgroundColor)) {
        val scrollState by remember { mutableStateOf(ScrollState(0)) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(10.dp)
                .verticalScroll(scrollState, enabled = true)
        ) {

            var addressText by remember { mutableStateOf("") }
            var portText by remember { mutableStateOf("") }
            var panelSenseNameText by remember { mutableStateOf("") }
            var userNameText by remember { mutableStateOf("") }
            var passwordText by remember { mutableStateOf("") }
            var passwordShow by remember { mutableStateOf(false) }

            Image(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxSize(0.5f)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )

            Text(
                text = stringResource(id = R.string.loginScreenProvideServerAddress),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                style = FontStyleH4_SemiBold
            )

            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(2f),
                    value = addressText,
                    singleLine = true,
                    onValueChange = { addressText = it },
                    label = { Text(stringResource(id = R.string.loginScreenServerAddress)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp), text = ":", style = FontStyleH3
                )
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = portText,
                    singleLine = true,
                    onValueChange = { portText = it },
                    label = { Text(stringResource(id = R.string.loginScreenServerPort)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next, keyboardType = KeyboardType.Number
                    )
                )
            }

            Text(
                text = stringResource(id = R.string.loginScreenProvideUserAndPass),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(Alignment.CenterHorizontally),
                style = FontStyleH4_SemiBold
            )

            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = userNameText,
                    singleLine = true,
                    onValueChange = { userNameText = it },
                    label = { Text(stringResource(id = R.string.loginScreenUserName)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.weight(0.1f))

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = passwordText,
                    singleLine = true,
                    onValueChange = { passwordText = it },
                    label = { Text(stringResource(id = R.string.loginScreenPassword)) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (passwordShow) VisualTransformation.None else PasswordVisualTransformation(),
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = panelSenseNameText,
                singleLine = true,
                onValueChange = { panelSenseNameText = it },
                label = { Text(stringResource(id = R.string.loginScreenPanelName)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Checkbox(checked = !passwordShow, onCheckedChange = { passwordShow = !it })

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { passwordShow = !passwordShow },
                    text = stringResource(id = R.string.loginScreenShowPassword),
                    style = FontStyleH4,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.align(Alignment.Center),
                    visible = !state.value.isConnecting,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        enabled = validateData(addressText, portText, userNameText, passwordText),
                        onClick = {
                            loginRequest(
                                ServerConnectionData(
                                    serverAddressIp = addressText,
                                    serverPort = portText,
                                    panelSenseName = panelSenseNameText,
                                    userName = userNameText,
                                    password = passwordText
                                )
                            )
                        },
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = stringResource(id = R.string.loginScreenConnect),
                            style = FontStyleButton,
                        )
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.align(Alignment.Center),
                    visible = state.value.isConnecting,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    val error by remember { errorState }
    error?.let {
        ErrorScreen(
            message = stringResource(id = R.string.loginScreenErrorMessage), onConfirm = clearError
        )
    }
}


fun validateData(addressIp: String, port: String, userName: String, password: String): Boolean {
    val ipRegex = Regex("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\$")
    return ipRegex.matches(addressIp) && port.toIntOrNull() != null && userName.isNotEmpty() && password.isNotEmpty()
}

@Suppress("StringLiteralDuplication")
@Preview(showSystemUi = false, widthDp = 480, heightDp = 480)
@Composable
@ExperimentalFoundationApi
fun LoginScreenRenderer() {
    LoginScreen(state = remember { mutableStateOf(LoginViewState()) },
        errorState = remember { mutableStateOf(null) },
        {},
        {})
}
