package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat

fun NotificationManager.sendNotification(id: Int, messageBody: String, detailsBundle: Bundle, applicationContext: Context) {
    //Normal click action opens MainActivity
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        id,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    //Changes action opens details
    val detailsIntent = Intent(applicationContext, DetailActivity::class.java)
    detailsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    detailsIntent.putExtras(detailsBundle)

    val detailsPendingIntent = PendingIntent.getActivity(
        applicationContext,
        id,
        detailsIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.download_notification_channel_id))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true) //dismiss when click
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            detailsPendingIntent
        )

    //Show
    notify(id, builder.build())
}