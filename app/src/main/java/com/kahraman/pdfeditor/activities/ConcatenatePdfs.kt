package com.kahraman.pdfeditor.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kahraman.pdfeditor.R
import com.kahraman.pdfeditor.adapters.PdfAdapter
import com.kahraman.pdfeditor.classes.PdfUtils
import com.kahraman.pdfeditor.models.PdfItem
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import java.io.File
import kotlin.io.copyTo
import kotlin.io.outputStream
import kotlin.io.use

class ConcatenatePdfs : AppCompatActivity() {

    private val selectedPdfItems = mutableListOf<PdfItem>()
    private lateinit var adapter: PdfAdapter

    private val pickPdf = registerForActivityResult( // Registers a file picker
        ActivityResultContracts.OpenMultipleDocuments() //  Allows picking multiple files
    ) { uris -> // reference to a resource - so that the app can access the file's data without needing its actual file path.
        if (uris != null) {
            for (uri in uris) {
                val name = getFileNameFromUri(this, uri)
                selectedPdfItems.add(PdfItem(uri, name ?: "Unnamed.pdf"))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_concatenate_pdfs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAdd = findViewById<Button>(R.id.btnAddPdf)
        val btnMerge = findViewById<Button>(R.id.btnMergePdf)
        val recyclerView = findViewById<RecyclerView>(R.id.pdfListRecyclerView)

        adapter = PdfAdapter(selectedPdfItems) { itemToDelete ->
            selectedPdfItems.remove(itemToDelete)
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAdd.setOnClickListener {
            pickPdf.launch(arrayOf("application/pdf"))
        }

        btnMerge.setOnClickListener {
            PDFBoxResourceLoader.init(applicationContext);
            mergePdfs()
        }
    }

    private fun mergePdfs() {
        val filesToMerge = mutableListOf<File>()

        for (item in selectedPdfItems) {
            try {
                val inputStream = contentResolver.openInputStream(item.uri)
                val tempFile = File.createTempFile("temp_pdf_", ".pdf", cacheDir)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                filesToMerge.add(tempFile)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to read PDF: ${item.name}", Toast.LENGTH_SHORT).show()
            }
        }

        val outputFile = PdfUtils.getPublicPdfFile(this, "merged.pdf")
        PdfUtils.mergePdfs(this, filesToMerge, outputFile)

        Toast.makeText(this, "Merged to: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()
    }
}