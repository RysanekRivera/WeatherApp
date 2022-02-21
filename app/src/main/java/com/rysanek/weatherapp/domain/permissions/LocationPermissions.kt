package com.rysanek.weatherapp.domain.permissions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

/**
 * Checks whether or not the location permission is granted.
 * **/
fun Activity.isLocationPermissionGranted() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

/**
 * handles if the user has denied **/
fun Activity.handlePermissionDenials(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        //NO-OP
    } else {
        if (permissions.isNotEmpty())
            permissions.forEach { permission -> showRationaleIfNeeded(permission, requestCode) }
    }
}

fun Activity.showRationaleIfNeeded(permission: String, requestCode: Int) {
    if (shouldShowRequestPermissionRationale(permission)) {
        AlertDialog.Builder(this)
            .setTitle("$permission Needed")
            .setMessage("Accept $permission Permission")
            .setPositiveButton("Accept") { _, _ ->
                requestPermissions(arrayOf(permission), requestCode)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
            .show()
    } else {
        AlertDialog.Builder(this)
            .setTitle("Permission Needed")
            .setMessage("Permissions are needed for proper functionality of the app.")
            .setPositiveButton("Settings") { _, _ ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    data = Uri.fromParts("package", packageName, null)
                    startActivity(this)
                }
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .create()
            .show()
    }
}
