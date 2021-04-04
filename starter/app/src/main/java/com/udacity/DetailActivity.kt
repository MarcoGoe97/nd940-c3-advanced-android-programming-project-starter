package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        extras?.let {
            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.cancel(extras.getInt(Constants.ID))

            tvFileNameDescription.text = extras.getString(Constants.FILE_NAME)
            tvStatusDescription.text = extras.getString(Constants.STATUS)

            when(extras.getString(Constants.STATUS)) {
                getString(R.string.download_success) -> {
                    tvStatusDescription.setTextColor(resources.getColor(R.color.green))
                }
                else -> {
                    tvStatusDescription.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                }
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

}
