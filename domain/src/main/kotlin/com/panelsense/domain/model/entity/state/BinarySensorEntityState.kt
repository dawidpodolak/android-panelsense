package com.panelsense.domain.model.entity.state

data class BinarySensorEntityState(
    override val entityId: String,
    val state: Boolean,
    val deviceClass: DeviceClass?,
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId) {

    enum class DeviceClass {
        BATTERY,
        BATTERY_CHARGING,
        CO,
        COLD,
        CONNECTIVITY,
        DOOR,
        GARAGE_DOOR,
        GAS,
        HEAT,
        LIGHT,
        LOCK,
        MOISTURE,
        MOTION,
        MOVING,
        OCCUPANCY,
        OPENING,
        PLUG,
        POWER,
        PRESENCE,
        PROBLEM,
        RUNNING,
        SAFETY,
        SMOKE,
        SOUND,
        TAMPER,
        UPDATE,
        VIBRATION,
        WINDOW,
    }
}
