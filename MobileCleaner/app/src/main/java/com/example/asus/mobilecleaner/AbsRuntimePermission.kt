package com.example.asus.mobilecleaner

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.SparseIntArray
import android.view.View

abstract class AbsRuntimePermission : AppCompatActivity() {
    private var mErrorString: SparseIntArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mErrorString = SparseIntArray()

    }

    abstract fun onPermissionsGranted(requestCode: Int)

    fun requestAppPermissions(requestedPermissions: Array<String>, stringId: Int, requestCode: Int) {
        mErrorString!!.put(requestCode, stringId)

        var permissionCheck = PackageManager.PERMISSION_GRANTED
        var showRequestPermissions = false
        for (permission in requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission)
            showRequestPermissions = showRequestPermissions || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (showRequestPermissions) {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE).setAction("GRANT") { ActivityCompat.requestPermissions(this@AbsRuntimePermission, requestedPermissions, requestCode) }.show()
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode)
            }
        } else {
            onPermissionsGranted(requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permisson in grantResults) {
            permissionCheck = permissionCheck + permisson
        }

        if (grantResults.size > 0 && PackageManager.PERMISSION_GRANTED == permissionCheck) {
            onPermissionsGranted(requestCode)
        } else {
            //Display message when contain some Dangerous permisson not accept
            Snackbar.make(findViewById(android.R.id.content), mErrorString!!.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE") {
                val i = Intent()
                i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                i.data = Uri.parse("package:$packageName")
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                startActivity(i)
            }.show()
        }
    }
}