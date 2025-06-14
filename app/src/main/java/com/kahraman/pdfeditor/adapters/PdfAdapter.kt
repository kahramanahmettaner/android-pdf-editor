package com.kahraman.pdfeditor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kahraman.pdfeditor.R
import com.kahraman.pdfeditor.models.PdfItem

class PdfAdapter(
    private val pdfItems: List<PdfItem>,
    private val onDeleteClick: (PdfItem) -> Unit
) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    inner class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfNameTextView: TextView = itemView.findViewById(R.id.pdfName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val pdfIcon: ImageView = itemView.findViewById(R.id.pdfIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false)
        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val item = pdfItems[position]
        holder.pdfNameTextView.text = item.name
        holder.deleteButton.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount() = pdfItems.size
}