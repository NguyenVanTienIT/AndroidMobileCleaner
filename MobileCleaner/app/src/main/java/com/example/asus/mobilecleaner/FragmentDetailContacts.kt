package com.example.asus.mobilecleaner

import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class FragmentDetailContacts() : Fragment() {


    var recyclerView : RecyclerView? = null
    var adapter : ContactDetailAdapter? = null

    var listContact : ArrayList<Contacts>? = null

    companion object {
        var nd : String? = null
        fun createnewFragment(c : String) : FragmentDetailContacts{
            nd = c
            return FragmentDetailContacts()
        }
    }

  /*  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_contacts)

        recyclerView = findViewById(R.id.list_detail)
        btnDelete = findViewById(R.id.btn_delete)

        var intent : Intent = getIntent()
        var nd : String = intent.getStringExtra("contacts")

        updateUI(nd)

    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view : View = inflater!!.inflate(R.layout.fragment_list_contacts, container, false)

        recyclerView = view.findViewById(R.id.list_detail)

        recyclerView?.layoutManager = LinearLayoutManager(getActivity())

        listContact = ArrayList()

        updateUI(nd!!)

        return view

    }


    private fun updateUI(c : String)
    {
        // set Adapter cho recyclerview
        listContact = getContactList()
        var listTam : ArrayList<Contacts> = ArrayList()

        for(contacts1 : Contacts in listContact!!){
            if(contacts1.numberPhone == c){
                listTam.add(contacts1)
            }
        }

        if(adapter == null) {
            adapter = ContactDetailAdapter(listTam!!)
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

    inner class ContactDetailHolder(inflater : LayoutInflater, parent : ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_detail_contacts,parent,false)),
            View.OnClickListener{

        var imgDetail : ImageView? = null
        var sdtDetail :  TextView? = null
        var nameDetail : TextView? = null
        var emailDetail : TextView? = null

        var newContact : Contacts? = null

        init {
            imgDetail = itemView.findViewById(R.id.img_detail)
            sdtDetail = itemView.findViewById(R.id.numberContacts)
            nameDetail =  itemView.findViewById(R.id.nameContacts)
            emailDetail = itemView.findViewById(R.id.emailContacts)
            itemView.setOnClickListener(this)
        }



        fun bind(contacts : Contacts){

            newContact = contacts

            sdtDetail!!.setText(contacts.numberPhone.toString())
            nameDetail!!.setText(contacts.name!!.toString())
            imgDetail!!.setImageResource(R.drawable.ic_contact)
            emailDetail!!.setText(contacts.email)
        }

        override fun onClick(v: View?) {

        }

    }

    inner class ContactDetailAdapter(listContacts : ArrayList<Contacts>) : RecyclerView.Adapter<ContactDetailHolder>(){

        var listContactAdapter : ArrayList<Contacts>? = null

        init {
            listContactAdapter = listContacts
        }

        override fun getItemCount(): Int {
            return listContactAdapter!!.size
        }

        override fun onBindViewHolder(holder: ContactDetailHolder, position: Int) {
            var contacts : Contacts = listContactAdapter!![position]

            holder!!.bind(contacts)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactDetailHolder {
            var layoutInflater : LayoutInflater = LayoutInflater.from(activity)

            return ContactDetailHolder(layoutInflater, parent)
        }

    }

}