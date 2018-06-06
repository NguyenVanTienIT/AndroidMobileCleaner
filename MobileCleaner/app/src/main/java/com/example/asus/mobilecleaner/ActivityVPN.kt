package com.example.asus.mobilecleaner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner

class ActivityVPN : AppCompatActivity() {

    var spinder : Spinner? = null
    var imgStatus : ImageView? = null
    var btnConnection : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn)

        spinder = findViewById(R.id.option_VPN)
        imgStatus = findViewById(R.id.img_status)
        btnConnection = findViewById(R.id.connection)




    }
}
