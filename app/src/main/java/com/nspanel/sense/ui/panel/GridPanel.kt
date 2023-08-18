package com.nspanel.sense.ui.panel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nspanel.sense.R

@Composable
fun GridPanel() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(count = 4) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clickable {

                    }
                    .background(
                        color = Color.Blue,
                        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Cyan),
                    verticalArrangement = Arrangement.Center,
                ) {

//                    iconProvider.getIcon(context, "weather-partly-cloudy")
//                        .collectAsState(
//                            initial = null
//                        )

                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        modifier = Modifier
                            .background(Color.White),
                        contentDescription = "Button 1",
                        alignment = Alignment.Center
                    )
                    Text("Button 1", color = Color.White)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    GridPanel()
}
