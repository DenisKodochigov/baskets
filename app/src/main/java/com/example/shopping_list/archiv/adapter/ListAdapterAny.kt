package com.example.shopping_list.archiv.adapter

//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.shopping_list.R
//import com.example.shopping_list.data.room.tables.BasketDB
//import com.example.shopping_list.data.room.tables.ProductDB
//import com.example.shopping_list.databinding.ItemBoxBinding
//import com.example.shopping_list.databinding.ItemProductBinding
//import com.example.shopping_list.entity.Product
//import javax.inject.Inject
//
//class ListAdapterAny @Inject constructor( private val onClick: (Any) -> Unit,
//    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private var items: List<Any> = emptyList()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setList(listItems: List<Any>) {
//        items = listItems
//        notifyDataSetChanged()
//    }
//    override fun getItemViewType(position: Int): Int {
//        return when (items[position]) {
//            is BasketDB -> R.layout.item_box        // The list of collections with checkbox
//            is Product -> R.layout.item_product  // The list of years // The list of images
//            else -> R.layout.item_product              // The list of country or genres
//        }
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            R.layout.item_box -> BoxVH(
//                ItemBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            R.layout.item_product -> ProductVH(
//                ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//            else -> throw IllegalArgumentException("Unsupported layout") // in case populated with a model we don't know how to display.
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = items.getOrNull(position)
//        when (holder) {
//            is BoxVH -> {
//                if (item is BasketDB) {
//                    holder.binding.tvName.text = item.basketName ?: ""
//                    holder.binding.tvName.setOnClickListener { onClick(item) }
//                }
//            }
//            is ProductVH -> {
//                if (item is ProductDB) {
//                    holder.binding.checkBox.text = item.product?.name ?: ""
//                    holder.binding.checkBox.setOnClickListener { onClick(item) }
//                }
//            }
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//}
//
//class BoxVH(val binding: ItemBoxBinding): RecyclerView.ViewHolder(binding.root)
//class ProductVH(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)