package com.fer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.fer.app.ui.AppViewModel
import com.fer.app.ui.FerApp
import com.fer.app.ui.theme.FerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AppViewModel by viewModels { AppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FerTheme {
                FerApp(viewModel)
            }
        }
    }
}
