@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.PanelSizeHelper.PanelItemSize
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.getOrientationHelper
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.app.ui.theme.FontStyleH1_SemiBold
import com.panelsense.app.ui.theme.FontStyleH3
import com.panelsense.app.ui.theme.FontStyleH4_SemiBold
import com.panelsense.app.ui.theme.MdiIcons
import com.panelsense.domain.model.Panel
import com.panelsense.domain.model.entity.state.WeatherEntityState
import com.panelsense.domain.model.entity.state.WeatherEntityState.WeatherCondition

data class WeatherStateView(
    val weatherState: WeatherEntityState? = null
)

@Composable
fun WeatherItemView(
    modifier: Modifier = Modifier,
    homePanel: Panel.HomePanel,
    entityInteractor: EntityInteractor,
    initState: WeatherStateView = WeatherStateView()
) {
    var state by remember { mutableStateOf(initState) }
    WeatherItemLaunchEffect(homePanel = homePanel, entityInteractor) {
        state = it
    }
    if (state.weatherState == null) return
    ConstraintLayout(
        modifier
    ) {
        val (todaySection, temperature, weatherDescription, weatherForecast) = createRefs()

        TodayWeatherView(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
                .constrainAs(todaySection) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }, weatherState = state.weatherState!!, entityInteractor
        )
    }
}

@Composable
private fun TodayWeatherView(
    modifier: Modifier,
    weatherState: WeatherEntityState,
    entityInteractor: EntityInteractor
) {
    val orientationHelper = getOrientationHelper()
    ConstraintLayout(
        modifier
            .onGloballyPositioned(orientationHelper::onGlobalLayout)
    ) {
        val state = weatherState.state ?: return@ConstraintLayout
        val (
            todayWeatherIcon,
            todayWeatherText,
            todayWeatherHumidity,
            todayWeatherPressure,
            todayWeatherWind,
            todayWeatherTemperature,
        ) = createRefs()

        val temperature =
            weatherState.temperature?.run { "$this${weatherState.temperatureUnit ?: ""}" }

        Image(
            modifier = Modifier
                .constrainAs(todayWeatherIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            painter = painterResource(id = state.getIconRes()),
            contentDescription = stringResource(state.getTextRes())
        )

        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .constrainAs(todayWeatherText) {
                    top.linkTo(todayWeatherIcon.top)
                    bottom.linkTo(todayWeatherIcon.bottom)
                    start.linkTo(todayWeatherIcon.end)
                },
            text = stringResource(state.getTextRes()),
            style = FontStyleH3,
            color = Color.White
        )

        if (temperature != null) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .constrainAs(todayWeatherTemperature) {
                        if (orientationHelper.panelItemSize == PanelItemSize.SMALL) {
                            top.linkTo(todayWeatherIcon.bottom)
                            end.linkTo(parent.end)
                        } else {
                            top.linkTo(todayWeatherIcon.top)
                            bottom.linkTo(todayWeatherIcon.bottom)
                            start.linkTo(todayWeatherText.end)
                        }
                    },
                text = temperature,
                style = FontStyleH1_SemiBold,
                color = Color.White
            )
        }

        WeatherAttributeView(
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(todayWeatherHumidity) {
                    top.linkTo(todayWeatherIcon.bottom)
                    start.linkTo(parent.start)
                },
            attr = weatherState.humidity?.run { "$this %" },
            mdiIconName = MdiIcons.HUMIDITY,
            entityInteractor = entityInteractor
        )

        WeatherAttributeView(
            modifier = Modifier
                .constrainAs(todayWeatherPressure) {
                    top.linkTo(todayWeatherHumidity.bottom)
                    start.linkTo(parent.start)
                },
            attr = weatherState.pressure?.run { "$this ${weatherState.pressureUnit ?: ""} " },
            mdiIconName = MdiIcons.GAUGE,
            entityInteractor = entityInteractor
        )

        WeatherAttributeView(
            modifier = Modifier
                .constrainAs(todayWeatherWind) {
                    top.linkTo(todayWeatherPressure.bottom)
                    start.linkTo(parent.start)
                },
            attr = weatherState.windSpeed?.run { "$this ${weatherState.windSpeedUnit ?: ""} " },
            mdiIconName = MdiIcons.WIND,
            entityInteractor = entityInteractor
        )
    }
}

@Composable
private fun WeatherAttributeView(
    modifier: Modifier,
    attr: String?,
    mdiIconName: String,
    entityInteractor: EntityInteractor
) {
    if (attr == null) {
        Spacer(modifier = modifier)
        return
    }
    var iconState by remember { mutableStateOf<Drawable?>(null) }
    LaunchedEffect(key1 = mdiIconName) {
        iconState = entityInteractor.getDrawable(mdiIconName, Color.White)
    }
    Row(
        modifier = modifier
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = rememberDrawablePainter(drawable = iconState), contentDescription = attr)
        Spacer(modifier = Modifier.requiredWidth(5.dp))
        Text(text = attr, style = FontStyleH4_SemiBold, color = Color.White)
    }
}

@Composable
fun WeatherItemLaunchEffect(
    homePanel: Panel.HomePanel,
    entityInteractor: EntityInteractor,
    callback: (WeatherStateView) -> Unit
) = LaunchedEffect(key1 = homePanel) {
    val weatherState = entityInteractor.listenOnState(
        homePanel.weatherEntity ?: return@LaunchedEffect,
        WeatherEntityState::class
    )
    weatherState.collect {
        callback(WeatherStateView(it))
    }
}

@Composable
@Suppress("UnusedPrivateMember")
private fun WeatherForecastItemsView(modifier: Modifier) {
    ConstraintLayout(modifier) {

    }
}

@DrawableRes
private fun WeatherCondition.getIconRes(): Int = when (this) {
    WeatherCondition.CLEAR_NIGHT -> R.drawable.ic_weather_clear_night
    WeatherCondition.CLOUDY -> R.drawable.ic_weather_cloudy
    WeatherCondition.EXCEPTIONAL -> R.drawable.ic_exclamation
    WeatherCondition.FOG -> R.drawable.ic_weather_fog
    WeatherCondition.HAIL -> R.drawable.ic_weather_hail
    WeatherCondition.LIGHTNING -> R.drawable.ic_weather_lightning
    WeatherCondition.LIGHTNING_RAINY -> R.drawable.ic_weather_lightning_rainy
    WeatherCondition.PARTLY_CLOUDY -> R.drawable.ic_weather_partly_cloudy
    WeatherCondition.POURING -> R.drawable.ic_weather_pouring
    WeatherCondition.RAINY -> R.drawable.ic_weather_rainy
    WeatherCondition.SNOWY -> R.drawable.ic_weather_snowy
    WeatherCondition.SNOWY_RAINY -> R.drawable.ic_weather_snowy_rainy
    WeatherCondition.SUNNY -> R.drawable.ic_weather_sunny
    WeatherCondition.WINDY -> R.drawable.ic_weather_windy
    WeatherCondition.WINDY_VARIANT -> R.drawable.ic_weather_windy_variant
}

@StringRes
private fun WeatherCondition.getTextRes(): Int = when (this) {
    WeatherCondition.CLEAR_NIGHT -> R.string.weatherClearNight
    WeatherCondition.CLOUDY -> R.string.weatherCloudy
    WeatherCondition.EXCEPTIONAL -> R.string.weatherExceptional
    WeatherCondition.FOG -> R.string.weatherFog
    WeatherCondition.HAIL -> R.string.weatherHail
    WeatherCondition.LIGHTNING -> R.string.weatherLightning
    WeatherCondition.LIGHTNING_RAINY -> R.string.weatherLightningRainy
    WeatherCondition.PARTLY_CLOUDY -> R.string.weatherPartlyCloudy
    WeatherCondition.POURING -> R.string.weatherPouring
    WeatherCondition.RAINY -> R.string.weatherRainy
    WeatherCondition.SNOWY -> R.string.weatherSnowy
    WeatherCondition.SNOWY_RAINY -> R.string.weatherSnowyRainy
    WeatherCondition.SUNNY -> R.string.weatherSunny
    WeatherCondition.WINDY -> R.string.weatherWindy
    WeatherCondition.WINDY_VARIANT -> R.string.weatherWindyVariant
}

@Preview
@Composable
fun WeatherItemPreview() {
    WeatherItemView(
        modifier = Modifier.background(Color.Gray),
        homePanel = Panel.HomePanel(), entityInteractor = mockEntityInteractor(
            LocalContext.current
        ),
        initState = WeatherStateView(
            WeatherEntityState(
                "weather.test",
                state = WeatherCondition.CLEAR_NIGHT
            )
        )
    )
}
