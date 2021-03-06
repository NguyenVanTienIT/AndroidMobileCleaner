package com.example.asus.mobilecleaner

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
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
import android.view.*
import android.widget.*

class ActivityContactsScan : AbsRuntimePermission() {



    var listRecycler : RecyclerView? = null
    var layoutNone : RelativeLayout? = null


    var adapter : ContactAdapter? = null
    var listContact :  ArrayList<Contacts>? = null
    var listTam : ArrayList<Contacts>?  = null
    companion object {
        private val REQUEST_PERMISSION = 10
        private var check = false
        public var phoneRemove : String? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_contacts_scan)

        listRecycler = findViewById(R.id.list_contacts)
        layoutNone = findViewById(R.id.none_copntacts)


        listRecycler?.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager(applicationContext).orientation)
        listRecycler!!.setHasFixedSize(true)
        listRecycler!!.setLayoutManager(LinearLayoutManager(applicationContext))
        listRecycler!!.addItemDecoration(itemDecoration)

        listContact = ArrayList()

        requestAppPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET),
                R.string.msg, REQUEST_PERMISSION)

        updateUI()
    }

    override fun onPermissionsGranted(requestCode: Int) {
        //Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
    }


    override fun onResume() {
        super.onResume()
        if(check) {
            if (phoneRemove != null) {
                for (c: Contacts in listTam!!) {
                    if (c.numberPhone == phoneRemove!!) {
                        var len : Int? = c.numberCount
                        len = len!! - 1
                        if(len < 2){
                            listTam!!.remove(c)
                        }
                        else {
                            c.numberCount = len
                        }
                    }
                }
                adapter!!.notifyDataSetChanged()
                check = false
            }

            if(listTam == null || listTam!!.size == 0){
                layoutNone!!.visibility = View.VISIBLE
                listRecycler!!.visibility = View.GONE
            }
        }
    }



    private fun updateUI()
    {
        // set Adapter cho recyclerview
        listContact = getContactList()
        listTam  = ArrayList()

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
                listTam?.add(contacts1)
            }
        }

        if(adapter == null) {
            adapter = ContactAdapter(listTam!!)
            listRecycler!!.adapter = adapter
        }

        else{
            adapter!!.notifyDataSetChanged()
        }

        if(listTam == null || listTam!!.size == 0){
            layoutNone!!.visibility = View.VISIBLE
            listRecycler!!.visibility = View.GONE
        }
    }



    private fun getContactList() : ArrayList<Contacts>?{
        var list : ArrayList<Contacts> = ArrayList()

    try {
        var uriContacts: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var cursor = applicationContext!!.contentResolver.query(uriContacts, null, null, null, null)

        while (cursor.moveToNext()) {
            var idName: String = ContactsContract.Contacts.DISPLAY_NAME
            //var idContact: String = ContactsContract.Data._ID
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

            /*val emailCur = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id), null)
            emailCur.moveToNext()
            val email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
            emailCur.close();*/


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
    }

    catch (e : Exception)
    {
        Toast.makeText(applicationContext, "You should allow to acess adressbook",Toast.LENGTH_SHORT).show()
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
            else  imageContact!!.setImageResource(R.drawable.user)
        }

        override fun onClick(v: View?) {
            var intent : Intent = Intent(applicationContext, ActivityDetailContacts::class.java)
            intent.putExtra("contacts", newContact!!.numberPhone)
            check = true
            startActivity(intent)
            //startActivityForResult(intent, RESULT_OK)

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
