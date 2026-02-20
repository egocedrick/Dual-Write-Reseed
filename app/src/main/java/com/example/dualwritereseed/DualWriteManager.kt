package com.example.dualwritereseed

import android.content.Context
import java.io.File
import java.security.MessageDigest

object DualWriteManager {

    private fun hashName(base: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val h = md.digest(base.toByteArray())
        return h.joinToString("") { "%02x".format(it) } + ".enc"
    }

    fun dualWrite(ctx: Context, bytes: ByteArray) {

        val a = File(ctx.filesDir, hashName("journal_a"))
        a.writeBytes(bytes)
        a.setReadOnly()

        ctx.getExternalFilesDir(null)?.let { ext ->
            val b = File(ext, hashName("journal_b"))
            b.writeBytes(bytes)
            b.setReadOnly()
        }
    }

    fun reseedIfMissing(ctx: Context) {
        val a = File(ctx.filesDir, hashName("journal_a"))
        val b = ctx.getExternalFilesDir(null)?.let { File(it, hashName("journal_b")) }

        when {
            a.exists() && b != null && !b.exists() -> b.writeBytes(a.readBytes())
            !a.exists() && b != null && b.exists() -> a.writeBytes(b.readBytes())
            else -> Unit
        }
    }
}