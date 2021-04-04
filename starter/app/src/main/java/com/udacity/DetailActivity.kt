package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        val extras = intent.extras
        extras?.let {
            Toast.makeText(applicationContext, extras.getString(Constants.FILE_NAME) + " status: " + extras.getString(Constants.STATUS), Toast.LENGTH_SHORT).show()
            notificationManager.cancel(extras.getInt(Constants.ID))
        }
    }

}
