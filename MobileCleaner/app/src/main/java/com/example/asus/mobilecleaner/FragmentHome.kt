package com.example.asus.mobilecleaner

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class FragmentHome : Fragment() {
   //private var fragmentManager : FragmentManager? = null
    private var transaction :  FragmentTransaction? = null

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*imgArroud = view.findViewById(R.id.img_arrow)
        imgLoad = view.findViewById(R.id.img_loading3)
        imgbackgroud = view.findViewById(R.id.img_bg3)*/

        textMassage = view.findViewById(R.id.massage)
        textNotice = view.findViewById(R.id.notice)
        iconNotice = view.findViewById(R.id.icon_notice)



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
                Toast.makeText(activity, "Full Scanning", Toast.LENGTH_SHORT).show()
            }
        })


        itemSecure!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(activity, "Securing", Toast.LENGTH_SHORT).show()
            }
        })
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