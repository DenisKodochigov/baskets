//package com.example.shopping_list.ui.basket_products
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.shopping_list.data.DataRepository
//import com.example.shopping_list.data.room.tables.BasketDB
//import com.example.shopping_list.data.room.tables.GroupWithProducts
//import com.example.shopping_list.data.room.tables.ProductDB
//import com.example.shopping_list.entity.ErrorApp
//import com.example.shopping_list.entity.Product
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class BasketProductsViewModel @Inject constructor(
//    private val errorApp: ErrorApp,
//    private val dataRepository: DataRepository) : ViewModel() {
//
//    private var _items = MutableStateFlow(emptyList<ProductDB>())
//    var items = _items.asStateFlow()
//    private var _groups = MutableStateFlow(emptyList<String>())
//    var groups = _groups.asStateFlow()
//    private var _units = MutableStateFlow(emptyList<String>())
//    var units = _units.asStateFlow()
//    private var _groupsWithProduct = MutableStateFlow(emptyList<GroupWithProducts>())
//    var groupsWithProduct = _groupsWithProduct.asStateFlow()
//
//    fun getListProduct(basket: BasketDB) {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getBasketProducts(basket)
//            }.fold(
//                onSuccess = { _items.value = it },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
//    fun getGroups() {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getGroups()
//            }.fold(
//                onSuccess = { _groups.value = it },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
//    fun getUnits() {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getUnits()
//            }.fold(
//                onSuccess = { _units.value = it },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
//    fun putProduct() {
//    }
//
//    fun addGroup(groupName: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.addGroup(groupName)
//            }.fold(
//                onSuccess = {getGroupsWithProduct() },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
//    fun getGroupsWithProduct(){
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getGroupsWithProduct()
//            }.fold(
//                onSuccess = { _groupsWithProduct.value = it },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
//}