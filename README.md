# APP FER

## Mapa de navegación
- Primera sesión: Bienvenida (Becario/Aspirante) → Nivel educativo → (si Universidad) Pública/Privada → lista de universidades → Inicio.
- Barra inferior: Inicio (feed) · Guardados · Preferencias (reabre el flujo anterior) · Ajustes → Tipo de usuario.
- Tocar un anuncio (Inicio o Guardados) abre la noticia con su detalle, "me gusta" y compartir.
- La estrella de cada anuncio lo guarda (solo en memoria) y aparece en Guardados.

## Estructura
- `model/` datos de la app (enums y data classes).
- `data/` contratos (`Repositorios.kt`) y su implementación hardcodeada en `data/fake/`.
- `ui/AppViewModel.kt` estado compartido; las pantallas solo reciben estado y callbacks.
- `ui/onboarding`, `ui/configuracion`, `ui/inicio` pantallas.
- `navigation/Routes.kt` rutas.

## Conectar el backend después
1. Crear implementaciones de `PerfilRepository`, `AjustesRepository`, `CatalogoRepository`, `AnnouncementRepository` y `NoticiaRepository` (Firebase)
2. Cambiarlas en `AppViewModel.Factory`.
(También `AnnouncementRepository` para el feed.) Las pantallas y la navegación no cambian.
