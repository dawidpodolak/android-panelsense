package com.panelsense.app.ui.main

import com.panelsense.data.icons.IconProvider
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface EntityInteractor {

    fun <T : EntityState> listenOnState(entityId: String, kType: KClass<T>): Flow<T>

    fun sendCommand(command: EntityCommand)

    fun getIconProvider(): IconProvider
}
