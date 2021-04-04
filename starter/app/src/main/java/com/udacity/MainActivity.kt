package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.buttonState = ButtonState.Completed

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            id?.let {
                val bundle = Bundle()
                bundle.putInt(Constants.ID, it.toInt())

                //Get the current selection
                val radioButtonID: Int = radioGroup.checkedRadioButtonId
                val radioButton: View? = radioGroup.findViewById(radioButtonID)

                //Get the downloaded file name
                val fileName = when(radioGroup.indexOfChild(radioButton)) {
                    0 -> getString(R.string.selection_glide)
                    1 -> getString(R.string.selection_app)
                    2 -> getString(R.string.selection_retrofit)
                    else -> ""
                }
                bundle.putString(Constants.FILE_NAME, fileName)

                //Get the status of the download
                val query = DownloadManager.Query().apply {
                    setFilterById(it)
                }

                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            bundle.putString(Constants.STATUS, getString(R.string.download_success))
                        }
                        DownloadManager.STATUS_FAILED -> {
                            bundle.putString(Constants.STATUS, getString(R.string.download_failed))
                        }
                    }
                } else {
                    bundle.putString(Constants.STATUS, getString(R.string.download_failed))
                }

                //Get the notification description
                val notificationDescription = when(radioGroup.indexOfChild(radioButton)) {
                    0 -> getString(R.string.notification_description_glide)
                    1 -> getString(R.string.notification_description_app)
                    2 -> getString(R.string.notification_description_retrofit)
                    else -> getString(R.string.notification_description)
                }

                //Create the notification
                val notificationManager = ContextCompat.getSystemService(
                    applicationContext,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    it.toInt(),
                    notificationDescription,
                    bundle,
                    applicationContext
                )
            }
        }
    }

    private fun download() {
        val radioButtonID: Int = radioGroup.checkedRadioButtonId
        val radioButton: View? = radioGroup.findViewById(radioButtonID)
        val url = when(radioGroup.indexOfChild(radioButton)) {
            0 -> URLGlide
            1 -> URLApp
            2 -> URLRetrofit
            else -> ""
        }

        if(url.isNotEmpty()) {
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.

            custom_button.buttonState = ButtonState.Loading
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_no_selection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = getColor(R.color.colorPrimary)
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.download_notification_channel_description)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val URLApp =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URLGlide =
            "https://github.com/bumptech/glide"
        private const val URLRetrofit =
            "https://github.com/square/retrofit"
    }

}
