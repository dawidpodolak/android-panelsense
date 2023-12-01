package com.panelsense.data.model.state

data class CoverState(
    val entityId: String,
    val state: String?,
    val position: Int?,
    val tiltPosition: Int?,
    val icon: String?,
    val friendlyName: String?,
    val deviceClass: String?,
    val supportedFeatures: Int?
)
