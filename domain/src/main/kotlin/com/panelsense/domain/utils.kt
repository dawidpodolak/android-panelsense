package com.panelsense.domain

import com.panelsense.domain.model.EntityDomain

val String.toDomain: EntityDomain
    get() = kotlin.runCatching { EntityDomain.valueOf(this.substringBefore(".").uppercase()) }
        .getOrElse { EntityDomain.UNKNOWN }
