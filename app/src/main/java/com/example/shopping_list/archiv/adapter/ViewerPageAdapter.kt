//package com.example.shopping_list.ui.adapter
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.shopping_list.App
//import com.example.shopping_list.R
//import com.example.shopping_list.data.room.tables.GroupWithProducts
//import com.example.shopping_list.data.room.tables.ProductDB
//import com.example.shopping_list.databinding.ItemViewerProductRecyclerBinding
//
//class ViewerPageAdapter (
//    private val onClick: (Any?) -> Unit,
//    private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
//
//    private var items: List<Any> = emptyList()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun setList(listItems: List<Any>) {
//        items = listItems
//        notifyDataSetChanged()
//    }
//    //Determining the size of the list
//    override fun getItemCount(): Int {
//        var size = 0
//        when (items[0]) {
//            is GroupWithProducts -> {
//                size = items.size
//            }
//        }
//        return size
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return GroupHV(ItemViewerProductRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (items[0]) {
//            is GroupWithProducts -> R.layout.item_viewer_product_recycler
//            else -> 0
//        }
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        when (holder) {
//            is GroupHV -> {
//                val item = items[position] as GroupWithProducts
//                val adapter = ListAdapterAny { onClickImagesAdapter(null) }
//
//                holder.binding.recyclerProduct.layoutManager = LinearLayoutManager(context,
//                    RecyclerView.HORIZONTAL,false)
//                holder.binding.recyclerProduct.adapter = adapter
//                //Creating a list corresponding to this tab
//                if (item.listProductDB.isEmpty()){
//                    adapter.setList(listOf(
//                        ProductDB(0,0,0,false, null),
//                        ProductDB(0,0,0,false, null)
//                    ))
//                }else{
//                    adapter.setList(item.listProductDB)
//                }
//            }
//        }
//    }
//
//    private fun onClickImagesAdapter(item: ProductDB?) {
//        onClick(item)
//    }
//}
//class GroupHV(val binding: ItemViewerProductRecyclerBinding): RecyclerView.ViewHolder(binding.root)
