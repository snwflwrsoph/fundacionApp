package com.fer.app.data.fake

import android.content.Context
import com.fer.app.data.AjustesRepository
import com.fer.app.data.AnnouncementRepository
import com.fer.app.data.PerfilRepository
import com.fer.app.model.AjustesApp
import com.fer.app.model.Announcement
import com.fer.app.model.NivelEducativo
import com.fer.app.model.PerfilUsuario
import com.fer.app.model.TamanoTexto
import com.fer.app.model.TipoUniversidad
import com.fer.app.model.TipoUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PerfilRepositoryPrefs(context: Context) : PerfilRepository {
    private val prefs = context.getSharedPreferences("fer_perfil_prefs", Context.MODE_PRIVATE)

    private val _perfil = MutableStateFlow(loadPerfil())
    override val perfil: StateFlow<PerfilUsuario> = _perfil.asStateFlow()

    private fun loadPerfil(): PerfilUsuario {
        val completo = prefs.getBoolean("onboarding_completo", false)
        val tipoUsuarioStr = prefs.getString("tipo_usuario", null)
        val nivelStr = prefs.getString("nivel_educativo", null)
        val tipoUniStr = prefs.getString("tipo_universidad", null)
        val uniId = prefs.getInt("universidad_id", -1).takeIf { it != -1 }
        val uniOtra = prefs.getString("universidad_otra", null)

        val tipoUsuario = tipoUsuarioStr?.let { runCatching { TipoUsuario.valueOf(it) }.getOrNull() }
        val nivel = nivelStr?.let { runCatching { NivelEducativo.valueOf(it) }.getOrNull() }
        val tipoUni = tipoUniStr?.let { runCatching { TipoUniversidad.valueOf(it) }.getOrNull() }

        return PerfilUsuario(
            onboardingCompleto = completo,
            tipoUsuario = tipoUsuario,
            nivelEducativo = nivel,
            tipoUniversidad = tipoUni,
            universidadId = uniId,
            universidadOtra = uniOtra
        )
    }

    override suspend fun guardar(perfil: PerfilUsuario) {
        _perfil.value = perfil
        prefs.edit().apply {
            putBoolean("onboarding_completo", perfil.onboardingCompleto)
            putString("tipo_usuario", perfil.tipoUsuario?.name)
            putString("nivel_educativo", perfil.nivelEducativo?.name)
            putString("tipo_universidad", perfil.tipoUniversidad?.name)
            putInt("universidad_id", perfil.universidadId ?: -1)
            putString("universidad_otra", perfil.universidadOtra)
            apply()
        }
    }
}

class AjustesRepositoryPrefs(context: Context) : AjustesRepository {
    private val prefs = context.getSharedPreferences("fer_ajustes_prefs", Context.MODE_PRIVATE)

    private val _ajustes = MutableStateFlow(loadAjustes())
    override val ajustes: StateFlow<AjustesApp> = _ajustes.asStateFlow()

    private fun loadAjustes(): AjustesApp {
        val notif = prefs.getBoolean("notificaciones", true)
        val oscuro = prefs.getBoolean("modo_oscuro", false)
        val lector = prefs.getBoolean("lector_pantalla", false)
        val compartir = prefs.getBoolean("compartir_datos", true)
        val tamanoStr = prefs.getString("tamano_texto", TamanoTexto.NORMAL.name)
        val tamano = tamanoStr?.let { runCatching { TamanoTexto.valueOf(it) }.getOrNull() } ?: TamanoTexto.NORMAL

        return AjustesApp(
            notificaciones = notif,
            modoOscuro = oscuro,
            lectorPantalla = lector,
            compartirDatos = compartir,
            tamanoTexto = tamano
        )
    }

    override suspend fun guardar(ajustes: AjustesApp) {
        _ajustes.value = ajustes
        prefs.edit().apply {
            putBoolean("notificaciones", ajustes.notificaciones)
            putBoolean("modo_oscuro", ajustes.modoOscuro)
            putBoolean("lector_pantalla", ajustes.lectorPantalla)
            putBoolean("compartir_datos", ajustes.compartirDatos)
            putString("tamano_texto", ajustes.tamanoTexto.name)
            apply()
        }
    }
}
