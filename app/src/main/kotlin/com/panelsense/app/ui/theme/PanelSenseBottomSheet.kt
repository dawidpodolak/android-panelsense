package com.panelsense.app.ui.theme

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelSenseBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxSize(0.95f),
            shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            containerColor = Color(0xD2444444),
            tonalElevation = 30.dp,
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = title,
                color = Color.White,
                style = FontStyleH2_SemiBold
            )
            content()
        }
    }
}
