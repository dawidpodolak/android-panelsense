package com.panelsense.data.model.state

data class SwitchState(
    val entityId: String,
    val on: Boolean,
    val friendlyName: String?,
    val icon: String?
)
