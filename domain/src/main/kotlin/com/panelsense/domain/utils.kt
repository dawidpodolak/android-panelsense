package com.panelsense.domain

import com.panelsense.domain.model.EntityDomain

val String.toDomain: EntityDomain
    get() = EntityDomain.valueOf(this.substringBefore(".").uppercase())
