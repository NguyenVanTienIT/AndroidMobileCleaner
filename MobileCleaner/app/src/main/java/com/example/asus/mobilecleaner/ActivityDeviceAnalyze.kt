package com.example.asus.mobilecleaner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.ArrayList


class ActivityDeviceAnalyze : AppCompatActivity() {

    var btnMemory : Button? = null
    var btnStogre : Button? = null

    var layoutMemory : LinearLayout? = null
    var layoutStogre : LinearLayout? = null

    var txtFreeDisk : TextView? = null
    var txtUsedDisk : TextView? = null

    //var txtPugrableMemory : TextView? = null
    var txtFreeMemory : TextView? = null
    var txtUseMemory : TextView? = null
    //var txtInnactiveMemory : TextView? = null
    //var txtActiveMemory : TextView? = null
    //var txtWiredMemory : TextView? = null

     var pieChart : PieChart? = null
     var pieChart1 : PieChart? = null



    companion object {
        final  var Gb : Double = 1048576.0
        final var GB : Double = 1073741824.0
        fun FomatDouble(x : Double) : String{
            var double : String = x.toString()
            var position : Int = double.indexOf('.')
            double = double.substring(0,position+3)
            return double
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_analyze)

        btnMemory = findViewById(R.id.memory)
        btnStogre = findViewById(R.id.stogre)

        layoutMemory = findViewById(R.id.memory_analyze)
        layoutStogre = findViewById(R.id.stogre_analyze)


        txtFreeDisk = findViewById(R.id.free_disk)
        txtUsedDisk = findViewById(R.id.used_disk)

        //txtPugrableMemory = findViewById(R.id.purgable_memory)
        txtFreeMemory = findViewById(R.id.free_memory)
        txtUseMemory = findViewById(R.id.use_memory)
        //txtInnactiveMemory = findViewById(R.id.inactive_memory)
        //txtActiveMemory = findViewById(R.id.active_memory)
        //txtWiredMemory = findViewById(R.id.wired_memory)

        pieChart = findViewById(R.id.idPieChart)
        pieChart1 = findViewById(R.id.idPieChart1)

        //var intent : Intent = getIntent()
        //changDataMemory(intent.getStringExtra("use"), intent.getStringExtra("free"))
        //changDataMemory(FomatDouble(getUsedRamMemorySize(),FomatDouble(getFreeRamMemorySize())


        changDataMemory(FomatDouble(getUsedMemorySize()), FomatDouble(getFreeMemorySize()))


        btnMemory!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
               // btnMemory!!.setBackgroundColor()

                layoutMemory!!.visibility = View.VISIBLE
                layoutStogre!!.visibility = View.GONE

                txtUseMemory!!.setText(FomatDouble(getUsedRamMemorySize()))
                txtFreeMemory!!.setText(FomatDouble(getFreeRamMemorySize()))
            }
        })

        btnStogre!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                layoutMemory!!.visibility = View.GONE
                layoutStogre!!.visibility = View.VISIBLE

                var usedMemory : String = FomatDouble(getUsedMemorySize())
                var freeMemory : String = FomatDouble(getFreeMemorySize())

                changDataStore(usedMemory, freeMemory)
            }
        })

    }


    fun changDataStore(usedMemory : String, freeMemory : String){
        val colors = java.util.ArrayList<Int>()

        var color1 : Int = Color.parseColor("#5CA3DE")
        var color2 : Int = Color.parseColor("#D0E462")


        colors.add(color1)
        colors.add(color2)

        pieChart!!.setUsePercentValues(true)
        pieChart!!.description.isEnabled = false
        pieChart!!.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart!!.dragDecelerationFrictionCoef = 0.95f
        pieChart!!.isDrawHoleEnabled = true
        pieChart!!.setHoleColor(Color.WHITE)
        pieChart!!.transparentCircleRadius = 61f

        var yValues : ArrayList<PieEntry> = ArrayList<PieEntry>()

        yValues.add(PieEntry(usedMemory.toFloat(), "Free disk"))
        yValues.add(PieEntry(freeMemory.toFloat(), "Used disk"))


        var dataSet : PieDataSet = PieDataSet(yValues, "Country")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(colors)


        var data : PieData = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        pieChart!!.data = data

        txtUsedDisk!!.setText(usedMemory + " GB")
        txtFreeDisk!!.setText(freeMemory + " GB")

    }

    fun changDataMemory(use : String, free : String){

        val colors = java.util.ArrayList<Int>()
        var color1 : Int = Color.parseColor("#EDA868")
        var color2 : Int = Color.parseColor("#C0DA55")


        colors.add(color1)
        colors.add(color2)

        pieChart1!!.setUsePercentValues(true)
        pieChart1!!.description.isEnabled = false
        pieChart1!!.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart1!!.dragDecelerationFrictionCoef = 0.95f
        pieChart1!!.isDrawHoleEnabled = true
        pieChart1!!.setHoleColor(Color.WHITE)
        pieChart1!!.transparentCircleRadius = 61f

        var yValues : ArrayList<PieEntry> = ArrayList<PieEntry>()

        yValues.add(PieEntry(use.toFloat(), "Free disk"))
        yValues.add(PieEntry(free.toFloat(), "Used disk"))


        var dataSet : PieDataSet = PieDataSet(yValues, "Country")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(colors)


        var data : PieData = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.YELLOW)

        pieChart1!!.data = data

        txtUseMemory!!.setText(use + " GB")
        txtFreeMemory!!.setText(free + " GB")

    }




    fun getUsedMemorySize(): Double {

        var freeSize = 0.0
        var totalSize = 0.0
        var usedSize = -1.0
        try {
            val info = Runtime.getRuntime()
            freeSize = info.freeMemory() / 1.0
            totalSize = info.totalMemory() / 1.0
            usedSize = totalSize - freeSize
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Đã có lỗi xảy ra khi thức hiên", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return usedSize / Gb
    }

    fun getFreeMemorySize() : Double{
        var freeSize = 0.0
        try {
            val info = Runtime.getRuntime()
            freeSize = info.freeMemory() / 1.0

        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Đã có lỗi xảy ra khi thức hiên", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        return freeSize / Gb
    }


    fun getRamSize() : Double{
        val actManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalMemory = memInfo.totalMem / 1.0
        return totalMemory / GB
    }

    fun getFreeRamMemorySize(): Double {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)

        return mi.availMem / GB
    }

    fun getUsedRamMemorySize() : Double{
       return getRamSize() - getFreeRamMemorySize()
    }



}
