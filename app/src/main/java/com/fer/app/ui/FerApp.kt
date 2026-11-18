package com.fer.app.ui

import android.app.Activity
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
import com.fer.app.ui.noticia.NewsDetailScreen
import com.fer.app.ui.onboarding.NivelEducativoScreen
import com.fer.app.ui.onboarding.PreferenciasScreen
import com.fer.app.ui.onboarding.SeleccionScreen
import com.fer.app.ui.components.EstadoCargaView
import com.fer.app.ui.notifications.NotificationScreen
import android.content.Intent
import android.provider.Settings
import com.fer.app.ui.configuracion.rememberLectorPantallaActivo
import com.fer.app.ui.noticia.agregarAlCalendario
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.fer.app.MainActivity
import com.fer.app.ui.configuracion.PermisosScreen
import com.fer.app.ui.configuracion.ReportarProblemaScreen
import com.fer.app.ui.configuracion.rememberNotificacionesPermitidas

@Composable
fun FerApp(
    viewModel: AppViewModel,
    anuncioIdPendiente: String? = null,
    onAnuncioPendienteConsumido: () -> Unit = {},
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val perfil by viewModel.perfil.collectAsState()
    val ajustes by viewModel.ajustes.collectAsState()
    val catalogo by viewModel.catalogo.collectAsState()
    val anuncios by viewModel.anuncios.collectAsState()
    val todosLosAnuncios by viewModel.todosLosAnuncios.collectAsState()
    val estadoAnuncios by viewModel.estadoAnuncios.collectAsState()
    val leidas by viewModel.notificacionesLeidas.collectAsState()
    val lectorPantallaActivo = rememberLectorPantallaActivo()
    val notificacionesPermitidas = rememberNotificacionesPermitidas()
    var esperandoActivarNotificaciones by rememberSaveable { mutableStateOf(false) }

    fun abrirAjustesDeNotificaciones() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            )
        }
        runCatching { context.startActivity(intent) }
    }

    val lanzadorPermisoNotificaciones =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
            if (concedido) {
                esperandoActivarNotificaciones = false
                viewModel.cambiarNotificaciones(true)
            } else if (esperandoActivarNotificaciones) {
                viewModel.cambiarNotificaciones(false)
                val actividad = context as? Activity
                val puedeMostrarDialogo = actividad != null &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        actividad,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                if (!puedeMostrarDialogo) {
                    Toast.makeText(
                        context,
                        "Activa las notificaciones en los ajustes del sistema",
                        Toast.LENGTH_SHORT
                    ).show()
                    abrirAjustesDeNotificaciones()
                } else {
                    esperandoActivarNotificaciones = false
                }
            }
        }

    LaunchedEffect(notificacionesPermitidas, esperandoActivarNotificaciones) {
        if (esperandoActivarNotificaciones && notificacionesPermitidas) {
            esperandoActivarNotificaciones = false
            viewModel.cambiarNotificaciones(true)
        }
    }

    fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            lanzadorPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Si la app se abrió desde una notificación push o deep link, navega directo al anuncio.
    LaunchedEffect(anuncioIdPendiente, perfil.onboardingCompleto, anuncios, todosLosAnuncios) {
        val destino = anuncioIdPendiente ?: return@LaunchedEffect
        if (!perfil.onboardingCompleto) return@LaunchedEffect

        val idResuelto = when {
            destino == MainActivity.DESTINO_RECIENTE -> {
                val candidato = anuncios.firstOrNull() ?: todosLosAnuncios.firstOrNull()
                candidato?.id ?: return@LaunchedEffect
            }
            destino.startsWith(MainActivity.PREFIJO_TITULO) -> {
                if (todosLosAnuncios.isEmpty()) return@LaunchedEffect
                val tituloBuscado = destino.removePrefix(MainActivity.PREFIJO_TITULO)
                val coincidencia = todosLosAnuncios.firstOrNull {
                    it.title.equals(tituloBuscado, ignoreCase = true)
                } ?: anuncios.firstOrNull() ?: todosLosAnuncios.firstOrNull()
                coincidencia?.id ?: return@LaunchedEffect
            }
            else -> destino
        }

        navController.navigate(Routes.detalle(idResuelto)) {
            launchSingleTop = true
        }
        onAnuncioPendienteConsumido()
    }

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
        pedirPermisoNotificaciones()

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
                    onNotificationsClick = { navController.navigate(Routes.NOTIFICACIONES) }
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

            composable(Routes.INICIO) {
                if (anuncios.isEmpty()) {
                    EstadoCargaView(
                        estado = estadoAnuncios,
                        textoVacio = "Todavía no hay anuncios publicados.",
                        onReintentar = viewModel::reintentarCargaAnuncios
                    )
                } else {
                    HomeScreen(
                        announcements = anuncios,
                        onAnnouncementClick = { navController.navigate(Routes.detalle(it.id)) },
                        onSaveClick = viewModel::alternarGuardado,
                        onSeeAllClick = { pantallaPendiente("Ver todo") }
                    )
                }
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
                    ?: todosLosAnuncios.firstOrNull { it.id == id }

                // Al abrir el detalle, su notificación deja de aparecer como nueva.
                LaunchedEffect(id) { viewModel.marcarLeida(id) }

                if (anuncio != null) {
                    NewsDetailScreen(
                        announcement = anuncio,
                        onBack = { navController.popBackStack() },
                        onLikeClick = { viewModel.alternarLike(id) },
                        onAddToCalendarClick = { context.agregarAlCalendario(anuncio) }
                    )
                } else {
                    // El anuncio se despublicó o ya no corresponde a la audiencia del usuario.
                    EstadoCargaView(
                        estado = estadoAnuncios,
                        textoVacio = "Este anuncio ya no está disponible.",
                        onReintentar = viewModel::reintentarCargaAnuncios
                    )
                }
            }
            composable(Routes.CONFIGURACION) {
                ConfiguracionScreen(
                    ajustes = ajustes.copy(
                        notificaciones = ajustes.notificaciones && notificacionesPermitidas
                    ),
                    onNotificacionesChange = { activo ->
                        if (!activo) {
                            esperandoActivarNotificaciones = false
                            viewModel.cambiarNotificaciones(false)
                        } else if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                            esperandoActivarNotificaciones = false
                            viewModel.cambiarNotificaciones(true)
                        } else {
                            esperandoActivarNotificaciones = true
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                lanzadorPermisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Activa las notificaciones en los ajustes del sistema",
                                    Toast.LENGTH_SHORT
                                ).show()
                                abrirAjustesDeNotificaciones()
                            }
                        }
                    },
                    onModoOscuroChange = viewModel::cambiarModoOscuro,
                    lectorPantallaActivo = lectorPantallaActivo,
                    onLectorPantallaClick = {
                        runCatching { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
                    },
                    onCompartirDatosChange = viewModel::cambiarCompartirDatos,
                    onTamanoTextoClick = { navController.navigate(Routes.TAMANO_TEXTO) },
                    onPermisosClick = { navController.navigate(Routes.PERMISOS) },
                    onReportarProblemaClick = { navController.navigate(Routes.REPORTAR_PROBLEMA) }
                )
            }
            composable(Routes.TAMANO_TEXTO) {
                TamanoTextoScreen(
                    seleccion = ajustes.tamanoTexto,
                    onSeleccionChange = viewModel::cambiarTamanoTexto,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.PERMISOS) {
                PermisosScreen(
                    notificacionesPermitidas = rememberNotificacionesPermitidas(),
                    onAbrirAjustesClick = ::abrirAjustesDeNotificaciones,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.REPORTAR_PROBLEMA) {
                ReportarProblemaScreen(
                    onEnviarReporte = { categoria, descripcion, contacto ->
                        viewModel.enviarReporteProblema(categoria, descripcion, contacto)
                        Toast.makeText(
                            context,
                            "Reporte enviado. ¡Gracias por tu ayuda!",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Routes.NOTIFICACIONES) {
                NotificationScreen(
                    anuncios = anuncios,
                    leidas = leidas,
                    onAnnouncementClick = { navController.navigate(Routes.detalle(it.id)) },
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

/** Navegación entre pestañas de la barra inferior (conserva el estado de cada una). */
private fun NavHostController.irAPestana(ruta: String) {
    navigate(ruta) {
        popUpTo(Routes.INICIO) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}