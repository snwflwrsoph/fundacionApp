package com.fer.app.data.fake

import com.fer.app.data.AjustesRepository
import com.fer.app.data.PerfilRepository
import com.fer.app.model.AjustesApp
import com.fer.app.model.PerfilUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Perfil en memoria (se pierde al cerrar la app). Siempre arranca sin onboarding completo. */
class PerfilRepositoryFake(inicial: PerfilUsuario = PerfilUsuario()) : PerfilRepository {
    private val _perfil = MutableStateFlow(inicial)
    override val perfil: StateFlow<PerfilUsuario> = _perfil.asStateFlow()

    override suspend fun guardar(perfil: PerfilUsuario) {
        _perfil.value = perfil
    }
}

/** Ajustes en memoria con los mismos valores iniciales que tenía la pantalla de Configuración. */
class AjustesRepositoryFake(inicial: AjustesApp = AjustesApp()) : AjustesRepository {
    private val _ajustes = MutableStateFlow(inicial)
    override val ajustes: StateFlow<AjustesApp> = _ajustes.asStateFlow()

    override suspend fun guardar(ajustes: AjustesApp) {
        _ajustes.value = ajustes
    }
}
