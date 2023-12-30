package com.panelsense.app.ui.main.panel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.panelsense.app.pxToDp
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest
import com.panelsense.app.ui.main.panel.item.PanelItemView
import com.panelsense.domain.model.Panel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

val ROW_ITEM_HEIGHT = 120.dp
const val ROW_COUNT = 3
val MIN_COLUMN_WIDTH = 250.dp
const val SCROLL_RESET_DELAY = 60_000L // 1 minute

@Composable
fun FlexPanelView(
    modifier: Modifier = Modifier,
    flexPanel: Panel.FlexPanel,
    entityInteractor: EntityInteractor
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .applyBackground(flexPanel.background)
    ) {

        val (rows, columns) = createRefs()

        FlexRowView(
            modifier = Modifier
                .constrainAs(rows) {
                    start.linkTo(parent.start, margin = 30.dp)
                    end.linkTo(parent.end, margin = 30.dp)
                    bottom.linkTo(parent.bottom, margin = 30.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
            flexPanel = flexPanel,
            entityInteractor = entityInteractor
        )

        FlexColumnsView(
            modifier = Modifier
                .constrainAs(columns) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(rows.top, margin = 30.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            flexPanel = flexPanel,
            entityInteractor = entityInteractor
        )
    }
}

@Composable
fun FlexRowView(
    modifier: Modifier = Modifier,
    flexPanel: Panel.FlexPanel,
    entityInteractor: EntityInteractor
) {
    Column(modifier = modifier) {
        flexPanel.rows.take(ROW_COUNT).forEachIndexed { columnIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                val weight = 1 / row.size.toFloat()
                row.forEachIndexed { rowIndex, item ->
                    PanelItemView(
                        modifier = Modifier
                            .weight(weight)
                            .requiredHeight(ROW_ITEM_HEIGHT),
                        panelItem = item, entityInteractor = entityInteractor
                    )

                    if ((rowIndex >= 0 && row.size > 1) && rowIndex < row.size - 1) {
                        Spacer(modifier = Modifier.requiredWidth(30.dp))
                    }
                }

            }
            val isNotLast = columnIndex < flexPanel.rows.size - 1

            if (isNotLast) {
                Spacer(modifier = Modifier.requiredHeight(30.dp))
            }
        }
    }
}

@Composable
fun FlexColumnsView(
    modifier: Modifier = Modifier,
    flexPanel: Panel.FlexPanel,
    entityInteractor: EntityInteractor
) {
    var maxWidth by remember { mutableStateOf<Int?>(null) }
    val rowState = rememberLazyListState()
    val rowScrollConnection = scrollReset(rowState)

    LazyRow(
        modifier = modifier
            .onSizeChanged { maxWidth = it.width }
            .drawWithContent {
                if (maxWidth != null) {
                    drawContent()
                }
            }
            .nestedScroll(rowScrollConnection),
        state = rowState,
    ) {
        val columnCount = flexPanel.columns.size
        itemsIndexed(
            flexPanel.columns
        ) { rowIndex, column ->
            if (maxWidth == null) return@itemsIndexed
            val maxWidthToCalculate =
                maxWidth!!.pxToDp() - (30.dp.times(columnCount - 1).plus(60.dp))
            val columnWidth = (maxWidthToCalculate.div(columnCount)).coerceAtLeast(MIN_COLUMN_WIDTH)
            val columnState = rememberLazyListState()
            val columnScrollConnection = scrollReset(columnState)

            if (rowIndex == 0) {
                Spacer(modifier = Modifier.requiredWidth(30.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .requiredWidth(columnWidth)
                    .nestedScroll(columnScrollConnection),
                state = columnState
            ) {
                itemsIndexed(column) { columnIndex, item ->
                    if (columnIndex == 0) {
                        Spacer(modifier = Modifier.requiredHeight(30.dp))
                    }
                    PanelItemView(
                        modifier = Modifier,
                        panelItem = item,
                        entityInteractor = entityInteractor,
                        layoutRequest = PanelItemLayoutRequest.Flex
                    )

                    Spacer(modifier = Modifier.requiredHeight(30.dp))
                }

            }

            val isNotLast = rowIndex < flexPanel.columns.size - 1

//            if (isNotLast) {
                Spacer(modifier = Modifier.requiredWidth(30.dp))
//            }
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun scrollReset(state: LazyListState): NestedScrollConnection {
    val scope = rememberCoroutineScope()
    val resetFlow by remember { mutableStateOf(MutableStateFlow<Float?>(null)) }
    LaunchedEffect(key1 = null) {
        resetFlow
            .filter { it != null }
            .debounce(SCROLL_RESET_DELAY)
            .collect {
                state.animateScrollToItem(0, 0)
            }
    }
    return remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                scope.launch {
                    resetFlow.emit(consumed.y)
                }

                return super.onPostScroll(consumed, available, source)
            }
        }
    }
}
