package com.panelsense.domain.model.entity.state

data class BinarySensorEntityState(
    override val entityId: String,
    val state: Boolean,
    val deviceClass: DeviceClass?,
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId) {

    val mdiIcon: String
        get() = (DeviceClass.values()
            .firstOrNull { it.className == deviceClass?.className }
            ?: DeviceClass.UNDEFINED).run { if (state) iconOn else iconOff }

    @Suppress("StringLiteralDuplication")
    enum class DeviceClass(val className: String, val iconOn: String, val iconOff: String) {
        BATTERY("battery", "battery", "battery-outline"),
        BATTERY_CHARGING("battery_charging", "battery-charging", "battery"),
        CO("carbon_monoxide", "smoke-detector-alert", "smoke-detector"),
        COLD("cold", "snowflake", "thermometer"),
        CONNECTIVITY("connectivity", "check-network-outline", "close-network-outline"),
        DOOR("door", "door-open", "door-closed"),
        GARAGE_DOOR("garage_door", "garage-open", "garage"),
        GAS("gas", "alert-circle", "check-circle"),
        PROBLEM("problem", "alert-circle", "check-circle"),
        SAFETY("safety", "alert-circle", "check-circle"),
        TAMPER("tamper", "alert-circle", "check-circle"),
        SMOKE("smoke", "smoke-detector-variant-alert", "smoke-detector-variant"),
        HEAT("heat", "fire", "thermometer"),
        LIGHT("light", "brightness7", "brightness5"),
        LOCK("lock", "lock-open", "lock"),
        MOISTURE("moisture", "water", "water-off"),
        MOTION("motion", "motion-sensor", "motion-sensor-off"),
        OCCUPANCY("occupancy", "home", "home-outline"),
        OPENING("opening", "square-outline", "square"),
        PLUG("plug", "power-plug", "power-plug-off"),
        PRESENCE("presence", "home", "home-outline"),
        RUNNING("running", "play", "stop"),
        SOUND("sound", "music-note", "music-note-off"),
        UPDATE("update", "", ""),
        VIBRATION("vibration", "vibrate", "crop-portrait"),
        WINDOW("window", "window-open", "window-closed"),
        UNDEFINED("", "checkbox-marked-circle", "radiobox-blank"),
    }
}
