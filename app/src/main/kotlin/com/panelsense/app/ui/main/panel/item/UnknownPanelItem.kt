package com.panelsense.app.ui.main.panel.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.panelsense.app.R
import com.panelsense.app.ui.main.panel.ButtonShape
import com.panelsense.app.ui.main.panel.GridPadding
import com.panelsense.app.ui.theme.FontStyleH3_SemiBold
import com.panelsense.app.ui.theme.PanelItemBackgroundColor
import com.panelsense.app.ui.theme.PanelItemTitleColor
import com.panelsense.domain.model.PanelItem

@Composable
fun UnknownPanelItem(modifier: Modifier, panelItem: PanelItem) {
    Box(
        modifier = modifier
            .background(
                color = PanelItemBackgroundColor,
                shape = ButtonShape
            )
            .padding(GridPadding)
            .fillMaxHeight(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        val title = stringResource(id = R.string.unknownPanel, panelItem.entity)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            color = PanelItemTitleColor,
            style = FontStyleH3_SemiBold
        )
    }
}
