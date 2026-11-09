package com.fer.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import com.fer.app.R
import com.fer.app.model.TipoUsuario
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay

private val azulFER = Color(0xFF172958)
private val grisSeleccion = Color(0xFFD5D5D5)
private val azulClaro = Color(0xFF9FD5E8)

@Composable
fun PreferenciasScreen(
    modifier: Modifier = Modifier,
    seleccionInicial: TipoUsuario? = null,
    onContinuar: (TipoUsuario) -> Unit = {}
) {
    var seleccion by remember {
        mutableStateOf<TipoUsuario?>(null)
    }
    var confirmando by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(confirmando) {
        if (confirmando) {
            delay(600)
            onContinuar(TipoUsuario.ASPIRANTE)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Bienvenido",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = azulFER
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Selecciona quién eres",
            fontSize = 15.sp,
            color = azulFER
        )

        Spacer(
            modifier = Modifier
                .width(32.dp)
                .height(3.dp)
                .background(azulClaro)
        )

        Spacer(modifier = Modifier.height(150.dp))

        OpcionFER(
            titulo = "Becario",
            descripcion = "Accede a avisos, convocatorias y beneficios exclusivos para becarios FER.",
            icono = R.drawable.graduation,
            seleccionado = seleccion == TipoUsuario.BECARIO,
            onClick = { seleccion = TipoUsuario.BECARIO }
        )

        Spacer(modifier = Modifier.height(25.dp))

        OpcionFER(
            titulo = "Aspirante",
            descripcion = "Convocatorias y avisos para futuros becarios.",
            icono = R.drawable.binoculares,
            seleccionado = seleccion == TipoUsuario.ASPIRANTE,
            onClick = {
                if (!confirmando) {
                    seleccion = TipoUsuario.ASPIRANTE
                    confirmando = true
                }
            }
        )

        Spacer(modifier = Modifier.height(65.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            BotonSiguienteFER(
                habilitado = seleccion == TipoUsuario.BECARIO && !confirmando,
                onClick = {
                    seleccion?.let(onContinuar)
                }
            )
        }
    }
}

@Composable
private fun OpcionFER(
    titulo: String,
    descripcion: String,
    icono: Int,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 68.dp)
            .background(
                color = if (seleccionado)
                    grisSeleccion else Color.White,
                shape = RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    azulFER,
                    RoundedCornerShape(9.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icono),
                contentDescription = titulo,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = azulFER
            )

            Text(
                text = descripcion,
                fontSize = 8.sp,
                lineHeight = 11.sp,
                color = azulFER
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        if (seleccionado && titulo == "Aspirante") {
            Icon(
                imageVector = PalomitaIcon,
                contentDescription = "Selección confirmada",
                tint = azulFER,
                modifier = Modifier.size(32.dp)
            )
        } else {
            RadioButton(
                selected = seleccionado,
                onClick = onClick,
                modifier = Modifier.size(32.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor = azulFER,
                    unselectedColor = azulFER
                )
            )
        }
    }
}

