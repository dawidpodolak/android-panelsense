package com.panelsense.data.mapper

import com.panelsense.domain.model.entity.EntityFeature

fun <T : EntityFeature> Int?.toSupportedFeature(featureArray: Array<T>): Set<T> {
    if (this == null) return emptySet()
    val supportedFeatures = mutableSetOf<T>()
    featureArray.forEach {
        if (this.and(it.value) == it.value) {
            supportedFeatures.add(it)
        }
    }
    return supportedFeatures
}
