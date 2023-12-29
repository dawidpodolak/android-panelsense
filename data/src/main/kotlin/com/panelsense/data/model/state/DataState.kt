package com.panelsense.data.model.state

import com.panelsense.domain.model.entity.state.EntityState

interface DataState {
    fun toDomainState(): EntityState
}
