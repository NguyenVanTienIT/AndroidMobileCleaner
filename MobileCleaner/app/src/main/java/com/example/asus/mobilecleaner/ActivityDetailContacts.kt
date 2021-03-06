package com.example.asus.mobilecleaner

import android.app.PendingIntent.getActivity
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.nio.file.Files.delete
import android.provider.ContactsContract.PhoneLookup
import java.nio.file.Files.delete
import android.support.v7.widget.DividerItemDecoration
import android.widget.*
import java.nio.file.Files.delete
import android.text.method.TextKeyListener.clear
import android.os.RemoteException
import android.provider.CallLog
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import java.nio.file.Files.delete
import java.nio.file.Files.delete
import android.content.ContentResolver
import java.nio.file.Files.delete



class ActivityDetailContacts : AppCompatActivity() {


    var btnDelete: Button? = null
    var recyclerView: RecyclerView? = null
    var listContact: ArrayList<Contacts>? = null
    var adapterDetailContacts: ContactDetailAdapter? = null
    var idSelect: String? = null
    var phoneSelect: String? = null
    var nameSelect: String? = null

    companion object {
        var selected: Int? = null
        var row_index: Int = -1
        var listTam: ArrayList<Contacts>? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_detail_contacts)

        btnDelete = findViewById(R.id.btn_delete)
        recyclerView = findViewById(R.id.list_detail)

        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = layoutManager

        listTam = ArrayList()


        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(layoutManager)
        recyclerView!!.addItemDecoration(itemDecoration)

        var intent: Intent = getIntent()
        var nd: String = intent.getStringExtra("contacts")

        updateUI(nd)

        btnDelete!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (idSelect != null && phoneSelect != null) {
                    deleteContact(applicationContext, phoneSelect!!, nameSelect!!,idSelect!!)
                    deleteContactsInList(idSelect!!)
                    ActivityContactsScan.phoneRemove = phoneSelect
                    idSelect = null
                    phoneSelect = null
                    nameSelect = null
                    adapterDetailContacts!!.notifyDataSetChanged()

                } else {
                    Toast.makeText(applicationContext, "Please choose 1 contacts", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    fun deleteContactsInList(id : String){
        for(i in 0..listTam!!.size - 1){
            if(listTam!!.get(i).idContacts.equals(id)){
                listTam!!.removeAt(i)
                break;
            }
        }
        adapterDetailContacts!!.notifyDataSetChanged()
    }

    private fun updateUI(c: String) {
        // set Adapter cho recyclerview
        listContact = getContactList()

        for (contacts1: Contacts in listContact!!) {
            if (contacts1.numberPhone == c) {
                listTam?.add(contacts1)
            }
        }
        if (adapterDetailContacts == null) {
            adapterDetailContacts = ContactDetailAdapter(listTam!!)
            recyclerView!!.adapter = adapterDetailContacts
        }
        else {
            adapterDetailContacts!!.notifyDataSetChanged()
        }
    }


    private fun getContactList(): ArrayList<Contacts> {
        var list: ArrayList<Contacts> = ArrayList()

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
            val contactId : Long = cursor.position.toLong()

            var src : Uri? = null

            if (photoId != 0L) {
                val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
                val photUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                        .CONTENT_DIRECTORY)
                src  = photUri
            } else  src = null

            var contacts: Contacts = Contacts(id, src, phone, name, 1, null)
            list.add(contacts)

        }
        cursor.close()

        return list
    }

    inner class ContactDetailHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_detail_contacts, parent, false)),
            View.OnClickListener {

        var imgDetail: ImageView? = null
        var sdtDetail: TextView? = null
        var nameDetail: TextView? = null
        var emailDetail: TextView? = null
        var id: TextView? = null
        var line: LinearLayout? = null

        var newContact: Contacts? = null

        init {
            imgDetail = itemView.findViewById(R.id.img_detail)
            sdtDetail = itemView.findViewById(R.id.numberContacts)
            nameDetail = itemView.findViewById(R.id.nameContacts)
            emailDetail = itemView.findViewById(R.id.emailContacts)
            id = itemView.findViewById(R.id.id_contacts)
            line = itemView.findViewById(R.id.line)
            itemView.setOnClickListener(this)
        }


        fun bind(contacts: Contacts) {
            newContact = contacts
            sdtDetail!!.setText(contacts.numberPhone)
            nameDetail!!.setText(contacts.name)

            if(contacts.srcImg != null){
                imgDetail!!.setImageURI(contacts.srcImg)
            }
            else  imgDetail!!.setImageResource(R.drawable.user)

            emailDetail!!.setText(contacts.email)
            id!!.setText(contacts.idContacts)
        }

        override fun onClick(v: View?) {

        }

    }

    inner class ContactDetailAdapter(listContacts: ArrayList<Contacts>) : RecyclerView.Adapter<ContactDetailHolder>() {

        var listContactAdapter: ArrayList<Contacts>? = null

        init {
            listContactAdapter = listContacts
        }

        override fun getItemCount(): Int {
            return listContactAdapter!!.size
        }

        override fun onBindViewHolder(holder: ContactDetailHolder, position: Int) {
            var contacts: Contacts = listContactAdapter!![position]
            var holder2 = holder
            holder!!.bind(contacts)

            if (row_index == position) {
                holder!!.line!!.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            } else {
                holder!!.line!!.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.item))
            }

            holder.line!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    row_index = position
                    idSelect = contacts.idContacts
                    phoneSelect = contacts.numberPhone
                    nameSelect = contacts.name
                    notifyDataSetChanged()
                }
            })
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactDetailHolder {
            var layoutInflater: LayoutInflater = LayoutInflater.from(applicationContext)

            return ContactDetailHolder(layoutInflater, parent)
        }

    }


    fun deleteContact(ctx: Context, phone: String, name: String, id: String): Boolean {   // xóa đi 1 số liên lạc với tên bất kỳ
        val contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))   // sử dụng cho viecj tìm kiếm số điên thoại
        val cur = ctx.contentResolver.query(contactUri, null, null, null, null)
        var value : Array<String> = arrayOf(id)

        try {
                while (cur.moveToNext()) {
                    if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equals(name)) {

                        if(cur.getString(cur.getColumnIndex(PhoneLookup.DATA_ID)).equals(id)) {
                            val lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                            val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)

                            ctx.getContentResolver().delete(uri, cur.getString(cur.getColumnIndex(PhoneLookup.DATA_ID)) + "=?", value)

                            return true

                        }
                    }
                }
       } catch (e: Exception) {
            println(e.stackTrace)
            Toast.makeText(applicationContext, "Erorr", Toast.LENGTH_SHORT).show()
        }
        finally {
        cur!!.close()
        }
        return false
    }

}



