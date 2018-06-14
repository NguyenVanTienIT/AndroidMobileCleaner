package com.example.asus.mobilecleaner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.provider.ContactsContract
import java.io.*
import java.lang.reflect.Array.get


class ActivityBackup : AppCompatActivity() {

    var btnBackup : Button? = null
    var recyclerHistory : RecyclerView? = null
    var listHistory : ArrayList<BackupContacts>? = null
    var historyAdapter : BackupAdapter?  = null


    companion object {
        val nameStogre : String = "/MobileCleaner"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)

        btnBackup = findViewById(R.id.btn_backup)
        recyclerHistory = findViewById(R.id.list_history)

        recyclerHistory!!.layoutManager = LinearLayoutManager(applicationContext)

        val itemDecoration = DividerItemDecoration(applicationContext, LinearLayoutManager(applicationContext).orientation)
        recyclerHistory!!.setHasFixedSize(true)
        recyclerHistory!!.setLayoutManager(LinearLayoutManager(applicationContext))
        recyclerHistory!!.addItemDecoration(itemDecoration)

        listHistory = ArrayList()

        // make folder for app

        val f = File(Environment.getExternalStorageDirectory().toString()+ nameStogre)
        if (!f.exists())
            f.mkdir()

        initView()


        btnBackup!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                getVcardString()
            }
        })
    }

    private fun isExternalStorageWritable(): Boolean {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            return true
        } else {
            return false
        }
    }


    fun BackupToStorage(){

    }


   /* fun writeFile(list : ArrayList<Contacts>) {
        if (isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val textFile = File(Environment.getExternalStorageDirectory().toString() + nameStogre, "Contacts.vcf")
            try {
                val fos = FileOutputStream(textFile)
                for (contacts : Contacts in list){

                }

                fos.write(text.getText().toString().toByteArray())
                fos.close()

                Toast.makeText(this, "File Saved.", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            Toast.makeText(this, "Cannot Write to External Storage.", Toast.LENGTH_SHORT).show()
        }
    }*/


    private fun getVcardString() {


       var vCard = ArrayList<String>()  // Its global....
        try {
            var cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            if (cursor != null && cursor.getCount() > 0) {
                var i: Int
                //val storage_path = Environment.getExternalStorageDirectory().toString() + nameStogre + "Contacts.VCF"
                //val textFile = File(Environment.getExternalStorageDirectory().toString() + nameStogre, "Contacts.VCF")
                val textFile = File(Environment.getExternalStorageDirectory().toString() + nameStogre, "Contacts.txt")
                val mFileOutputStream = FileOutputStream(textFile)
                cursor.moveToFirst()
                i = 0
                while (i < cursor.getCount()) {
                    Toast.makeText(applicationContext, "this is here"+cursor.getCount(), Toast.LENGTH_SHORT).show()
                    vCard.add(get(cursor, vCard)!!.toString())
                    //vCard.add("0968931478")
                    Log.d("TAG", "Contact " + (i + 1) + "VcF String is" + vCard.get(i))
                    cursor.moveToNext()
                    mFileOutputStream.write(vCard.get(i).toString().toByteArray())
                    i++
                }
                mFileOutputStream.close()
                cursor.close()
            }
            else {
                Log.d("TAG", "No Contacts in Your Phone")
            }
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }


    private fun get(cursor : Cursor, list : ArrayList<String>) : String?{
        var   lookupKey : String= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        var uri : Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
        var fd : AssetFileDescriptor? = null;
        try {
            fd = this.getContentResolver().openAssetFileDescriptor(uri, "r");

            var fis : FileInputStream = fd.createInputStream();
           // var buf : ByteArray = (fd.getDeclaredLength() as Int) as ByteArray
            //var buf : ByteArray = ByteArray(fd.getDeclaredLength().toInt())
            var buf : ByteArray = readBytes(fis)
            //fis.read(buf);
            var vcardstring : String = buf.toString()
            //list.add(vcardstring);
            return vcardstring
        }
        catch (e1 : Exception)
        {
            e1.printStackTrace();
        }
        return null
    }


    @Throws(IOException::class)
    fun readBytes(inputStream: InputStream): ByteArray {
        // this dynamically extends to take the bytes you read
        val byteBuffer = ByteArrayOutputStream()

        // this is storage overwritten on each iteration with bytes
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        // we need to know how may bytes were read to write them to the byteBuffer
        var len = 0
        while (len != -1) {
            byteBuffer.write(buffer, 0, len)
            len = inputStream.read(buffer)
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray()
    }

    fun checkPermission(permission: String): Boolean {
        val check = ContextCompat.checkSelfPermission(this, permission)
        return check == PackageManager.PERMISSION_GRANTED
    }



    private fun initView() {


        for(i in 0..5){
            listHistory?.add(BackupContacts(i * 1000,"14/08/1997",null))
        }


        if(historyAdapter == null) {
            historyAdapter = BackupAdapter(listHistory!!)
            recyclerHistory!!.adapter = historyAdapter
        }

        else{
            historyAdapter!!.notifyDataSetChanged()
        }
    }

    inner class BackupHolder(inflater : LayoutInflater, parent : ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_history_backup,parent,false)),
            View.OnClickListener{


        var txtNumContacts : TextView?= null
        var txtTimebackup : TextView? = null


        var newBackupContact : BackupContacts? = null


        init {
            txtNumContacts = itemView.findViewById(R.id.count_contacts)
            txtTimebackup = itemView.findViewById(R.id.history)

            itemView.setOnClickListener(this)
        }



        fun bind(backupContacts: BackupContacts){
            newBackupContact = backupContacts
            txtNumContacts!!.setText(backupContacts.numCount.toString())
            txtTimebackup!!.setText(backupContacts.timeBackup)
        }

        override fun onClick(v: View?) {
            Toast.makeText(applicationContext, "Chúng tôi sẽ phát triển sau", Toast.LENGTH_SHORT).show()
        }

    }

    inner class BackupAdapter(listBackup : ArrayList<BackupContacts>) : RecyclerView.Adapter<BackupHolder>(){

        var listBackupAdapter : ArrayList<BackupContacts>? = null

        init {
            listBackupAdapter = listBackup
        }

        override fun getItemCount(): Int {
            return listBackupAdapter!!.size
        }

        override fun onBindViewHolder(holder: BackupHolder, position: Int) {
            var backupContacts : BackupContacts = listBackupAdapter!![position]

            holder!!.bind(backupContacts)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupHolder {
            var layoutInflater : LayoutInflater = LayoutInflater.from(applicationContext)

            return BackupHolder(layoutInflater, parent)
        }

    }


}
