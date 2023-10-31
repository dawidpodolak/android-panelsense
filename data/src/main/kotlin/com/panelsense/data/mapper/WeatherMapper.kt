package com.panelsense.data.mapper

import com.panelsense.data.model.state.WeatherState
import com.panelsense.domain.model.entity.state.WeatherEntityState

fun WeatherState.toEntityState(): WeatherEntityState =
    WeatherEntityState(entityId)
