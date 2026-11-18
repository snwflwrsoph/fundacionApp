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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.ui.theme.FerTheme

enum class CategoriaProblema(val titulo: String, val subtitulo: String) {
    FALLO_APP(
        titulo = "Fallo en la aplicación",
        subtitulo = "La app se cierra, tarda en cargar o algo no funciona"
    ),
    CONTENIDO(
        titulo = "Problema con un anuncio",
        subtitulo = "Información incorrecta, enlaces rotos o imágenes"
    ),
    NOTIFICACIONES(
        titulo = "Notificaciones o calendario",
        subtitulo = "No llegan avisos o no se agendan eventos"
    ),
    OTRO(
        titulo = "Otro",
        subtitulo = "Sugerencia o cualquier otro inconveniente"
    )
}

@Composable
fun ReportarProblemaScreen(
    onEnviarReporte: (categoria: String, descripcion: String, contacto: String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var categoriaSeleccionada by rememberSaveable { mutableStateOf(CategoriaProblema.FALLO_APP) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var contacto by rememberSaveable { mutableStateOf("") }

    val puedeEnviar = descripcion.isNotBlank()

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
                text = stringResource(R.string.reportar_problema),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Selecciona el tipo de problema y descríbelo para que podamos ayudarte.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 12.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            CategoriaProblema.entries.forEach { categoria ->
                ItemCategoriaProblema(
                    titulo = categoria.titulo,
                    subtitulo = categoria.subtitulo,
                    seleccionado = categoriaSeleccionada == categoria,
                    onClick = { categoriaSeleccionada = categoria }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Describe el problema") },
                    placeholder = { Text("Escribe los detalles de lo que ocurrió...") },
                    minLines = 4,
                    maxLines = 8,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contacto,
                    onValueChange = { contacto = it },
                    label = { Text("Correo de contacto (opcional)") },
                    placeholder = { Text("ejemplo@correo.com") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        onEnviarReporte(
                            categoriaSeleccionada.titulo,
                            descripcion.trim(),
                            contacto.trim()
                        )
                    },
                    enabled = puedeEnviar,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Enviar reporte",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemCategoriaProblema(
    titulo: String,
    subtitulo: String,
    seleccionado: Boolean,
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
private fun ReportarProblemaScreenPreview() {
    FerTheme {
        ReportarProblemaScreen(onEnviarReporte = { _, _, _ -> })
    }
}
