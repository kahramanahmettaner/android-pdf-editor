package com.kahraman.pdfeditor.classes

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import com.tom_roush.pdfbox.pdmodel.PDDocument
import java.io.File
import java.io.IOException

object PdfUtils {

    fun getPublicPdfFile(context: Context, fileName: String): File {
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        if (downloadsDir != null && !downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }
        return File(downloadsDir, fileName)
    }

    fun mergePdfs(context: Context?, pdfFiles: MutableList<File>, outputFile: File) {
        val mergerUtility = PDFMergerUtility()
        mergerUtility.destinationFileName = outputFile.absolutePath

        // We'll store documents to close *after* saving the merged file
        val sourceDocuments: MutableList<PDDocument> = ArrayList<PDDocument>()

        try {
            for (file in pdfFiles) {
                val doc = PDDocument.load(file)
                mergerUtility.addSource(file)
                sourceDocuments.add(doc!!)
            }

            mergerUtility.mergeDocuments(null) // no MemoryUsageSetting on Android

            Toast.makeText(
                context,
                "PDFs merged to: " + outputFile.absolutePath,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error merging PDFs", Toast.LENGTH_SHORT).show()
        } finally {
            // Now it's safe to close all documents
            for (doc in sourceDocuments) {
                try {
                    doc.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}