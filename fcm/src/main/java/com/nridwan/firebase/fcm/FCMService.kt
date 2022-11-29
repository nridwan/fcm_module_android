package com.nridwan.firebase.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nridwan.firebase.fcm.data.FCMNotifRequest
import kotlin.random.Random

abstract class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val request = processNotification(message)
        if (request != null) sendNotification(request)
    }

    abstract fun processNotification(raw: RemoteMessage): FCMNotifRequest?

    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        id: String,
        name: String,
        importance: Int? = null
    ): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                id, name, importance ?: NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }
        return id
    }

    open fun getOrCreateChannel(
        notificationManager: NotificationManager,
        request: FCMNotifRequest
    ): String {
        return createNotificationChannel(
            notificationManager,
            request.channelId,
            request.channelName
        )
    }

    abstract fun getPendingIntent(request: FCMNotifRequest): PendingIntent?

    open fun configureNotification(
        notificationBuilder: NotificationCompat.Builder,
        request: FCMNotifRequest
    ): NotificationCompat.Builder {
        return notificationBuilder
    }

    protected fun sendNotification(request: FCMNotifRequest) {
        val pendingIntent = getPendingIntent(request)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = configureNotification(
            NotificationCompat.Builder(this, getOrCreateChannel(notificationManager, request))
                .apply {
                    if (request.color != null)
                        color = request.color
                }
                .setSmallIcon(
                    if (request.smallIcon == 0)
                        android.R.drawable.sym_def_app_icon
                    else request.smallIcon
                )
                .setContentTitle(request.title)
                .setContentText(request.body)
                .setAutoCancel(true)
                .setSound(request.ringtone ?: defaultSoundUri)
                .setContentIntent(pendingIntent), request
        )

        notificationManager.notify(Random.nextInt(9999 - 1000) + 1000, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
    }
}