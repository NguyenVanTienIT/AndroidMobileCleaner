package com.example.asus.mobilecleaner

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ActivityContactsScan : AppCompatActivity() {


    var listRecycler : RecyclerView? = null
    var adapter : ContactAdapter? = null
    var listContact :  ArrayList<Contacts>? = null
    //var btnDelete : Button?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_scan)

        listRecycler = findViewById(R.id.list_contacts)
        //btnDelete = findViewById(R.id.delete_contacts)

        listRecycler?.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager(applicationContext).orientation)
        listRecycler!!.setHasFixedSize(true)
        listRecycler!!.setLayoutManager(LinearLayoutManager(applicationContext))
        listRecycler!!.addItemDecoration(itemDecoration)

        listContact = ArrayList()
        checkAndRequestPermissions()
        updateUI()



        /*btnDelete!!.setOnClickListener(object  : View.OnClickListener{
            override fun onClick(v: View?) {

            }
        })*/


    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS)
        val listPermissionsNeeded = java.util.ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(applicationContext!!, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((applicationContext as Activity?)!!, listPermissionsNeeded.toTypedArray(), 1)
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
                    ++ count
                }
                else if (contacts1.numberPhone == contacts2.numberPhone && count != 0 && contacts1.numberPhone != "-1"){
                    contacts2.numberPhone = "-1"
                    ++ count
                    contacts1.numberCount = count
                }
            }
            if (count > 1){
                listTam.add(contacts1)
            }
        }

        if(adapter == null) {
            adapter = ContactAdapter(listTam!!)
            listRecycler!!.adapter = adapter
        }

        else{
            adapter!!.notifyDataSetChanged()
        }
    }




    private fun getContactList() : ArrayList<Contacts>{
        var list : ArrayList<Contacts> = ArrayList()

    try {
        var uriContacts: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var cursor = applicationContext!!.contentResolver.query(uriContacts, null, null, null, null)

        while (cursor.moveToNext()) {
            var idName: String = ContactsContract.Contacts.DISPLAY_NAME
            var idContact: String = ContactsContract.Contacts._ID
            var idPhone: String = ContactsContract.CommonDataKinds.Phone.NUMBER

            var colNameIndex: Int = cursor.getColumnIndex(idName)
            var colPhoneIndex: Int = cursor.getColumnIndex(idPhone)
            var colId: Int = cursor.getColumnIndex(idContact)

            var name: String = cursor.getString(colNameIndex)
            var phone: String = cursor.getString(colPhoneIndex)
            var id: String = cursor.getString(colId)

            val photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.PHOTO_ID))
            val contactId: Long = cursor.position.toLong()

            var src: Uri? = null

            if (photoId != 0L) {
                val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
                val photUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                src = photUri
            } else src = null

            var contacts: Contacts = Contacts(id, src, phone, name, 1, null)

            list.add(contacts)

        }

        cursor.close()
    }catch (e : Exception){
        Toast.makeText(applicationContext, "You should allow to acess  adress book",Toast.LENGTH_SHORT).show()
    }


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

            if(contacts.srcImg != null){
                imageContact!!.setImageURI(contacts.srcImg)
            }
            else  imageContact!!.setImageResource(R.drawable.facebook_avatar)
        }

        override fun onClick(v: View?) {
            var intent : Intent = Intent(applicationContext, ActivityDetailContacts::class.java)
            intent.putExtra("contacts", newContact!!.numberPhone)
            startActivity(intent)

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

    }
}
