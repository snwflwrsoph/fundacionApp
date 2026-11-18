package com.fer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.fer.app.ui.AppViewModel
import com.fer.app.ui.FerApp
import com.fer.app.ui.theme.FerTheme
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    private var anuncioPendiente by mutableStateOf<String?>(null)
    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            anuncioPendiente = intent.getStringExtra(EXTRA_ANUNCIO_ID)
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
        intent.getStringExtra(EXTRA_ANUNCIO_ID)?.let { anuncioPendiente = it }
    }

    private companion object {
        const val EXTRA_ANUNCIO_ID = "anuncioId"
    }
}
