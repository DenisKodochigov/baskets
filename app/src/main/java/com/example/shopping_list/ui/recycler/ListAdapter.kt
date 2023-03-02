package com.example.shopping_list.ui.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping_list.R
import com.example.shopping_list.databinding.ItemBoxBinding
import com.example.shopping_list.databinding.ItemProductBinding
import com.example.shopping_list.entity.Box
import com.example.shopping_list.entity.ModeViewer
import com.example.shopping_list.entity.Product
import javax.inject.Inject

class ListAdapter@Inject constructor(
    private val mode: ModeViewer = ModeViewer.BOX, //For what context is the adapter called
    private val onClick: (Any) -> Unit,      //Call back on click item recyclerview
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(listItems: List<Any>) {
        items = listItems
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Box -> R.layout.item_box        // The list of collections with checkbox
            is Product -> R.layout.item_product  // The list of years // The list of images
            else -> R.layout.item_product              // The list of country or genres
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_box -> BoxVH(
                ItemBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.item_product -> ProductVH(
                ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Unsupported layout") // in case populated with a model we don't know how to display.
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        when (holder) {
            is BoxVH -> {
                if (item is Box) {
                    holder.binding.tvName.text = item.name ?: ""
                    holder.binding.tvName.setOnClickListener { onClick(item) }
                }
            }
            is ProductVH -> {
                if (item is Product) {
                    holder.binding.tvName.text = item.name
                    holder.binding.tvName.setOnClickListener { onClick(item) }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class BoxVH(val binding: ItemBoxBinding): RecyclerView.ViewHolder(binding.root)
class ProductVH(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)