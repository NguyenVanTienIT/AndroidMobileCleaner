package com.example.asus.mobilecleaner

/**
 * Created by leo on 5/17/2018.
 */
class Contacts(id : String, src : String, number : String,ten : String, count : Int, mail : String) {
    var srcImg : String? = null
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