package com.panelsense.data.mapper

import com.panelsense.data.model.state.CoverState
import com.panelsense.domain.model.entity.state.CoverEntityState

fun CoverState.toEntityState(): CoverEntityState =
    CoverEntityState(entityId)
