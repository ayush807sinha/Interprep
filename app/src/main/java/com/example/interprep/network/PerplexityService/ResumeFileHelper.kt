package com.example.interprep.network.PerplexityService

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.*


object ResumeFileHelper {
    suspend fun extractTextFromPdf(context: Context, fileUri: Uri): String =
        withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
                if (inputStream == null) {
                    Log.e("ResumeFileHelper", "InputStream is null for URI: $fileUri")
                    return@withContext "Error: Unable to open file."
                }


                val tempFile = File(context.cacheDir, "resume_temp.pdf")
                FileOutputStream(tempFile).use { output ->
                    inputStream.copyTo(output)
                }

                PDDocument.load(tempFile).use { document ->
                    val stripper = PDFTextStripper()
                    return@withContext stripper.getText(document)
                }
            } catch (e: Exception) {
                Log.e("ResumeFileHelper", "PDF extraction failed: ${e.localizedMessage}")
                return@withContext "Error reading PDF file."
            }
        }

    fun getFileName(context: Context, uri: Uri): String {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (name == null) {
            name = uri.path
            val cut = name?.lastIndexOf('/')
            if (cut != null && cut != -1) name = name?.substring(cut + 1)
        }
        return name ?: "unknown_file.pdf"
    }
}
