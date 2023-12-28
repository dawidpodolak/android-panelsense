package com.panelsense.domain

import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.ItemTypeDomain

val String.entityToDomain: EntityDomain?
    get() = kotlin.runCatching { EntityDomain.valueOf(this.substringBefore(".").uppercase()) }
        .getOrNull()

val String.typeToDomain: ItemTypeDomain?
    get() = kotlin.runCatching { ItemTypeDomain.valueOf(this.uppercase()) }
        .getOrNull()
