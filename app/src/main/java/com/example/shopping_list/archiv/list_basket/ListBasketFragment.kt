//package com.example.shopping_list.ui.list_basket
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.transition.TransitionInflater
//import com.example.shopping_list.R
//import com.example.shopping_list.data.room.tables.BasketDB
//import com.example.shopping_list.databinding.FragmentListBasketBinding
//import com.example.shopping_list.ui.adapter.ListAdapterAny
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import java.util.*
//
//@AndroidEntryPoint
//class ListBasketFragment: Fragment() {
//
//    private var _binding: FragmentListBasketBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: ListBasketViewModel by viewModels()
//    private val adapter = ListAdapterAny { basket -> onItemClick(basket as BasketDB) }
//    private lateinit var bottomSheetDialog: BottomSheetDialog
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val inflater = TransitionInflater.from(requireContext())
//        enterTransition = inflater.inflateTransition(R.transition.slide_right)
//        exitTransition = inflater.inflateTransition(R.transition.fade)
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentListBasketBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel.getListBasket()
//        bottomSheetDialog = context?.let{ BottomSheetDialog(it, R.style.AppBottomSheetDialogTheme) }!!
//        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        binding.basketList.adapter = adapter
//        binding.basketList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
//        viewModel.items.onEach {
//            adapter.setList(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//
//        (activity as AppCompatActivity).findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
//            showBottomSheetNewBasket()
//        }
//    }
//
//    //The procedure for processing the selection of a country
//    @SuppressLint("UseCompatLoadingForDrawables", "CutPasteId")
//    private fun showBottomSheetNewBasket(){
//        //Connect the outlet to the bottom sheet
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet_new_basket)
//        bottomSheetDialog.findViewById<TextView>(R.id.et_new_basket)?.text =
//            Calendar.getInstance().time.toString()
//        bottomSheetDialog.show()
//        bottomSheetDialog.findViewById<Button>(R.id.bt_new_basket)?.setOnClickListener {
//            viewModel.addNewBasket(bottomSheetDialog.findViewById<TextView>(R.id.et_new_basket)?.text.toString())
//        }
//        viewModel.newBasket.onEach {
//            if (it != 0L) {
//                Log.d("KDS","$it")
//                viewModel.getListBasket()
////                bottomSheetDialog.cancel()
//            }
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//    }
//
//    private fun onItemClick(basket: BasketDB) {
//        viewModel.putBasket(basket)
//        findNavController().navigate(R.id.action_nav_basket_to_nav_basket_products)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}