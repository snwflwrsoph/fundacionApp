package com.fer.app.ui.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.model.AjustesApp
import com.fer.app.model.TamanoTexto
import com.fer.app.ui.theme.FerTheme

@Composable
fun TamanoTextoScreen(
    seleccion: TamanoTexto,
    onSeleccionChange: (TamanoTexto) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colorResource(R.color.fondo_boton_atras),
                    contentColor = colorResource(R.color.azul_principal)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.regresar)
                )
            }
            Text(
                text = "Tamaño de texto",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Text(
            text = "Selecciona el tamaño de letra para la aplicación",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 12.dp),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        TamanoTexto.entries.forEach { tamano ->
            ItemOpcionTamano(
                titulo = tamano.label,
                subtitulo = when (tamano) {
                    TamanoTexto.CHICO -> "Texto más compacto"
                    TamanoTexto.NORMAL -> "Tamaño estándar recomendado"
                    TamanoTexto.GRANDE -> "Texto más grande y accesible"
                },
                seleccionado = seleccion == tamano,
                onClick = { onSeleccionChange(tamano) }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun ItemOpcionTamano(
    titulo: String,
    subtitulo: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitulo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        RadioButton(
            selected = seleccionado,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TamanoTextoScreenPreview() {
    FerTheme {
        TamanoTextoScreen(seleccion = TamanoTexto.NORMAL, onSeleccionChange = {})
    }
}
