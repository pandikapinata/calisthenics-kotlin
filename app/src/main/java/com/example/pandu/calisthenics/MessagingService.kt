package com.example.pandu.calisthenics

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.pandu.calisthenics.utils.PreferenceHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor


@Suppress("DEPRECATION")
class MessagingService : FirebaseMessagingService() {
    private var preferencesHelper: PreferenceHelper? = null

    override fun onNewToken(s: String?) {

        preferencesHelper = PreferenceHelper(this)

        preferencesHelper?.setFCM(s)
    }

    @SuppressLint("ServiceCast")
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val message = remoteMessage?.notification?.body
        val title = remoteMessage?.notification?.title
//        val title = remoteMessage?.data?.get("title")
//        val body = remoteMessage?.data?.get("body")
        val room = remoteMessage?.data?.get("room")

        startActivity(intentFor<MainActivity>().clearTop())

        val pendingIntent : PendingIntent? = PendingIntent.getActivity(this, 0, intentFor<MainActivity>(), PendingIntent.FLAG_ONE_SHOT)

        val sound : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this, "my_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(android.app.Notification.PRIORITY_HIGH)
            .setSound(sound)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = notificationBuilder.build()
        notificationManager.notify(0, notification)
    }
}