package com.fer.app.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fer.app.model.NivelEducativo
import com.fer.app.model.Noticia
import com.fer.app.model.TipoUniversidad
import com.fer.app.navigation.Routes
import com.fer.app.ui.buscar.BuscarScreen
import com.fer.app.ui.components.AppBottomBar
import com.fer.app.ui.components.AppTopBar
import com.fer.app.ui.components.BottomDestination
import com.fer.app.ui.configuracion.ConfiguracionScreen
import com.fer.app.ui.configuracion.TamanoTextoScreen
import com.fer.app.ui.inicio.GuardadosScreen
import com.fer.app.ui.inicio.HomeScreen
import com.fer.app.ui.noticia.NoticiaScreen
import com.fer.app.ui.onboarding.NivelEducativoScreen
import com.fer.app.ui.onboarding.PreferenciasScreen
import com.fer.app.ui.onboarding.SeleccionScreen

/**
 * Raíz de la app: un solo NavHost con todas las pantallas.
 *
 * Mapa de navegación:
 *
 *   Primer arranque / pestaña "Preferencias":
 *     BIENVENIDA (Becario/Aspirante) -> NIVEL_EDUCATIVO
 *        |- Preparatoria o Maestría -> fin del flujo
 *        '- Universidad -> TIPO_UNIVERSIDAD (Pública/Privada) -> UNIVERSIDADES/{tipo} -> fin del flujo
 *
 *   Fin del flujo: primer arranque -> INICIO; si venía de la pestaña -> regresa a donde estaba.
 *
 *   App (barra inferior): INICIO (feed) | GUARDADOS | Preferencias (abre el flujo) | CONFIGURACION
 *     CONFIGURACION -> TIPO_USUARIO
 *     INICIO / GUARDADOS -> (tocar un anuncio) DETALLE/{id}
 */
@Composable
fun FerApp(viewModel: AppViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val perfil by viewModel.perfil.collectAsState()
    val ajustes by viewModel.ajustes.collectAsState()
    val catalogo by viewModel.catalogo.collectAsState()
    val anuncios by viewModel.anuncios.collectAsState()
    val noticias by viewModel.noticias.collectAsState()

    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route
    val mostrarBarraSuperior = rutaActual in Routes.CON_BARRA_SUPERIOR
    val mostrarBarraInferior = rutaActual in Routes.CON_BARRA_INFERIOR

    // La barra superior del feed es azul (íconos de estado claros); el resto de pantallas son claras.
    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as Activity).window
            WindowCompat.getInsetsController(ventana, vista).isAppearanceLightStatusBars = !mostrarBarraSuperior
        }
    }

    // Se calcula una sola vez: si ya hay perfil completo entra directo a Inicio.
    val destinoInicial = rememberSaveable {
        if (viewModel.perfil.value.onboardingCompleto) Routes.INICIO else Routes.BIENVENIDA
    }

    fun finalizarFlujo() {
        viewModel.completarOnboarding()

        navController.navigate(Routes.INICIO) {
            popUpTo(Routes.BIENVENIDA) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    fun pantallaPendiente(nombre: String = "Pantalla") {
        Toast.makeText(context, "$nombre pendiente", Toast.LENGTH_SHORT).show()
    }

    fun alSeleccionarPestana(destino: BottomDestination) {
        when (destino) {
            BottomDestination.HOME -> navController.irAPestana(Routes.INICIO)
            BottomDestination.SAVED -> navController.irAPestana(Routes.GUARDADOS)
            BottomDestination.SETTINGS -> navController.irAPestana(Routes.CONFIGURACION)
            // Preferencias no tiene contenido propio: reabre el flujo de preferencias.
            BottomDestination.PREFERENCES ->
                navController.navigate(Routes.BIENVENIDA) { launchSingleTop = true }
        }
    }

    val pestanaActual = when (rutaActual) {
        Routes.GUARDADOS -> BottomDestination.SAVED
        Routes.CONFIGURACION -> BottomDestination.SETTINGS
        else -> BottomDestination.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (mostrarBarraSuperior) {
                AppTopBar(
                    onSearchClick = { navController.navigate(Routes.BUSQUEDA) },
                    onNotificationsClick = { pantallaPendiente("Notificaciones") }
                )
            }
        },
        bottomBar = {
            if (mostrarBarraInferior) {
                AppBottomBar(selected = pestanaActual, onSelect = ::alSeleccionarPestana)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = destinoInicial,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ---------- Flujo de preferencias ----------
            composable(Routes.BIENVENIDA) {
                PreferenciasScreen(
                    seleccionInicial = perfil.tipoUsuario,
                    onContinuar = { tipo ->
                        viewModel.seleccionarTipoUsuario(tipo)

                        if (tipo.name == "ASPIRANTE") {
                            finalizarFlujo()
                        } else {
                            navController.navigate(Routes.NIVEL_EDUCATIVO)
                        }
                    }
                )
            }
            composable(Routes.NIVEL_EDUCATIVO) {
                NivelEducativoScreen(
                    seleccionInicial = perfil.nivelEducativo,
                    onRegresar = { navController.popBackStack() },
                    onContinuar = { nivel ->
                        viewModel.seleccionarNivel(nivel)
                        // Solo universidad pide elegir tipo y universidad (cambiar aquí si Maestría también debe).
                        if (nivel == NivelEducativo.UNIVERSIDAD) {
                            navController.navigate(Routes.TIPO_UNIVERSIDAD)
                        } else {
                            finalizarFlujo()
                        }
                    }
                )
            }
            composable(Routes.TIPO_UNIVERSIDAD) {
                SeleccionScreen(
                    opciones = catalogo.tiposUniversidad,
                    onRegresar = { navController.popBackStack() },
                    onContinuar = { id, _ ->
                        TipoUniversidad.deId(id)?.let { tipo ->
                            viewModel.seleccionarTipoUniversidad(tipo)

                            if (tipo == TipoUniversidad.PUBLICA) {
                                finalizarFlujo()
                            } else {
                                navController.navigate(Routes.universidades(tipo))
                            }
                        }
                    }
                )
            }
            composable(
                route = Routes.UNIVERSIDADES,
                arguments = listOf(navArgument(Routes.ARG_TIPO) { type = NavType.StringType })
            ) { entrada ->
                val tipo = entrada.arguments?.getString(Routes.ARG_TIPO)
                    ?.let { runCatching { TipoUniversidad.valueOf(it) }.getOrNull() }
                    ?: TipoUniversidad.PUBLICA
                SeleccionScreen(
                    opciones = catalogo.universidades[tipo].orEmpty(),
                    esUltimaPantalla = true,
                    onRegresar = { navController.popBackStack() },
                    onContinuar = { id, textoOtra ->
                        viewModel.seleccionarUniversidad(id, textoOtra)
                        finalizarFlujo()
                    }
                )
            }

            // ---------- App principal ----------
            composable(Routes.INICIO) {
                HomeScreen(
                    announcements = anuncios,
                    onAnnouncementClick = { navController.navigate(Routes.detalle(it.id)) },
                    onSaveClick = viewModel::alternarGuardado,
                    onSeeAllClick = { pantallaPendiente("Ver todo") }
                )
            }
            composable(Routes.GUARDADOS) {
                GuardadosScreen(
                    guardados = anuncios.filter { it.isSaved },
                    onAnnouncementClick = { navController.navigate(Routes.detalle(it.id)) },
                    onSaveClick = viewModel::alternarGuardado
                )
            }
            composable(
                route = Routes.DETALLE,
                arguments = listOf(navArgument(Routes.ARG_ID) { type = NavType.StringType })
            ) { entrada ->
                val id = entrada.arguments?.getString(Routes.ARG_ID).orEmpty()
                val anuncio = anuncios.firstOrNull { it.id == id }
                val noticia = noticias[id]

                // Pide el detalle al abrir (con backend aquí se haría la llamada).
                LaunchedEffect(id, anuncio != null) {
                    anuncio?.let(viewModel::cargarNoticia)
                }

                if (noticia != null) {
                    NoticiaScreen(
                        noticia = noticia,
                        onBack = { navController.popBackStack() },
                        onLikeClick = { viewModel.alternarLikeNoticia(id) },
                        onSaveClick = { viewModel.alternarGuardarCalendarioNoticia(id) }
                    )
                }
            }
            composable(Routes.CONFIGURACION) {
                ConfiguracionScreen(
                    ajustes = ajustes,
                    onNotificacionesChange = viewModel::cambiarNotificaciones,
                    onModoOscuroChange = viewModel::cambiarModoOscuro,
                    onLectorPantallaChange = viewModel::cambiarLectorPantalla,
                    onCompartirDatosChange = viewModel::cambiarCompartirDatos,
                    onTamanoTextoClick = { navController.navigate(Routes.TAMANO_TEXTO) },
                    // Estas dos no tienen pantalla diseñada todavía.
                    onPermisosClick = { pantallaPendiente() },
                    onReportarProblemaClick = { pantallaPendiente() }
                )
            }
            composable(Routes.TAMANO_TEXTO) {
                TamanoTextoScreen(
                    seleccion = ajustes.tamanoTexto,
                    onSeleccionChange = viewModel::cambiarTamanoTexto,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.BUSQUEDA) {
                BuscarScreen(
                    anuncios = anuncios,
                    onAnnouncementClick = { anuncio ->
                        navController.navigate(Routes.detalle(anuncio.id))
                    },
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = viewModel::alternarGuardado
                )
            }
        }
    }
}

/** Abre el menú de compartir de Android con el título y un extracto de la noticia. */
private fun compartir(context: Context, noticia: Noticia) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, noticia.titulo)
        putExtra(Intent.EXTRA_TEXT, "${noticia.titulo}\n\n${noticia.descripcion.take(200)}…")
    }
    context.startActivity(Intent.createChooser(intent, null))
}

/** Navegación entre pestañas de la barra inferior (conserva el estado de cada una). */
private fun NavHostController.irAPestana(ruta: String) {
    navigate(ruta) {
        popUpTo(Routes.INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
