package com.example.asus.mobilecleaner

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.widget.Button
import com.example.asus.mobilecleaner.R.id.toolbar
import kotlinx.android.synthetic.main.fragment_home.*
import android.content.Intent
import android.net.Uri


class MainActivity :  NavigationView.OnNavigationItemSelectedListener, AbsRuntimePermission() {


    var btnMenu : Button? = null
    companion object {
        private val REQUEST_PERMISSION = 10
        private val MY_REQUEST_CODE = 1
        var send = false



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)
        requestAppPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET),
                R.string.msg, REQUEST_PERMISSION)

        var fragment : FragmentHome = FragmentHome()

        supportFragmentManager.beginTransaction().add(R.id.container_home, fragment).commit()

        //supportFragmentManager.beginTransaction().add(R.id.container_home, FragmentHome()).commit()

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)

        toggle.syncState()

       /* if(FragmentHome.status == true)
            toolbar.setBackgroundColor(Color.parseColor("#0C2C43"))
        else{
            toolbar.setBackgroundColor(Color.parseColor("#0C2C43"))
        }*/
        nav_view.setNavigationItemSelectedListener(this)

    }


    override fun onPermissionsGranted(requestCode: Int) {

    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if(send){
            Toast.makeText(applicationContext, "Send complete", Toast.LENGTH_SHORT).show()
            send = false
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.feed_back -> {
               // Toast.makeText(applicationContext, "feedback selected", Toast.LENGTH_SHORT).show()
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@misskinglimited.com"))
                email.putExtra(Intent.EXTRA_SUBJECT, "Contact Us")
                email.putExtra(Intent.EXTRA_TEXT, "sent a message using the contact us ")

                email.type = "message/rfc822"
                startActivityForResult(Intent.createChooser(email, "Choose an Email client:"), 1);
                send = true
            }
            R.id.rate_app-> {
                //Toast.makeText(applicationContext, "Rate app selected", Toast.LENGTH_SHORT).show()
                val appPackageName = packageName // getPackageName() from Context or Activity object
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }

            }
            R.id.setting -> {
                //Toast.makeText(applicationContext, "Setting selected", Toast.LENGTH_SHORT).show()
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://misskinglimited.com"))
                startActivity(browserIntent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
