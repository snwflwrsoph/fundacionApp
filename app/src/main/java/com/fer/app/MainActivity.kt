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

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val ajustes by viewModel.ajustes.collectAsState()
            FerTheme(
                darkTheme = ajustes.modoOscuro,
                tamanoTexto = ajustes.tamanoTexto
            ) {
                FerApp(viewModel)
            }
        }
    }
}
