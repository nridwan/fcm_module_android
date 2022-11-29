package com.nridwan.firebaseexample.firebase.impl

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.google.firebase.messaging.RemoteMessage
import com.nridwan.firebaseexample.MainActivity
import com.nridwan.firebaseexample.R
import com.nridwan.firebase.fcm.FCMService
import com.nridwan.firebase.fcm.data.FCMNotifRequest

class FcmServiceImpl : FCMService() {
    companion object {
        const val TAG = "FCMService"
    }

    val channelId by lazy { getString(R.string.default_notification_channel_id) }
    val channelName by lazy { getString(R.string.default_notification_channel_name) }
    val smallIcon = R.drawable.ic_notifications

    override fun processNotification(raw: RemoteMessage): FCMNotifRequest? {
        if (raw.data["title"] != null) {
            return FCMNotifRequest(
                title = raw.data["title"] ?: "",
                body = raw.data["body"] ?: "",
                channelId = channelId,
                channelName = channelName,
                smallIcon = smallIcon
            )
        }
        if (raw.notification != null) {
            return FCMNotifRequest(
                title = raw.notification?.title ?: "",
                body = raw.notification?.body ?: "",
                channelId = channelId,
                channelName = channelName,
                smallIcon = smallIcon
            )
        }
        return null
    }

    override fun getPendingIntent(request: FCMNotifRequest): PendingIntent? {
        return PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_MUTABLE
            else PendingIntent.FLAG_ONE_SHOT
        )
    }

    override fun onNewToken(token: String) {
    }
}