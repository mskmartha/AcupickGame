package com.example.acupickapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acupickapp.Model.Quote
import com.example.acupickapp.databinding.ItemQuoteBinding

class QuoteAdapter(
    private val quotes: List<Quote>,
    private val onItemClick: (Quote) -> Unit
) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = quotes[position]
        holder.binding.textTitle.text = quote.title
        holder.binding.textBody.text = quote.body

        holder.itemView.setOnClickListener {
            onItemClick(quote)
        }
    }

    override fun getItemCount(): Int = 5
}