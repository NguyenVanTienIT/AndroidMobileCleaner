package com.example.asus.mobilecleaner

import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.test.ProviderTestCase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ArrayAdapter
import android.Manifest.permission
import android.Manifest.permission.READ_CONTACTS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class FragmentListContacts : Fragment() {

    var recyclerView :  RecyclerView? = null
    var adapter : FragmentListContacts.ContactAdapter? = null
    var listContact :  ArrayList<Contacts>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view : View = inflater!!.inflate(R.layout.fragment_list_contacts, container, false)

        recyclerView = view.findViewById(R.id.list_contacts)

        recyclerView?.layoutManager = LinearLayoutManager(getActivity())

        listContact = ArrayList()

        checkAndRequestPermissions()

        updateUI()

        return view

    }


    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS)
        val listPermissionsNeeded = java.util.ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity!!, listPermissionsNeeded.toTypedArray(), 1)
        }
    }

   private fun updateUI()
    {
            // set Adapter cho recyclerview
        listContact = getContactList()
        var listTam : ArrayList<Contacts> = ArrayList()

        for(contacts1 : Contacts in listContact!!){
            var count = 0;
            for(contacts2 : Contacts in listContact!!){
                if(contacts1.numberPhone == contacts2.numberPhone && count == 0 && contacts1.numberPhone != "-1"){
                    count++
                }
                else if (contacts1.numberPhone == contacts2.numberPhone && count == 1 && contacts1.numberPhone != "-1"){
                    contacts2.numberPhone = "-1"
                    count++
                    contacts1.numberCount = count
                }
            }
            if (count > 1){
                listTam.add(contacts1)
            }
        }

            if(adapter == null) {
                adapter = ContactAdapter(listTam!!)
                recyclerView!!.adapter = adapter
            }

            else{
                adapter!!.notifyDataSetChanged()
            }
    }




    private fun getContactList() : ArrayList<Contacts>{
        var list : ArrayList<Contacts> = ArrayList()


            var uriContacts: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            var cursor = activity!!.contentResolver.query(uriContacts, null, null, null, null)

            while (cursor.moveToNext()) {
                var idName: String = ContactsContract.Contacts.DISPLAY_NAME
                var idPhone: String = ContactsContract.CommonDataKinds.Phone.NUMBER

                var colNameIndex: Int = cursor.getColumnIndex(idName)
                var colPhoneIndex: Int = cursor.getColumnIndex(idPhone)

                var name: String = cursor.getString(colNameIndex)
                var phone: String = cursor.getString(colPhoneIndex)

                var contacts: Contacts = Contacts("google.com", phone, name, 1, "cityhunterconbocuoi@gmai.com")
                list.add(contacts)

            }

            cursor.close()


        return list
    }



    inner class ContactHolder(inflater : LayoutInflater, parent : ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_contacts,parent,false)),
            View.OnClickListener{


        var imageContact : ImageView?= null
        var sdtContacts : TextView? = null
        var countContact :  TextView? = null

        var newContact : Contacts? = null


        init {
            imageContact = itemView.findViewById(R.id.img_contacts)
            sdtContacts = itemView.findViewById(R.id.sdt_contacts)
            countContact =  itemView.findViewById(R.id.count_contacts)
            itemView.setOnClickListener(this)
        }



        fun bind(contacts : Contacts){

            newContact = contacts

            sdtContacts!!.setText(contacts.numberPhone.toString())
            countContact!!.setText(contacts.numberCount!!.toString())
            imageContact!!.setImageResource(R.drawable.ic_contact)
        }

        override fun onClick(v: View?) {
            var intent : Intent = Intent(activity, ActivityDetailContacts::class.java)
            intent.putExtra("contacts", newContact!!.numberPhone)
            startActivity(intent)

            //Toast.makeText(activity, "abc", Toast.LENGTH_SHORT).show()
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
            var layoutInflater : LayoutInflater = LayoutInflater.from(activity)

            return ContactHolder(layoutInflater, parent)
        }

    }





}