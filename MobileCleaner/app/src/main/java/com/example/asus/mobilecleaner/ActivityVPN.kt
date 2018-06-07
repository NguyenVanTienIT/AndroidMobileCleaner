package com.example.asus.mobilecleaner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ActivityVPN : AppCompatActivity() {

    var listRecyclerVPN : RecyclerView?= null
    var listVPN : ArrayList<VPN>? = null
    var adapterVPN : VPNAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vpn)

        listRecyclerVPN = findViewById(R.id.list_vpn)
        listVPN = ArrayList()
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        listRecyclerVPN?.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        listRecyclerVPN!!.setHasFixedSize(true)
        listRecyclerVPN!!.setLayoutManager(layoutManager)
        listRecyclerVPN!!.addItemDecoration(itemDecoration)

        upDateUI()
    }

    fun upDateUI(){

        var arrayCountryName : Array<String> = arrayOf("USA", "Australia", "Canada", "Germany", "Hong Kong", "Singapore", "UK", "Czech Republic", "Denmark", "Georgia", "Gibraltar", "Greece", "Estonia")
       // var arrayImage : Array<String> = arrayOf("united_states", "australia", "canada", "germany", "hongkong", "singapore", "united_kingdom","cz","dm","geos","gib","greens","esto")

        var arrayImage : Array<Int> = arrayOf(R.drawable.united_states, R.drawable.australia, R.drawable.canada, R.drawable.germany, R.drawable.hongkong, R.drawable.singapore, R.drawable.united_kingdom, R.drawable.cz, R.drawable.dm, R.drawable.geos, R.drawable.gib,R.drawable.greens,R.drawable.esto)
        var arrayIp : Array<String> = arrayOf("31.3.157.129", "203.31.216.1", "162.219.179.50", "107.168.204.2", "119.81.174.2", "119.81.38.202", "194.88.142.6","83.167.240.130","109.202.157.130","191.101.71.129","45.59.179.2","167.88.110.2","155.254.235.2")

        var size : Int = arrayCountryName.size

        for(i in 0..size-1){
            var newVPN : VPN = VPN(arrayCountryName[i], arrayImage[i],arrayIp[i])
            listVPN?.add(newVPN)
        }

        if(adapterVPN == null) {
            adapterVPN = VPNAdapter(listVPN!!)
            listRecyclerVPN!!.adapter = adapterVPN
        }

        else{
            adapterVPN!!.notifyDataSetChanged()
        }

    }


    inner class VPNHolder(inflater : LayoutInflater, parent : ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_vpn, parent,false)),
            View.OnClickListener{


        var imageVPN : ImageView?= null
        var titleVPN :  TextView? = null

        var newVPN : VPN? = null

        init {
            imageVPN = itemView.findViewById(R.id.img_item)
            titleVPN = itemView.findViewById(R.id.item_title)
            itemView.setOnClickListener(this)
        }

        fun bind(vpn : VPN){

            newVPN = vpn

            titleVPN!!.setText(newVPN!!.mTitle)
            imageVPN!!.setImageResource(newVPN!!.mImage!!)
        }

        override fun onClick(v: View?) {

        }

    }

    inner class VPNAdapter(listVPN : ArrayList<VPN>) : RecyclerView.Adapter<VPNHolder>(){

        var listVPNAdapter : ArrayList<VPN>? = null

        init {
            listVPNAdapter = listVPN
        }

        override fun getItemCount(): Int {
            return listVPNAdapter!!.size
        }

        override fun onBindViewHolder(holder: VPNHolder, position: Int) {
            var vpn : VPN = listVPNAdapter!![position]

            holder!!.bind(vpn)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VPNHolder {
            var layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)

            return VPNHolder(layoutInflater, parent)
        }

    }

}
