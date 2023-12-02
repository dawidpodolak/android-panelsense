@file:Suppress("MatchingDeclarationName")
package com.panelsense.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)

annotation class DataStoreType(val type: Type) {
    enum class Type {
        SvgImage, AppData
    }
}
