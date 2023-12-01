package com.panelsense.app.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CLOSE_DELAY = 60000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelSenseBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val hideModalBottomSheet: () -> Unit = {
        coroutineScope.launch {
            bottomSheetState.hide()
            showBottomSheet.value = false
        }
    }

    if (showBottomSheet.value) {
        var timerJob by remember { mutableStateOf<Job?>(null) }
        LaunchedEffect(key1 = null) {
            timerJob = coroutineScope.launch {
                delay(CLOSE_DELAY)
                hideModalBottomSheet()
            }
        }

        ModalBottomSheet(
            modifier = Modifier
                .fillMaxSize(0.95f),
            shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            windowInsets = WindowInsets.displayCutout,
            containerColor = Color(0xD2444444),
            tonalElevation = 30.dp,
            onDismissRequest = {
                showBottomSheet.value = false
                timerJob?.cancel()
            },
            sheetState = bottomSheetState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = title,
                    color = Color.White,
                    style = FontStyleH2_SemiBold
                )

                Icon(
                    modifier = Modifier
                        .requiredWidth(32.dp)
                        .requiredHeight(32.dp)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            hideModalBottomSheet()
                        },
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            content()
        }
    }
}
