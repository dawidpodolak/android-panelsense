package com.panelsense.data.mapper

import com.panelsense.data.model.state.SwitchState
import com.panelsense.domain.model.entity.state.SwitchEntityState

fun SwitchState.toEntityState(): SwitchEntityState =
    SwitchEntityState(entityId)
