package com.example.textrecoginition.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecoginition.databinding.ItemTextBinding

class ListAdapter(private val data: List<String>, private val onItemClicked: (String) -> Unit) :
    RecyclerView.Adapter<ListAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(
            ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.apply {
            val text = data[position]
            bind(text)
            itemView.setOnClickListener {
                onItemClicked.invoke(text)
            }
        }
    }

    class TextViewHolder(private val binding: ItemTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.textView.text = item
        }
    }
}