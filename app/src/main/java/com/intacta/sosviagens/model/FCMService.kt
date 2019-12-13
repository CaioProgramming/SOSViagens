package com.intacta.sosviagens.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.firebase.ui.auth.ui.email.RegisterEmailFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.intacta.sosviagens.R
import com.intacta.sosviagens.view.activities.Home

class FCMService:FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(RegisterEmailFragment.TAG, "From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(RegisterEmailFragment.TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("suggestion", true)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
            println("notification " + remoteMessage.notification!!.body!!)
            Log.d(RegisterEmailFragment.TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            val CHANNEL_ID = "sosmanager"
            //            Uri alarmSound = Uri.parse("android.resource://com.intactaengenharia.intacta/"+R.raw.notify);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val att = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                val name = "Intacta Engenharia"
                val description = "Canal de notificações intacta engenharia"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                channel.enableVibration(true)
                //                channel.setSound(alarmSound,att);

                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = getSystemService<NotificationManager>(NotificationManager::class.java!!)
                notificationManager.createNotificationChannel(channel)

            }
            val builder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentText(remoteMessage.notification!!.body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    //                        .setSound(alarmSound)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification!!.title))
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(this)

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(5, builder.build())


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

}