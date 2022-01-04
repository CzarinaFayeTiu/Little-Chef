package com.mobdeve.s12.avila.tiu.littlechef

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //when user taps on the notification what will appear
        val i = Intent(context, TimerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)


        val builder = NotificationCompat.Builder(context, "littlechef")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Times Up!!")
            .setContentText("Check your dish now!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)//when user taps on notification it calls pending intent
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(123, builder.build())

    }



}