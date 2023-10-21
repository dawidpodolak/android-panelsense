package com.panelsense.app.ui.main.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.panelsense.app.R

val fontFamily = FontFamily(
    Font(R.font.opensans_extra_bold, FontWeight.ExtraBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
    Font(R.font.opensans_semi_bold, FontWeight.SemiBold),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_light, FontWeight.Light)
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
