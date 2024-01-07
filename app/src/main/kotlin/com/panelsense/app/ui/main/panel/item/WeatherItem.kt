@file:Suppress("MatchingDeclarationName")

package com.panelsense.app.ui.main.panel.item

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.panelsense.app.R
import com.panelsense.app.ui.main.EntityInteractor
import com.panelsense.app.ui.main.panel.applyBackgroundForItem
import com.panelsense.app.ui.main.panel.getDrawable
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Companion.applySizeForRequestLayout
import com.panelsense.app.ui.main.panel.item.PanelItemLayoutRequest.Flex
import com.panelsense.app.ui.main.panel.mockEntityInteractor
import com.panelsense.app.ui.theme.FontStyleH1_SemiBold
import com.panelsense.app.ui.theme.FontStyleH3
import com.panelsense.app.ui.theme.FontStyleH4_SemiBold
import com.panelsense.app.ui.theme.FontStyleH6_SemiBold
import com.panelsense.app.ui.theme.FontStyleLarge_SemiBold
import com.panelsense.app.ui.theme.MdiIcons
import com.panelsense.domain.model.Panel
import com.panelsense.domain.model.PanelItem
import com.panelsense.domain.model.entity.state.WeatherEntityState
import com.panelsense.domain.model.entity.state.WeatherEntityState.WeatherCondition
import org.threeten.bp.format.DateTimeFormatter

data class WeatherStateView(
    val weatherState: WeatherEntityState? = null
)

@Composable
fun WeatherItemView(
    modifier: Modifier = Modifier,
    weatherEntity: String?,
    panelItem: PanelItem = PanelItem(entity = weatherEntity),
    entityInteractor: EntityInteractor,
    layoutRequest: PanelItemLayoutRequest = PanelItemLayoutRequest.Standard,
    initState: WeatherStateView = WeatherStateView()
) {
    var state by remember { mutableStateOf(initState) }
    WeatherItemLaunchEffect(weatherEntity = weatherEntity, entityInteractor) {
        state = it
    }
    if (state.weatherState == null) return
    Column(
        modifier
            .applyBackgroundForItem(panelItem, layoutRequest)
    ) {
        TodayWeatherView(
            modifier = Modifier
                .applySizeForRequestLayout(layoutRequest)
                .wrapContentHeight(),
            stateView = state,
            layoutRequest = layoutRequest,
            entityInteractor = entityInteractor
        )

        WeatherForecastView(
            modifier = Modifier
                .padding(top = 15.dp),
            weather = state.weatherState!!,
            entityInteractor = entityInteractor
        )
    }
}

@Composable
private fun TodayWeatherView(
    modifier: Modifier,
    stateView: WeatherStateView,
    layoutRequest: PanelItemLayoutRequest,
    entityInteractor: EntityInteractor,
) {
    val weatherState = stateView.weatherState ?: return
    ConstraintLayout(
        modifier
    ) {
        val state = weatherState.state ?: return@ConstraintLayout
        val (
            todayWeatherIcon,
            todayWeatherText,
            todayWeatherHumidity,
            todayWeatherPressure,
            todayWeatherWind,
            todayWeatherTemperature,
            todayWeatherTemperatureIcon,
        ) = createRefs()

        val temperature =
            weatherState.temperature?.run { "$this${weatherState.temperatureUnit ?: ""}" }
        val bottomBarrier =
            createBottomBarrier(todayWeatherHumidity, todayWeatherPressure, todayWeatherWind)
        val rightBarrier =
            createEndBarrier(todayWeatherHumidity, todayWeatherPressure, todayWeatherWind)

        Image(
            modifier = Modifier
                .constrainAs(todayWeatherIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            painter = rememberDrawablePainter(LocalContext.current.getDrawable(state.getIconRes())),
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
            var iconState by remember { mutableStateOf<Drawable?>(null) }
            LaunchedEffect(key1 = temperature) {
                iconState = entityInteractor.getDrawable(MdiIcons.THERMOMETER, Color.White)
            }
            Image(
                modifier = Modifier
                    .constrainAs(todayWeatherTemperatureIcon) {
                        end.linkTo(todayWeatherTemperature.start)
                        bottom.linkTo(todayWeatherTemperature.bottom)
                        top.linkTo(todayWeatherTemperature.top)
                    }
                    .run { if (layoutRequest is Flex) scale(1.5f, 1.5f) else this },
                painter = rememberDrawablePainter(drawable = iconState),
                contentDescription = temperature
            )

            Text(
                modifier = Modifier
                    .constrainAs(todayWeatherTemperature) {
                        if (layoutRequest is Flex) {
                            start.linkTo(rightBarrier, margin = 30.dp)
                            end.linkTo(parent.end)
                            end.linkTo(parent.end)
                            bottom.linkTo(bottomBarrier)
                            top.linkTo(todayWeatherHumidity.top)
                        } else {
                            start.linkTo(rightBarrier, margin = 30.dp)
                            bottom.linkTo(bottomBarrier)
                        }
                    },
                text = temperature,
                style = if (layoutRequest is Flex) FontStyleLarge_SemiBold else FontStyleH1_SemiBold,
                color = Color.White
            )
        }

        TodayWeatherAttributes(
            todayWeatherHumidity = todayWeatherHumidity,
            todayWeatherPressure = todayWeatherPressure,
            todayWeatherIcon = todayWeatherIcon,
            todayWeatherWind = todayWeatherWind,
            weatherState = weatherState,
            entityInteractor = entityInteractor
        )
    }
}

@Composable
private fun ConstraintLayoutScope.TodayWeatherAttributes(
    todayWeatherHumidity: ConstrainedLayoutReference,
    todayWeatherPressure: ConstrainedLayoutReference,
    todayWeatherIcon: ConstrainedLayoutReference,
    todayWeatherWind: ConstrainedLayoutReference,
    weatherState: WeatherEntityState,
    entityInteractor: EntityInteractor
) {
    WeatherAttributeView(
        modifier = Modifier
            .padding(top = 7.dp)
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

@Composable
private fun WeatherAttributeView(
    modifier: Modifier,
    attr: String?,
    mdiIconName: String,
    imageModifier: Modifier = Modifier,
    margin: Dp = 5.dp,
    textStyle: TextStyle = FontStyleH4_SemiBold,
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = imageModifier,
            painter = rememberDrawablePainter(drawable = iconState),
            contentDescription = attr
        )
        Spacer(modifier = Modifier.requiredWidth(margin))
        Text(text = attr, style = textStyle, color = Color.White)
    }
}

@Composable
fun WeatherItemLaunchEffect(
    weatherEntity: String?,
    entityInteractor: EntityInteractor,
    callback: (WeatherStateView) -> Unit
) = LaunchedEffect(key1 = weatherEntity) {
    val weatherState = entityInteractor.listenOnState(
        weatherEntity ?: return@LaunchedEffect,
        WeatherEntityState::class
    )
    weatherState.collect {
        callback(WeatherStateView(it))
    }
}


@Composable
fun WeatherForecastView(
    modifier: Modifier,
    weather: WeatherEntityState,
    entityInteractor: EntityInteractor
) {
    if (weather.forecast.isNullOrEmpty()) return

    LazyRow(modifier) {
        itemsIndexed(weather.forecast!!) { index, forecast ->
            ConstraintLayout {
                val (forecastItem, divider) = createRefs()
                WeatherForecastItemsView(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .widthIn(max = 80.dp)
                        .constrainAs(forecastItem) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    weather = weather,
                    forecast = forecast,
                    entityInteractor = entityInteractor
                )
                if (index != weather.forecast!!.size - 1) {
                    Spacer(modifier = Modifier
                        .requiredWidth(0.75.dp)
                        .background(Color.White)
                        .constrainAs(divider) {
                            top.linkTo(forecastItem.top)
                            bottom.linkTo(forecastItem.bottom)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Suppress("UnusedPrivateMember")
private fun WeatherForecastItemsView(
    modifier: Modifier,
    weather: WeatherEntityState,
    forecast: WeatherEntityState.WeatherForecastEntity,
    entityInteractor: EntityInteractor
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        forecast.datetime?.let {
            val text = it.format(DateTimeFormatter.ofPattern(FORECAST_DAY_FORMAT)) +
                    it.format(DateTimeFormatter.ofPattern(FORECAST_DATE_FORMAT))
            Text(text = text, style = FontStyleH6_SemiBold, color = Color.White)
        }

        forecast.condition?.let {
            Image(
                modifier = Modifier
                    .requiredWidth(40.dp)
                    .aspectRatio(1f),
                painter = rememberDrawablePainter(LocalContext.current.getDrawable(it.getIconRes())),
                contentDescription = stringResource(id = it.getTextRes())
            )
        }

        forecast.condition?.getTextRes()?.let {
            Text(
                text = stringResource(id = it),
                style = FontStyleH6_SemiBold,
                color = Color.White,
                maxLines = 2,
                minLines = 2,
                textAlign = TextAlign.Center
            )
        }

        forecast.temperature?.let { temperature ->
            Row {
                WeatherAttributeView(
                    modifier = Modifier,
                    attr = temperature.toString() + weather.temperatureUnit.orEmpty(),
                    mdiIconName = MdiIcons.THERMOMETER,
                    imageModifier = Modifier
                        .requiredWidth(12.dp)
                        .aspectRatio(1f),
                    margin = 2.dp,
                    textStyle = FontStyleH6_SemiBold,
                    entityInteractor = entityInteractor
                )
            }
        }

        forecast.humidity?.let { humidity ->
            Row {
                WeatherAttributeView(
                    modifier = Modifier,
                    attr = humidity.toString() + "%",
                    mdiIconName = MdiIcons.HUMIDITY,
                    imageModifier = Modifier
                        .requiredWidth(12.dp)
                        .aspectRatio(1f),
                    margin = 2.dp,
                    textStyle = FontStyleH6_SemiBold,
                    entityInteractor = entityInteractor
                )
            }
        }

        forecast.pressure?.let { pressure ->
            Row {
                WeatherAttributeView(
                    modifier = Modifier,
                    attr = pressure.toString() + weather.pressureUnit.orEmpty(),
                    mdiIconName = MdiIcons.GAUGE,
                    imageModifier = Modifier
                        .requiredWidth(12.dp)
                        .aspectRatio(1f),
                    margin = 2.dp,
                    textStyle = FontStyleH6_SemiBold,
                    entityInteractor = entityInteractor
                )
            }
        }

        forecast.windSpeed?.let { windSpeed ->
            Row {
                WeatherAttributeView(
                    modifier = Modifier,
                    attr = windSpeed.toString() + weather.windSpeedUnit.orEmpty(),
                    mdiIconName = MdiIcons.WIND,
                    imageModifier = Modifier
                        .requiredWidth(12.dp)
                        .aspectRatio(1f),
                    margin = 2.dp,
                    textStyle = FontStyleH6_SemiBold,
                    entityInteractor = entityInteractor
                )
            }
        }
    }
}

const val FORECAST_DAY_FORMAT = "EEE"
const val FORECAST_DATE_FORMAT = "dd/MM"

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
        weatherEntity = Panel.HomePanel().weatherEntity, entityInteractor = mockEntityInteractor(
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
