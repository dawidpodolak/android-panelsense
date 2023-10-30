package com.panelsense.app.ui.main.panel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.panelsense.app.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    mainPanelPosition: Int? = null
) {
    Box(
        modifier = modifier
            .requiredHeight(60.dp)
            .fillMaxWidth()
            .background(Color(0x80464646))
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val coroutineScope = rememberCoroutineScope()
            val (leftButton, centerButton, rightButton) = createRefs()
            val buttonTint = Color(0xFFA7A7A7)

            if (pagerState.canScrollBackward) {
                IconButton(
                    modifier = Modifier
                        .constrainAs(leftButton) {
                            end.linkTo(centerButton.start, margin = 60.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.triangle_left_outline),
                        contentDescription = "back",
                        tint = buttonTint
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .constrainAs(centerButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxHeight()
                    .aspectRatio(1f),
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(mainPanelPosition ?: 0)
                    }
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.circle_outline),
                    contentDescription = "back",
                    tint = buttonTint
                )
            }
            if (pagerState.canScrollForward) {
                IconButton(
                    modifier = Modifier
                        .constrainAs(rightButton) {
                            start.linkTo(centerButton.end, margin = 60.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.triangle_right_outline),
                        contentDescription = "back",
                        tint = buttonTint
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun NavigationPreview() {
    NavigationBar(pagerState = object : PagerState() {
        override val pageCount: Int = 4
    })
}
