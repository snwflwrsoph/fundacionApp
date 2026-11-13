package com.fer.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.model.ID_UNIVERSIDAD_OTRA
import com.fer.app.model.OpcionSeleccion
import com.fer.app.model.TipoUniversidad
import com.fer.app.ui.theme.FerCeleste
import com.fer.app.ui.theme.FerFondoTarjetaSeleccionada
import com.fer.app.ui.theme.FerFontFamily
import com.fer.app.ui.theme.FerNavy
import com.fer.app.ui.theme.FerTheme

@Composable
fun SeleccionScreen(
    opciones: List<OpcionSeleccion>,
    onContinuar: (Int, String) -> Unit,
    onRegresar: () -> Unit,
    modifier: Modifier = Modifier,
    esUltimaPantalla: Boolean = false
) {
    var seleccionadoId by rememberSaveable { mutableStateOf<Int?>(null) }
    var otraUniversidadTexto by rememberSaveable { mutableStateOf("") }

    val puedeContinuar = when {
        seleccionadoId == null -> false
        seleccionadoId == ID_UNIVERSIDAD_OTRA -> otraUniversidadTexto.isNotBlank()
        else -> true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 40.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------- Encabezado ----------
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Bienvenido",
            fontFamily = FerFontFamily,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Selecciona quién eres",
                fontFamily = FerFontFamily,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(4.dp)
                    .background(FerCeleste, RoundedCornerShape(2.dp))
            )
        }

        // ---------- Tarjetas ----------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            items(opciones) { opcion ->
                val estaSeleccionado = seleccionadoId == opcion.id

                Column {
                    Card(
                        onClick = { seleccionadoId = opcion.id },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (estaSeleccionado) FerFondoTarjetaSeleccionada else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (estaSeleccionado)
                                    Modifier.border(1.5.dp, FerNavy, RoundedCornerShape(24.dp))
                                else Modifier
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = opcion.icono),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = opcion.titulo,
                                    fontFamily = FerFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = FerNavy
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = opcion.descripcion,
                                    fontFamily = FerFontFamily,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    color = FerNavy
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            RadioButton(
                                selected = estaSeleccionado,
                                onClick = { seleccionadoId = opcion.id },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = FerNavy,
                                    unselectedColor = FerNavy
                                )
                            )
                        }
                    }

                    if (estaSeleccionado && opcion.id == ID_UNIVERSIDAD_OTRA) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = otraUniversidadTexto,
                            onValueChange = { otraUniversidadTexto = it },
                            label = { Text("Nombre de tu universidad") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // ---------- Botones ----------
        val esFinDeFlujo = esUltimaPantalla || seleccionadoId == TipoUniversidad.PUBLICA.id

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            BotonCircular(
                icono = FlechaIzquierdaIcon,
                descripcion = "Regresar",
                habilitado = true,
                onClick = onRegresar
            )

            BotonSiguienteFER(
                habilitado = puedeContinuar,
                esUltimoPaso = esFinDeFlujo,
                onClick = {
                    seleccionadoId?.let { id ->
                        onContinuar(
                            id,
                            if (id == ID_UNIVERSIDAD_OTRA) otraUniversidadTexto else ""
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun BotonCircular(
    icono: ImageVector,
    descripcion: String,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = Modifier
            .size(56.dp)
            .background(if (habilitado) FerNavy else Color(0xFFD0D5E0), CircleShape)
    ) {
        Icon(imageVector = icono, contentDescription = descripcion, tint = Color.White)
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun SeleccionScreenPreview() {
    FerTheme {
        SeleccionScreen(
            opciones = listOf(
                OpcionSeleccion(1, "UDEM", "Convocatorias y avisos.", R.drawable.ic_universidad),
                OpcionSeleccion(2, "CETYS", "Convocatorias y avisos.", R.drawable.ic_universidad)
            ),
            onContinuar = { _, _ -> },
            onRegresar = {},
            esUltimaPantalla = true
        )
    }
}