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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fer.app.R
import com.fer.app.model.TipoUsuario
import com.fer.app.ui.theme.FerCeleste
import com.fer.app.ui.theme.FerFondoTarjetaSeleccionada
import com.fer.app.ui.theme.FerFontFamily
import com.fer.app.ui.theme.FerNavy

@Composable
fun PreferenciasScreen(
    modifier: Modifier = Modifier,
    seleccionInicial: TipoUsuario? = null,
    onContinuar: (TipoUsuario) -> Unit = {}
) {
    var seleccion by remember {
        mutableStateOf<TipoUsuario?>(seleccionInicial)
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

        // ---------- Tarjetas (centradas verticalmente) ----------
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            item {
                OpcionCard(
                    titulo = "Becario",
                    descripcion = "Accede a avisos, convocatorias y beneficios exclusivos para becarios FER.",
                    icono = R.drawable.graduation,
                    seleccionado = seleccion == TipoUsuario.BECARIO,
                    onClick = { seleccion = TipoUsuario.BECARIO }
                )
            }
            item {
                OpcionCard(
                    titulo = "Aspirante",
                    descripcion = "Convocatorias y avisos para futuros becarios.",
                    icono = R.drawable.binoculares,
                    seleccionado = seleccion == TipoUsuario.ASPIRANTE,
                    onClick = { seleccion = TipoUsuario.ASPIRANTE }
                )
            }
        }

        // ---------- Botones ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            BotonSiguienteFER(
                habilitado = seleccion != null,
                esUltimoPaso = seleccion == TipoUsuario.ASPIRANTE,
                onClick = {
                    seleccion?.let(onContinuar)
                }
            )
        }
    }
}

@Composable
fun OpcionCard(
    titulo: String,
    descripcion: String,
    icono: Int,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) FerFondoTarjetaSeleccionada else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (seleccionado)
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
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(FerNavy, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icono),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontFamily = FerFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = FerNavy
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = descripcion,
                    fontFamily = FerFontFamily,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    color = FerNavy
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            RadioButton(
                selected = seleccionado,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = FerNavy,
                    unselectedColor = FerNavy
                )
            )
        }
    }
}
