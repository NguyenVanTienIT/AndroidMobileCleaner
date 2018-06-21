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
import android.R.attr.path
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityBackup : AbsRuntimePermission() {


    var btnBackup : Button? = null
    var recyclerHistory : RecyclerView? = null
    var listHistory : ArrayList<BackupContacts>? = null
    var historyAdapter : BackupAdapter?  = null



    val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")


    companion object {
        private val REQUEST_PERMISSION = 10
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

        requestAppPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET),
                R.string.msg, REQUEST_PERMISSION)

        // make folder for app
        try {
            val f = File(Environment.getExternalStorageDirectory().toString() + nameStogre)
            if (!f.exists()) f.mkdir()
        }
        catch (e : Exception){
            Toast.makeText(applicationContext, "Xảy ra lỗi ở đây", Toast.LENGTH_SHORT).show()
        }

        initView()


        btnBackup!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                btnBackup!!.isEnabled = false
                getVcardString()
            }
        })
    }


    private fun getContactList() : ArrayList<Contacts>? {
        var list: ArrayList<Contacts> = ArrayList()

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

    override fun onPermissionsGranted(requestCode: Int) {

    }

    private fun getVcardString(){
            try {

                var nameFile : String = UUID.randomUUID().toString()

                while (checkRandomNameFile(nameFile)){
                    nameFile = UUID.randomUUID().toString()
                }

                val textFile = File(Environment.getExternalStorageDirectory().toString() + nameStogre, nameFile + ".VCF")
                val mFileOutputStream = FileOutputStream(textFile)
                val w = BufferedWriter(OutputStreamWriter(mFileOutputStream, StandardCharsets.UTF_8))

                var listContacts: ArrayList<Contacts> = getContactList()!!

                for (contacts: Contacts in listContacts) {
                    w.write("BEGIN:VCARD\r\n")

                    w.write("VERSION:3.0\r\n")
                    w.write("FN:" + contacts.name.toString() + "\r\n")
                    w.write("TEL;TYPE=WORK,VOICE:" + contacts.numberPhone + "\r\n")
                    w.write("EMAIL;TYPE=PREF,INTERNET:" + contacts.email + "\r\n")

                    w.write("END:VCARD\r\n")
                }
                Toast.makeText(applicationContext, "Backup Complete", Toast.LENGTH_SHORT).show()
                w.close()
                mFileOutputStream.close()

                val cal: Calendar = Calendar.getInstance();
                var timeCreat = sdf.format(cal.time)

                val fileInformation = File(Environment.getExternalStorageDirectory().toString() + nameStogre, nameFile + ".txt")
                //var out = openFileOutput(fileInformation.toString(), Context.MODE_PRIVATE)
                val out = FileOutputStream(fileInformation)
                val w2 = BufferedWriter(OutputStreamWriter(out, StandardCharsets.UTF_8))
                w2.write(listContacts.size.toString() + "/" + timeCreat)

                w2.close()
                out.close()

                FileRecent(timeCreat)
                listHistory?.add(0, BackupContacts(listContacts.size, timeCreat, nameFile+".VCF"))
                historyAdapter!!.notifyDataSetChanged()
                btnBackup!!.isEnabled = true
            }
            catch (e : Exception){
                Toast.makeText(applicationContext, "Plese allow pessminsion", Toast.LENGTH_SHORT).show()
                btnBackup!!.isEnabled = true
            }
    }

        fun FileRecent(time : String){
            val fileInformation = File(Environment.getExternalStorageDirectory().toString() + nameStogre, "recent.txt")
            //var out = openFileOutput(fileInformation.toString(), Context.MODE_PRIVATE)
            val out = FileOutputStream(fileInformation)
            val w2 = BufferedWriter(OutputStreamWriter(out, StandardCharsets.UTF_8))
            w2.write(time)
            w2.close()
            out.close()
        }

    fun checkRandomNameFile(name : String) : Boolean{
        var listName : ArrayList<String> = getListNameFile()

        if(listName == null) return  false
        for(i : String in listName){
            if (i == name) return  true
        }
        return false
    }

    fun getListNameFile() : ArrayList<String>{
        var results : ArrayList<String> = ArrayList<String>();
        var files : Array<File>? = File(Environment.getExternalStorageDirectory().toString() + nameStogre).listFiles()

        for ( file in files!!) {
            var index : Int = file.toString().lastIndexOf("/")
            var nameFile : String = file.toString().substring(index+1)
            results.add(nameFile);
        }
        return results
    }



    fun checkPermission(permission: String): Boolean {
        val check = ContextCompat.checkSelfPermission(this, permission)
        return check == PackageManager.PERMISSION_GRANTED
    }



    private fun initView() {

        var results : ArrayList<String> = ArrayList<String>();
        var files : Array<File>? = File(Environment.getExternalStorageDirectory().toString() + nameStogre).listFiles()

        if(files != null) {
            for (file in files!!) {

                var index: Int = file.toString().lastIndexOf("/")
                var nameFile: String = file.toString().substring(index + 1)
                results.add(nameFile);

                var indexPoint = nameFile.lastIndexOf(".")
                var format: String = nameFile.substring(indexPoint + 1)

                if (format == "txt" && nameFile != "recent.txt") {
                    try {
                        val input = FileInputStream(file)
                        val r = BufferedReader(InputStreamReader(input, StandardCharsets.UTF_8))

                        var data: String = r.readLine()
                        var i: Int = data.lastIndexOf("/")
                        var num = data.substring(0, i)
                        var time: String = data.substring(i + 1)

                        listHistory?.add(BackupContacts(num.toInt(), time, nameFile))
                    }
                    catch (e : Exception){
                        Toast.makeText(applicationContext, "Xảy ra lỗi ở đây", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }




        if(historyAdapter == null) {
            Collections.reverse(listHistory);
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

       /* fun setItemClickListener(itemClickListener : ItemClickListener) {
            itemClickListener = itemClickListener
        }*/

        override fun onClick(v: View?) {

            //itemClickListener?.onClick(v!!, adapterPosition, false)
            var path : String = Environment.getExternalStorageDirectory().toString() + nameStogre + "/"+ newBackupContact!!.urlShare
            ShareFile(path)
            //Toast.makeText(applicationContext, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    fun ShareFile(myFilePath : String){
        var intentShareFile = Intent(Intent.ACTION_SEND);
        var fileWithinMyDir =  File(myFilePath);

        if(fileWithinMyDir.exists()) {
        intentShareFile.setType("application/pdf");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));

        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File...");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

        startActivity(Intent.createChooser(intentShareFile, "Share File"));
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
