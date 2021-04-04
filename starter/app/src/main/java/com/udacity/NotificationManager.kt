package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat

fun NotificationManager.sendNotification(id: Int, messageBody: String, detailsBundle: Bundle, applicationContext: Context) {
    //Intent opens details with attributes
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
        .setContentIntent(detailsPendingIntent)
        .setAutoCancel(true) //dismiss when click
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            detailsPendingIntent
        )

    //Show
    notify(id, builder.build())
}