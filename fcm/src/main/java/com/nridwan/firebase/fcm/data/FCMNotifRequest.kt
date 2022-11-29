package com.nridwan.firebase.fcm.data

import android.net.Uri
import androidx.annotation.DrawableRes

class FCMNotifRequest(
    val title: String,
    val body: String,
    val channelId: String,
    val channelName: String,
    @DrawableRes val smallIcon: Int = 0,
    val color: Int? = null,
    val ringtone: Uri? = null,
    val extra: Any? = null)