//package com.example.shopping_list.exsample
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.TextView
//import com.example.shopping_list.R
//import com.example.shopping_list.data.room.tables.GroupWithProducts
//import com.example.shopping_list.databinding.BottomSheetAddProductBinding
//import com.example.shopping_list.ui.adapter.ViewerPageAdapter
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import com.google.android.material.tabs.TabLayoutMediator
//
//private const val COLLAPSED_HEIGHT = 228
//
//class AddProduct : BottomSheetDialogFragment() {
//
//    private var _binding: BottomSheetAddProductBinding? = null
//    private val binding get() = _binding!!
//
//
//    override fun getTheme() = R.style.AppBottomSheetDialogTheme
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?): View {
//        _binding = BottomSheetAddProductBinding.bind(
//            inflater.inflate(R.layout.bottom_sheet_add_product, container))
//        return binding.root
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        val density = requireContext().resources.displayMetrics.density
//        Log.d("KDS", "Start BottomSheetDialogFragment AddProduct")
//        dialog?.let {
//            // Находим сам bottomSheet и достаём из него Behaviour
//            val bottomSheet =
//                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//            val behavior = BottomSheetBehavior.from(bottomSheet)
//
//            // Выставляем высоту для состояния collapsed и выставляем состояние collapsed
//            behavior.peekHeight = (COLLAPSED_HEIGHT * density).toInt()
//            behavior.state = BottomSheetBehavior.STATE_EXPANDED
//
//            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {  }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    with(binding) {
//                        // Нас интересует только положительный оффсет, тк при отрицательном нас устроит стандартное поведение - скрытие фрагмента
//
//                        }
//                    }
//                }
//            )
//        }
//    }
//
//    private fun processingTabLayout(group: List<GroupWithProducts>) {
//        val adapter = ViewerPageAdapter ({ onClickViewPager() }, requireContext())
//        adapter.setList(group)
//        binding.viewpager.adapter = adapter
//        //Programmatically select the first tab.
//        binding.viewpager.currentItem = 0
//        //We fill in the tabs text fields
//        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
//            tab.setCustomView(R.layout.tab)
//            tab.customView?.findViewById<TextView>(R.id.tv_tab_name)?.text = group[position].group.nameGroup
////            tab.customView?.findViewById<TextView>(R.id.tv_tab_name)
////                ?.setTextColor(resources.getColor(R.color.black, null))
//        }.attach()
//        //We draw the tabs
////        binding.tabs.getTabAt(0)?.customView?.findViewById<ConstraintLayout>(R.id.linearlayout)
////            ?.setBackgroundResource(R.drawable.tab_selected_background)
////        binding.tabs.getTabAt(0)?.customView?.findViewById<TextView>(R.id.tv_tab_name)
////            ?.setTextColor(resources.getColor(R.color.white, null))
//        //Actions when activating a particular tabs
////        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
////            override fun onTabSelected(tab: TabLayout.Tab?) {
////                tab?.customView?.findViewById<ConstraintLayout>(R.id.linearlayout)
////                    ?.setBackgroundResource(R.drawable.tab_selected_background)
////                tab?.customView?.findViewById<TextView>(R.id.tv_gallery_tab_name)
////                    ?.setTextColor(resources.getColor(R.color.white, null))
////            }
////            override fun onTabUnselected(tab: TabLayout.Tab?) {
////                tab?.customView?.findViewById<ConstraintLayout>(R.id.linearlayout)
////                    ?.setBackgroundResource(R.drawable.tab_unselected_background)
////                tab?.customView?.findViewById<TextView>(R.id.tv_gallery_tab_name)
////                    ?.setTextColor(resources.getColor(R.color.black, null))
////            }
////            override fun onTabReselected(tab: TabLayout.Tab?) {}
////        })
//    }
//    private fun onClickViewPager() {
////        viewModel.putFilm()
////        findNavController().navigate(R.id.action_nav_gallery_to_nav_viewer_image)
//    }
//
//}
