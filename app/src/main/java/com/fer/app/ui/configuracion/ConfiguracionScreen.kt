package com.fer.app.ui.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.model.AjustesApp
import com.fer.app.ui.theme.FerTheme

@Composable
fun ConfiguracionScreen(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier,
    onNotificacionesChange: (Boolean) -> Unit = {},
    onModoOscuroChange: (Boolean) -> Unit = {},
    onLectorPantallaChange: (Boolean) -> Unit = {},
    onCompartirDatosChange: (Boolean) -> Unit = {},
    onTamanoTextoClick: () -> Unit = {},
    onPermisosClick: () -> Unit = {},
    onReportarProblemaClick: () -> Unit = {}
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
            Text(
                text = stringResource(R.string.titulo),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { SeccionTitulo(stringResource(R.string.seccion_preferencias)) }
            item {
                ItemSwitch(
                    titulo = stringResource(R.string.notificaciones_titulo),
                    subtitulo = stringResource(R.string.notificaciones_subtitulo),
                    checked = ajustes.notificaciones,
                    onCheckedChange = onNotificacionesChange
                )
            }
            item { Divisor() }
            item { SeccionTitulo(stringResource(R.string.seccion_accesibilidad)) }
            item {
                ItemSwitch(
                    titulo = stringResource(R.string.modo_oscuro_titulo),
                    subtitulo = stringResource(R.string.modo_oscuro_subtitulo),
                    checked = ajustes.modoOscuro,
                    onCheckedChange = onModoOscuroChange
                )
            }
            item { Divisor() }
            item {
                Item(
                    titulo = stringResource(R.string.tamano_texto_titulo),
                    subtitulo = stringResource(R.string.tamano_texto_subtitulo),
                    onClick = onTamanoTextoClick
                )
            }
            item { Divisor() }
            item {
                ItemSwitch(
                    titulo = stringResource(R.string.lector_pantalla_titulo),
                    subtitulo = stringResource(R.string.lector_pantalla_subtitulo),
                    checked = ajustes.lectorPantalla,
                    onCheckedChange = onLectorPantallaChange
                )
            }
            item { Divisor() }
            item { SeccionTitulo(stringResource(R.string.seccion_privacidad)) }
            item {
                ItemSwitch(
                    titulo = stringResource(R.string.compartir_datos_titulo),
                    subtitulo = stringResource(R.string.compartir_datos_subtitulo),
                    checked = ajustes.compartirDatos,
                    onCheckedChange = onCompartirDatosChange
                )
            }
            item { Divisor() }
            item {
                Item(
                    titulo = stringResource(R.string.permisos_titulo),
                    subtitulo = stringResource(R.string.permisos_subtitulo),
                    onClick = onPermisosClick
                )
            }
            item { Divisor() }
            item { SeccionTitulo(stringResource(R.string.seccion_ayuda)) }
            item {
                Text(
                    text = stringResource(R.string.reportar_problema),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onReportarProblemaClick() }
                        .padding(horizontal = 28.dp, vertical = 12.dp),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textDecoration = TextDecoration.Underline
                )
            }
            item { Divisor() }
        }
    }
}

@Composable
private fun SeccionTitulo(texto: String) {
    Text(
        text = texto,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 28.dp, end = 28.dp, top = 28.dp, bottom = 12.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun ItemSwitch(
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 28.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitulo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun Item(
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 28.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitulo,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun Divisor() {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ConfiguracionScreenPreview() {
    FerTheme {
        ConfiguracionScreen(ajustes = AjustesApp())
    }
}
