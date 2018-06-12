package com.example.asus.mobilecleaner

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
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var btnMenu : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)



        var fragment : FragmentHome = FragmentHome()

        supportFragmentManager.beginTransaction().add(R.id.container_home, fragment).commit()
        /*FragmentHome.openNavigation(fragment)!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(applicationContext, "Hello", Toast.LENGTH_SHORT).show()
            }

        })*/
        /*FragmentHome.btnMenu!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(applicationContext, "Hello", Toast.LENGTH_SHORT).show()
            }

        })*/

        //supportFragmentManager.beginTransaction().add(R.id.container_home, FragmentHome()).commit()

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

      /*  btnMenu = findViewById(R.id.btn_menu)

        btnMenu!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                drawer_layout.openDrawer(GravityCompat.START)
            }

        })*/
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.feed_back -> {
                Toast.makeText(applicationContext, "feedback selected", Toast.LENGTH_SHORT).show()
            }
            R.id.rate_app-> {
                Toast.makeText(applicationContext, "Rate app selected", Toast.LENGTH_SHORT).show()
            }
            R.id.setting -> {
                Toast.makeText(applicationContext, "Setting selected", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
