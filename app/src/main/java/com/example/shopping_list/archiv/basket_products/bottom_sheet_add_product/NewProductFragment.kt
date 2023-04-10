//package com.example.shopping_list.archiv.basket_products.bottom_sheet_add_product
//
//import android.content.Context
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import com.example.shopping_list.R
//import com.example.shopping_list.ui.basket_products.BasketProductsViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//
//
//@AndroidEntryPoint
//class NewProductFragment: Fragment(R.layout.bottom_sheet_new_product) {
//    companion object {
//        fun newInstance() = NewProductFragment()
//    }
//    private val viewModel: BasketProductsViewModel by viewModels()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        view.findViewById<View>(R.id.back_button)
//            .setOnClickListener { gotoFragment { goBack() } }
//
////        val spinnerGroup = view.findViewById<Spinner>(R.id.sp_group)
////        val spinnerUnit = view.findViewById<Spinner>(R.id.sp_unit)
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        viewModel.getGroups()
//        viewModel.getUnits()
//        viewModel.groups.onEach { groups ->
//            context?.let {
////                getSpinner(groups.toTypedArray(),spinnerGroup, it)
////                val adapter = ArrayAdapter(it, R.layout.dropdown_menu_popup_item, groups)
////                val editTextFilledExposedDropdown: AutoCompleteTextView = view.findViewById(R.id.filled_exposed_dropdown)
////                editTextFilledExposedDropdown.setAdapter(adapter)
//            }
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//        viewModel.units.onEach { units ->
//            context?.let {
////                getSpinner(units.toTypedArray(),spinnerUnit, it)
//            }
//        }.launchIn(viewLifecycleOwner.lifecycleScope)
//
//
//
//
//    }
//    private fun getSpinner(array: Array<String>, spinnerView: Spinner, context: Context){
//
//        ArrayAdapter(context, android.R.layout.simple_spinner_item, array).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinnerView.adapter = adapter
//        }
//
//        spinnerView.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
//            {
//                Toast.makeText(context, "Selected position $position", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) { }
//        }
//    }
//
//}