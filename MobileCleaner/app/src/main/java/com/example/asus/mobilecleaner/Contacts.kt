package com.example.asus.mobilecleaner

import android.net.Uri

/**
 * Created by leo on 5/17/2018.
 */
class Contacts(id : String, src : Uri?, number : String,ten : String, count : Int, mail : String?) {
    var srcImg : Uri? = null
    var numberPhone : String? = null
    var numberCount : Int? = null
    var email : String? = null
    var name : String? = null
    var idContacts : String? = null

    init {
        idContacts = id
        name = ten
        srcImg = src
        numberPhone = number
        numberCount = count
        email = mail
    }

}