//package com.example.shopping_list.archiv.basket_products.bottom_sheet_add_product
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import android.view.MotionEvent
//import android.view.View
//import android.widget.EditText
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.viewpager2.widget.ViewPager2
//import com.example.shopping_list.R
//import com.example.shopping_list.data.room.tables.GroupWithProducts
//import com.example.shopping_list.ui.adapter.ViewerPageAdapter
//import com.example.shopping_list.ui.basket_products.BasketProductsViewModel
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayoutMediator
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
//@AndroidEntryPoint
//class AddProductFragment: Fragment(R.layout.bottom_sheet_add_product) {
//
//    companion object {
//        fun newInstance() = AddProductFragment()
//    }
//    private val viewModel: BasketProductsViewModel by viewModels()
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val etAddGroup = view.findViewById<EditText>(R.id.et_add_group)
//        val btAddProduct = view.findViewById<EditText>(R.id.bt_add_products)
//
//        etAddGroup.setOnTouchListener { _, event ->
//            val drawableRight = 2
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                if (event.rawX >= (etAddGroup.right - etAddGroup.paddingEnd -
//                            etAddGroup.compoundDrawables[drawableRight].bounds.width())) {
//                    Log.d("KDS", "Click add group")
//                    if (!etAddGroup.text.isNullOrEmpty()) viewModel.addGroup(etAddGroup.text.toString())
//                    true
//                }
//            }
//            false
//        }
//        btAddProduct.setOnClickListener {
//            gotoFragment { goToNewProductFragment() }
//        }
//        viewModel.groupsWithProduct.onEach {
//            processingTabLayout(it, view)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//    }
//
//    private fun processingTabLayout(group: List<GroupWithProducts>, view: View) {
//
//        val viewpager = view.findViewById<ViewPager2>(R.id.viewpager)
//        val tabs = view.findViewById<TabLayout>(R.id.tabs)
//        val adapter = ViewerPageAdapter  ({ onClickViewPager() }, requireContext())
//        adapter.setList(group)
//
//        viewpager.adapter = adapter
//        viewpager.currentItem = 0
//        TabLayoutMediator(tabs, viewpager) { tab, position ->
//            tab.text = group[position].group.nameGroup
//        }.attach()
//    }
//    private fun onClickViewPager() {
////        viewModel.putFilm()
////        findNavController().navigate(R.id.action_nav_gallery_to_nav_viewer_image)
//    }
//}