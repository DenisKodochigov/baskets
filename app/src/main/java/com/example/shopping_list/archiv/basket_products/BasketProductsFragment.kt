package com.example.shopping_list.archiv.basket_products

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.transition.TransitionInflater
//import com.example.shopping_list.App
//import com.example.shopping_list.R
//import com.example.shopping_list.databinding.FragmentBasketProductsBinding
//import com.example.shopping_list.entity.Product
//import com.example.shopping_list.ui.adapter.ListAdapterAny
//import com.example.shopping_list.ui.basket_products.bottom_sheet_add_product.AddProductToBasketDialogFragment
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
//@AndroidEntryPoint
//class BasketProductsFragment: Fragment()  {
//    private var _binding: FragmentBasketProductsBinding? = null
//    private val binding get() = _binding!!
//    private val viewModel: BasketProductsViewModel by viewModels()
//    private val adapter = ListAdapterAny { product -> onItemClick(product as Product) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val inflater = TransitionInflater.from(requireContext())
//        enterTransition = inflater.inflateTransition(R.transition.slide_right)
//        exitTransition = inflater.inflateTransition(R.transition.fade)
//
//    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentBasketProductsBinding.inflate(inflater, container, false)
//        App.basket?.let { viewModel.getListProduct(it) }
//        return binding.root
//    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
//            showBottomSheetNewBasket()
//        }
//        viewModel.getGroupsWithProduct()
//        binding.productList.adapter = adapter
//        binding.productList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
//        viewModel.items.onEach {
//            adapter.setList(it)
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//    }
//
//    private fun showBottomSheetNewBasket(){
//        AddProductToBasketDialogFragment()
//            .show((activity as AppCompatActivity?)!!.supportFragmentManager, "tag")
//    }
//    private fun onItemClick(product: Product) {
//        viewModel.putProduct()
////        findNavController().navigate(R.id.action_nav_box_to_nav_product)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}