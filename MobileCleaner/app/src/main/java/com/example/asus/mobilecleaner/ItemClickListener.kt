package com.example.asus.mobilecleaner

import android.view.View

interface ItemClickListener {

    fun onClick(view: View, position: Int, isLongClick: Boolean)
}