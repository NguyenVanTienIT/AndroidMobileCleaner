package com.example.asus.mobilecleaner

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.view.animation.LinearInterpolator
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


class FragmentHome : Fragment(), Animation.AnimationListener{


    //private var fragmentManager : FragmentManager? = null
    private var transaction :  FragmentTransaction? = null
    var animation: Animation? = null
    var animation2: Animation? = null

    var imgLoad : ImageView? = null
    var imgArroud : ImageView? = null
    var imgbackgroud : ImageView? = null


    var textMassage : TextView?= null
    var textNotice : TextView? = null
    var iconNotice : ImageView? = null


    var iconAnlyze : ImageView? = null
    var iconQuickScan : ImageView? = null
    var iconFullScan : ImageView? = null
    var iconSecure : ImageView? = null

    var statusAnalyze : TextView? = null
    var statusQuickScan : TextView? = null
    var statusFullScan : TextView? = null
    var statusSecure : TextView? = null


    var itemAnalyze : LinearLayout? = null
    var itemScan : LinearLayout? = null
    var itemFullScan : LinearLayout? = null
    var itemSecure : LinearLayout? = null


    companion object {

        var open : Boolean = false
        var trans : Boolean = false


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgArroud = view.findViewById(R.id.img_arrow)
        imgLoad = view.findViewById(R.id.img_loading3)
        imgbackgroud = view.findViewById(R.id.img_bg3)

        textMassage = view.findViewById(R.id.massage)
        textNotice = view.findViewById(R.id.notice)
        iconNotice = view.findViewById(R.id.icon_notice)

        //btnMenu = view.findViewById(R.id.btn_menu)



        iconAnlyze = view.findViewById(R.id.icon_analyze)
        statusAnalyze = view.findViewById(R.id.status_analyze)

        iconQuickScan = view.findViewById(R.id.icon_quick)
        statusQuickScan = view.findViewById(R.id.status_quick)

        iconFullScan = view.findViewById(R.id.icon_fullScan)
        statusFullScan = view.findViewById(R.id.status_fullScan)

        iconSecure = view.findViewById(R.id.icon_secure)
        statusSecure = view.findViewById(R.id.status_secure)


        itemAnalyze = view.findViewById(R.id.anlyze)
        itemScan = view.findViewById(R.id.quickScan)
        itemFullScan = view.findViewById(R.id.fullScan)
        itemSecure = view.findViewById(R.id.secure)

        animation = AnimationUtils.loadAnimation(activity, R.anim.rotate);
        animation2 = AnimationUtils.loadAnimation(activity, R.anim.rotate2);

       // animation!!.setAnimationListener(context);
        imgLoad!!.setVisibility(View.VISIBLE);
        imgLoad!!.startAnimation(animation);

        imgArroud!!.setVisibility(View.VISIBLE);
        imgArroud!!.startAnimation(animation2);

        animation!!.setInterpolator(LinearInterpolator());
        animation2!!.setInterpolator(LinearInterpolator());



        try {
            val fileRecent = File(Environment.getExternalStorageDirectory().toString() + "/MobileCleaner", "recent.txt")

            if (fileRecent.exists()) {
                val input = FileInputStream(fileRecent)
                val r = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8))
                var data: String = r.readLine()
                statusFullScan!!.setText("Lastest backup at " + data)

                input.close()
                r.close()
            } else {
                statusFullScan!!.setText("you have not backed up")
            }
            open = true
        }
        catch (e : Exception){
            open = false
            statusFullScan!!.setText("please allow pessminsion")
        }



        itemAnalyze!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var intent : Intent = Intent(activity, ActivityDeviceAnalyze::class.java)

                //intent.putExtra("use",ActivityDeviceAnalyze.FomatDouble(getUsedRamMemorySize()))
                //intent.putExtra("free", ActivityDeviceAnalyze.FomatDouble(getFreeRamMemorySize()))
                startActivity(intent)
            }
        })


        itemScan!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(activity, "Scanning", Toast.LENGTH_SHORT).show()
                var intent1 : Intent = Intent(activity, ActivityContactsScan::class.java)
                startActivity(intent1)
            }
        })


        itemFullScan!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if(open) {
                    var intent: Intent = Intent(activity, ActivityBackup::class.java)
                    startActivity(intent)
                }
                else{
                    statusFullScan!!.setText("please allow pessminsion")
                }
            }
        })


        itemSecure!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(activity, "Securing", Toast.LENGTH_SHORT).show()
                var intent2 : Intent = Intent(activity, ActivityVPN::class.java)
                startActivity(intent2)
            }
        })



    }




    override fun onResume() {
        super.onResume()
        try {
            val fileRecent = File(Environment.getExternalStorageDirectory().toString() + "/MobileCleaner", "recent.txt")

            if (fileRecent.exists()) {

                val input = FileInputStream(fileRecent)
                val r = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8))
                var data: String = r.readLine()
                statusFullScan!!.setText("Lastest backup at " + data)

                input.close()
                r.close()
            } else {
                statusFullScan!!.setText("you have not backed up")
            }
            open = true
        }catch (e : Exception){
            open =false
            statusFullScan!!.setText("please allow pessminsion")
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {

    }

    override fun onAnimationStart(animation: Animation?) {

    }

  /*  fun getRamSize() : Double{
        val actManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalMemory = memInfo.totalMem / 1.0
        return totalMemory / ActivityDeviceAnalyze.GB
    }

    fun getFreeRamMemorySize(): Double {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)

        return mi.availMem / ActivityDeviceAnalyze.GB
    }

    fun getUsedRamMemorySize() : Double{
        return getRamSize() - getFreeRamMemorySize()
    }*/

}