package com.example.dualwritereseed

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile

object SecureFileManager {

    private val FILES = listOf(
        "",
        "",
        ""
    )

    fun syncFiles(context: Context, internalUri: Uri, externalUri: Uri) {
        val internalTree = DocumentFile.fromTreeUri(context, internalUri) ?: return
        val externalTree = DocumentFile.fromTreeUri(context, externalUri) ?: return

        FILES.forEach { name ->
            val internalDoc = internalTree.findFile(name)
            val externalDoc = externalTree.findFile(name)

            if ((internalDoc == null || !internalDoc.exists()) &&
                externalDoc != null && externalDoc.exists()
            ) {
                val data = readFile(context, externalDoc.uri)
                if (data != null && data.isNotEmpty()) {
                    writeFile(context, internalTree, name, data)
                    Log.d("SecureFileManager", "Restored INTERNAL: $name")
                }
            }

            if ((externalDoc == null || !externalDoc.exists()) &&
                internalDoc != null && internalDoc.exists()
            ) {
                val data = readFile(context, internalDoc.uri)
                if (data != null && data.isNotEmpty()) {
                    writeFile(context, externalTree, name, data)
                    Log.d("SecureFileManager", "Reseeded EXTERNAL: $name")
                }
            }
        }
    }

    private fun readFile(context: Context, uri: Uri): ByteArray? =
        try { context.contentResolver.openInputStream(uri)?.use { it.readBytes() } }
        catch (e: Exception) { e.printStackTrace(); null }

    private fun writeFile(context: Context, tree: DocumentFile, name: String, data: ByteArray) {
        try {
            val target = tree.findFile(name) ?: tree.createFile("application/octet-stream", name)
            target?.uri?.let { uri ->
                context.contentResolver.openOutputStream(uri)?.use { os -> os.write(data) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}