package com.fer.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fer.app.notifications.FerMessagingService
import com.fer.app.ui.AppViewModel
import com.fer.app.ui.FerApp
import com.fer.app.ui.theme.FerTheme

class MainActivity : ComponentActivity() {
    private var anuncioPendiente by mutableStateOf<String?>(null)
    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FerMessagingService.asegurarCanal(this)
        if (savedInstanceState == null) {
            anuncioPendiente = extraerDestinoDeIntent(intent)
        }
        enableEdgeToEdge()
        setContent {
            val ajustes by viewModel.ajustes.collectAsState()
            FerTheme(
                darkTheme = ajustes.modoOscuro,
                tamanoTexto = ajustes.tamanoTexto
            ) {
                FerApp(
                    viewModel = viewModel,
                    anuncioIdPendiente = anuncioPendiente,
                    onAnuncioPendienteConsumido = { anuncioPendiente = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        extraerDestinoDeIntent(intent)?.let { anuncioPendiente = it }
    }

    private fun extraerDestinoDeIntent(intent: Intent?): String? {
        if (intent == null) return null

        FerMessagingService.extraerIdDeUri(intent.data)?.let { return it }

        val extras = intent.extras ?: return null
        val clavesId = listOf(EXTRA_ANUNCIO_ID, "anuncio_id", "announcementId", "announcement_id", "id")
        for (clave in clavesId) {
            val valor = extras.getString(clave)?.trim()
            if (!valor.isNullOrEmpty()) return valor
        }

        val link = extras.getString("link") ?: extras.getString("deepLink") ?: extras.getString("url")
        if (!link.isNullOrBlank()) {
            runCatching { Uri.parse(link) }.getOrNull()?.let { uri ->
                FerMessagingService.extraerIdDeUri(uri)?.let { return it }
            }
        }

        val titulo = extras.getString(EXTRA_ANUNCIO_TITULO)
            ?: extras.getString("titulo")
            ?: extras.getString("title")
            ?: extras.getString("gcm.notification.title")
        if (!titulo.isNullOrBlank()) {
            return "$PREFIJO_TITULO${titulo.trim()}"
        }

        val vieneDePushFcm = extras.containsKey("google.message_id") ||
            extras.containsKey("google.sent_time") ||
            extras.containsKey("from")
        if (vieneDePushFcm) {
            return DESTINO_RECIENTE
        }

        return null
    }

    companion object {
        const val EXTRA_ANUNCIO_ID = "anuncioId"
        const val EXTRA_ANUNCIO_TITULO = "anuncioTitulo"
        const val PREFIJO_TITULO = "titulo:"
        const val DESTINO_RECIENTE = "__reciente__"
    }
}
