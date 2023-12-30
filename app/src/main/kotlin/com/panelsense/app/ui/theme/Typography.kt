package com.panelsense.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
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
        fontSize = 16.sp,
        color = TextColor
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

val FontStyleH1_Bold = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH1_SemiBold = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH1 = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)
val FontStyleLarge_Light = TextStyle(
    fontSize = 48.sp,
    fontWeight = FontWeight.Light,
    color = TextColor
)
val FontStyleLarge_SemiBold = TextStyle(
    fontSize = 48.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)

val FontStyleXLarge_Light = TextStyle(
    fontSize = 64.sp,
    fontWeight = FontWeight.Light,
    color = TextColor
)

val FontStyleXXLarge_Light = TextStyle(
    fontSize = 96.sp,
    fontWeight = FontWeight.Light,
    color = TextColor
)

val FontStyleH2_Bold = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH2_SemiBold = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH2 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)

val FontStyleH3_Bold = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH3_SemiBold = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH3_Medium = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.Medium,
    color = TextColor
)
val FontStyleH3_Regular = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)
val FontStyleH3 = TextStyle(
    fontSize = 19.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)

val FontStyleH4_Bold = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH4_SemiBold = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH4 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)

val FontStyleH5_Bold = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH5_SemiBold = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH5 = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)

val FontStyleH6_Bold = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    color = TextColor
)
val FontStyleH6_SemiBold = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextColor
)
val FontStyleH6 = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    color = TextColor
)
val FontStyleButton = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium,
    color = Color.White
)
