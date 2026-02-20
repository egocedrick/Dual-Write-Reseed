package com.example.dualwritereseed

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dualwritereseed.ui.theme.DualWriteReseedTheme

class MainActivity : ComponentActivity() {

    private var internalUri: Uri? = null
    private var externalUri: Uri? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pickInternalLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {

                    val takeFlags = (result.data?.flags ?: 0) and
                            (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    try {
                        contentResolver.takePersistableUriPermission(uri, takeFlags)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    PrefsHelper.saveInternalUri(this, uri)
                    internalUri = uri
                }
            }
        }

        val pickExternalLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    val takeFlags = (result.data?.flags ?: 0) and
                            (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    try {
                        contentResolver.takePersistableUriPermission(uri, takeFlags)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    PrefsHelper.saveExternalUri(this, uri)
                    externalUri = uri
                }
            }
        }

        internalUri = PrefsHelper.getInternalUri(this)
        externalUri = PrefsHelper.getExternalUri(this)

        setContent {
            DualWriteReseedTheme {
                var output by remember { mutableStateOf("") }

                Column(modifier = Modifier.padding(16.dp)) {
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                            addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                            )
                        }
                        pickInternalLauncher.launch(intent)
                    }) { Text("Pick Internal Folder") }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                            addFlags(
                                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                            )
                        }
                        pickExternalLauncher.launch(intent)
                    }) { Text("Pick External Folder") }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        if (internalUri != null && externalUri != null) {
                            SecureFileManager.syncFiles(this@MainActivity, internalUri!!, externalUri!!)
                            output = "Sync complete (bi-directional)"
                        } else {
                            output = "Pick both internal and external folders first"
                        }
                    }) { Text("Sync Now") }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Output: $output")
                }
            }
        }
    }
}