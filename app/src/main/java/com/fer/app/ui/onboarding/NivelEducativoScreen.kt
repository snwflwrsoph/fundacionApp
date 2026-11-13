package com.fer.app.ui.onboarding

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.model.NivelEducativo
import com.fer.app.ui.theme.FerCeleste
import com.fer.app.ui.theme.FerFontFamily
import com.fer.app.ui.theme.FerNavy

@Composable
fun NivelEducativoScreen(
    modifier: Modifier = Modifier,
    seleccionInicial: NivelEducativo? = null,
    onRegresar: () -> Unit = {},
    onContinuar: (NivelEducativo) -> Unit = {}
) {
    var nivelSeleccionado by remember {
        mutableStateOf<NivelEducativo?>(seleccionInicial)
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
                text = "Selecciona tu nivel educativo",
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
            item {
                OpcionCard(
                    titulo = "Preparatoria",
                    descripcion = "Accede a avisos, convocatorias y beneficios educativos para becarios FER.",
                    icono = R.drawable.backpack,
                    seleccionado = nivelSeleccionado == NivelEducativo.PREPARATORIA,
                    onClick = { nivelSeleccionado = NivelEducativo.PREPARATORIA }
                )
            }
            item {
                OpcionCard(
                    titulo = "Universidad",
                    descripcion = "Convocatorias y avisos para futuros becarios.",
                    icono = R.drawable.education,
                    seleccionado = nivelSeleccionado == NivelEducativo.UNIVERSIDAD,
                    onClick = { nivelSeleccionado = NivelEducativo.UNIVERSIDAD }
                )
            }
            item {
                OpcionCard(
                    titulo = "Maestría",
                    descripcion = "Convocatorias y avisos para futuros becarios.",
                    icono = R.drawable.airplane,
                    seleccionado = nivelSeleccionado == NivelEducativo.MAESTRIA,
                    onClick = { nivelSeleccionado = NivelEducativo.MAESTRIA }
                )
            }
        }

        // ---------- Botones ----------
        val esFinDeFlujo = nivelSeleccionado == NivelEducativo.PREPARATORIA || nivelSeleccionado == NivelEducativo.MAESTRIA

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonCircular(
                icono = FlechaIzquierdaIcon,
                descripcion = "Regresar",
                habilitado = true,
                onClick = onRegresar
            )

            BotonSiguienteFER(
                habilitado = nivelSeleccionado != null,
                esUltimoPaso = esFinDeFlujo,
                onClick = {
                    nivelSeleccionado?.let(onContinuar)
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
