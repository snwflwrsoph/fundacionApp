package com.fer.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.fer.app.MainActivity
import com.fer.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FerMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token FCM registrado: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val prefs = getSharedPreferences("fer_ajustes_prefs", Context.MODE_PRIVATE)
        val notificacionesActivas = prefs.getBoolean("notificaciones", true)
        if (!notificacionesActivas) {
            Log.d(TAG, "Notificación ignorada: desactivadas en ajustes del usuario")
            return
        }

        val data = message.data
        val anuncioId = extraerIdDeMapa(data)
        val titulo = message.notification?.title
            ?: data["titulo"]
            ?: data["title"]
            ?: getString(R.string.app_name)
        val cuerpo = message.notification?.body
            ?: data["resumen"]
            ?: data["descripcion"]
            ?: data["body"]
            ?: data["mensaje"]
            ?: "Tienes un nuevo anuncio en FER"

        mostrarNotificacion(
            context = this,
            titulo = titulo,
            cuerpo = cuerpo,
            anuncioId = anuncioId
        )
    }

    companion object {
        private const val TAG = "FER_FCM"
        const val CANAL_ID = "fer_anuncios"
        private const val CANAL_NOMBRE = "Anuncios y convocatorias"
        private const val CANAL_DESCRIPCION = "Avisos de nuevos anuncios, eventos y convocatorias de FER"

        fun asegurarCanal(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (manager.getNotificationChannel(CANAL_ID) == null) {
                    val canal = NotificationChannel(
                        CANAL_ID,
                        CANAL_NOMBRE,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = CANAL_DESCRIPCION
                    }
                    manager.createNotificationChannel(canal)
                }
            }
        }

        fun extraerIdDeMapa(data: Map<String, String>): String? {
            val clavesId = listOf("anuncioId", "anuncio_id", "announcementId", "announcement_id", "id")
            for (clave in clavesId) {
                val valor = data[clave]?.trim()
                if (!valor.isNullOrEmpty()) return valor
            }
            val link = data["link"] ?: data["deepLink"] ?: data["url"]
            if (!link.isNullOrBlank()) {
                runCatching { Uri.parse(link) }.getOrNull()?.let { uri ->
                    extraerIdDeUri(uri)?.let { return it }
                }
            }
            return null
        }

        fun extraerIdDeUri(uri: Uri?): String? {
            if (uri == null) return null
            val queryId = uri.getQueryParameter("anuncioId")
                ?: uri.getQueryParameter("anuncio_id")
                ?: uri.getQueryParameter("announcementId")
                ?: uri.getQueryParameter("id")
            if (!queryId.isNullOrBlank()) return queryId.trim()

            val segmentos = uri.pathSegments.orEmpty().filter { it.isNotBlank() }
            if (uri.scheme == "fer") {
                val host = uri.host?.lowercase()
                if ((host == "anuncio" || host == "detalle" || host == "anuncios") && segmentos.isNotEmpty()) {
                    return segmentos.last()
                }
            }
            val indice = segmentos.indexOfFirst {
                val s = it.lowercase()
                s == "anuncio" || s == "anuncios" || s == "detalle"
            }
            if (indice >= 0 && indice + 1 < segmentos.size) {
                return segmentos[indice + 1]
            }
            return null
        }

        fun mostrarNotificacion(
            context: Context,
            titulo: String,
            cuerpo: String,
            anuncioId: String?
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            asegurarCanal(context)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                if (!anuncioId.isNullOrBlank()) {
                    putExtra(MainActivity.EXTRA_ANUNCIO_ID, anuncioId)
                    data = Uri.parse("fer://anuncio/${Uri.encode(anuncioId)}")
                }
                putExtra(MainActivity.EXTRA_ANUNCIO_TITULO, titulo)
            }

            val requestCode = (anuncioId ?: titulo).hashCode()
            val pendingIntent = PendingIntent.getActivity(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificacion = NotificationCompat.Builder(context, CANAL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setStyle(NotificationCompat.BigTextStyle().bigText(cuerpo))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            NotificationManagerCompat.from(context).notify(requestCode, notificacion)
        }
    }
}
