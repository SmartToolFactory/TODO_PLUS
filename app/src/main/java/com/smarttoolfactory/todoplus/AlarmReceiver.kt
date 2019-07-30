package com.smarttoolfactory.todoplus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm!!!", Toast.LENGTH_SHORT).show()
        setAlarmNotification(context)
    }

    private fun setAlarmNotification(context: Context) {
        val notificationId = Random().nextInt(1000)
        val CHANNEL_ID = "CHANNEL_ID$notificationId"

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarm")
            .setContentText("Alarm is ringing")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the intent that will fire when the user taps the notification
            //                        .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(alarmSound)

        val notificationManager = NotificationManagerCompat.from(context)

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build())
    }
}