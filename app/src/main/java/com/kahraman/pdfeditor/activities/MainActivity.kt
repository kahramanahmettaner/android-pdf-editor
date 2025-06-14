package com.kahraman.pdfeditor.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kahraman.pdfeditor.activities.Example
import com.kahraman.pdfeditor.R

class MainActivity : AppCompatActivity() {

    private val itemList = listOf("PDF Viewer", "Concatenate PDFs", "Split PDF", "PDF From Images", "Create PDF");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val listView: ListView = findViewById(R.id.listView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = when (itemList[position]) {
                "PDF Viewer" -> Intent(this, Example::class.java)
                "Concatenate PDFs" -> Intent(this, ConcatenatePdfs::class.java)
                "Split PDF" -> Intent(this, Example::class.java)
                "PDF From Images" -> Intent(this, Example::class.java)
                "Create PDF" -> Intent(this, Example::class.java)
                else -> Intent(this, Example::class.java)
            }
            startActivity(intent)

        }
    }
}