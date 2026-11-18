# APP FER

App Android (Kotlin + Jetpack Compose, Material 3) de la fundación. Muestra un feed de anuncios, convocatorias, eventos e historias para dos tipos de usuario: **becarios** y **aspirantes**.

## Mapa de navegación
- Primera sesión: Bienvenida (Becario/Aspirante) → Nivel educativo → (si Universidad) Pública/Privada → lista de universidades → Inicio.
  - Un aspirante salta directo a Inicio. Preparatoria y Maestría terminan el flujo sin pedir universidad.
- Barra inferior: Inicio (feed) · Guardados · Preferencias (reabre el flujo anterior) · Ajustes.
- Barra superior (Inicio y Guardados): búsqueda y notificaciones.
- Ajustes → Tamaño de texto. Notificaciones, modo oscuro, lector de pantalla y compartir datos son interruptores.
- Tocar un anuncio (Inicio, Guardados, Búsqueda o Notificaciones) abre el detalle con "me gusta". En anuncios de tipo evento también aparece "Agregar a calendario".
- La estrella de cada anuncio lo guarda y aparece en Guardados.

## Estructura
- `model/` datos de la app (enums y data classes).
- `data/` contratos (`Repositorios.kt`) y sus implementaciones:
  - `data/firebase/`: anuncios desde Firestore (`AnnouncementRepositoryFirestore`) y su conversión (`AnuncioMapper`).
  - `data/fake/`: perfil y ajustes en `SharedPreferences`, catálogo hardcodeado y un repositorio de anuncios de prueba (`FakeAnnouncements`).
- `ui/AppViewModel.kt` estado compartido; las pantallas solo reciben estado y callbacks.
- `ui/FerApp.kt` raíz de la app: barras y un solo `NavHost`.
- `ui/onboarding`, `ui/configuracion`, `ui/inicio`, `ui/noticia`, `ui/buscar`, `ui/notifications` pantallas; `ui/components` piezas reutilizables.
- `navigation/Routes.kt` rutas.

## De dónde salen los datos
- **Anuncios:** Firestore, colección `anuncios`, escuchados en tiempo real. La app inicia sesión anónima en Firebase Auth (debe estar activada) y solo lee los documentos con `estado == "publicado"`.
- **Perfil y ajustes:** `SharedPreferences` en el teléfono.
- **Guardados, "me gusta", calendario y notificaciones leídas:** `SharedPreferences` en el teléfono. Lo único que se escribe en Firestore es el contador `likes` del anuncio.
- **Catálogo de universidades:** hardcodeado en `CatalogoRepositoryFake`.

### Campos de un documento de `anuncios`
| Campo | Tipo | Notas |
|---|---|---|
| `titulo` | string | Obligatorio; sin título el anuncio se descarta |
| `resumen`, `descripcion`, `autor` | string | |
| `autorFotoUrl` | string | URL de la foto del autor |
| `imagenPrincipal` | string | URL |
| `imagenesSecundarias` | lista de strings | URLs |
| `fecha` | Timestamp | Si falta se usa `publicarEn`; sin ninguna, el anuncio va al final |
| `publicarEn` | Timestamp | Publicación programada: no se muestra hasta esa hora |
| `tags` | lista de strings | El primer tag define la categoría (aniversario, convocatoria, historia, alianza, evento; si no, general) |
| `audiencias` | lista de strings | Se muestra si está vacía, incluye "todos"/"general" o incluye la audiencia del usuario (becario/aspirante) |
| `prioridad` | string | `"urgente"` marca el anuncio como urgente |
| `isFeatured` | boolean | El primero destacado se muestra grande en Inicio |
| `likes` | número | |
| `estado` | string | Debe ser `"publicado"` |

## Trabajar sin Firebase
En `AppViewModel.Factory` cambia `AnnouncementRepositoryFirestore(application)` por `AnnouncementRepositoryFake()`. Las pantallas y la navegación no cambian.

## Pendiente
- "Ver todo" (Inicio), "Permisos" y "Reportar problema" (Ajustes) aún no tienen pantalla y solo muestran un aviso.
- Las notificaciones son los 30 anuncios más recientes; todavía no hay notificaciones push.
