package com.panelsense.app.ui.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.panelsense.app.R

@Composable
fun ErrorScreen(
    title: String? = null,
    message: String,
    confirmButtonText: String? = null,
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        icon = {},
        title = { Text(title ?: stringResource(id = R.string.errorTitle)) },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        onDismissRequest = {},
        dismissButton = {}
    )
}
