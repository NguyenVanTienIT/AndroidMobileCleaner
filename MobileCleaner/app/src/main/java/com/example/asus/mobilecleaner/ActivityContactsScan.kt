package com.example.asus.mobilecleaner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class ActivityContactsScan : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_scan)


        supportFragmentManager.beginTransaction().add(R.id.list_contact, FragmentListContacts()).commit()


//        recyclerView = findViewById(R.id.list_contacts)
//
//        updateUI()

    }

   /* private fun updateUI()
    {

        // tạo dữ liệu giả
        var listContact :  ArrayList<Contacts> = ArrayList()

       for (i in 1..6){
           var contacts : Contacts = Contacts("abc","096893147"+i, i,"nguyenvana@gmail.com")
           listContact.add(contacts)
       }


        // set Adapter cho recyclerview

        if(adapter == null) {
            adapter = ContactAdapter(listContact)
            recyclerView!!.adapter = adapter
        }

        else{
            adapter!!.notifyDataSetChanged()
        }

    }


    inner class ContactHolder(inflater : LayoutInflater, parent : ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_contacts,parent,false)),
            View.OnClickListener{


        var imageContact : ImageView?= null
        var sdtContacts : TextView? = null
        var countContact :  TextView? = null


        init {
            imageContact = itemView.findViewById(R.id.img_contacts)
            sdtContacts = itemView.findViewById(R.id.sdt_contacts)
            countContact =  itemView.findViewById(R.id.count_contacts)
        }



        override fun onClick(v: View?) {

        }

        fun bind(contacts : Contacts){
            sdtContacts!!.setText(contacts.numberPhone)
            countContact!!.setText(contacts.numberCount!!)
            imageContact!!.setImageResource(R.drawable.ic_contact)
        }

    }

    inner class ContactAdapter(listContacts : ArrayList<Contacts>) : RecyclerView.Adapter<ContactHolder>(){

        var listContactAdapter : ArrayList<Contacts>? = null

        init {
            listContactAdapter = listContacts
        }

        override fun getItemCount(): Int {
            return listContactAdapter!!.size
        }

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            var contacts : Contacts = listContactAdapter!![position]

            holder!!.bind(contacts)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
            var layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)

            return ContactHolder(layoutInflater, parent)
        }

    }*/
}
