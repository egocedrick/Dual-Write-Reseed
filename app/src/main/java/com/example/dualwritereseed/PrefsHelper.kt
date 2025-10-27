package com.example.dualwritereseed

import android.content.Context
import android.content.Intent
import android.net.Uri

object PrefsHelper {
    private const val PREFS_NAME = "dualwrite_prefs"
    private const val KEY_INTERNAL_URI = "internal_uri"
    private const val KEY_EXTERNAL_URI = "external_uri"

    fun saveInternalUri(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_INTERNAL_URI, uri.toString()).apply()
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    fun saveExternalUri(context: Context, uri: Uri) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_EXTERNAL_URI, uri.toString()).apply()
        context.contentResolver.takePersistableUriPermission(
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    fun getInternalUri(context: Context): Uri? {
        val uriString = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_INTERNAL_URI, null)
        return uriString?.let { Uri.parse(it) }
    }

    fun getExternalUri(context: Context): Uri? {
        val uriString = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_EXTERNAL_URI, null)
        return uriString?.let { Uri.parse(it) }
    }
}