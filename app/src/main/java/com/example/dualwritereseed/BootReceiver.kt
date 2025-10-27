package com.example.dualwritereseed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val internalUri = PrefsHelper.getInternalUri(context)
            val externalUri = PrefsHelper.getExternalUri(context)
            if (internalUri != null && externalUri != null) {
                SecureFileManager.syncFiles(context, internalUri, externalUri)
            }
        }
    }
}