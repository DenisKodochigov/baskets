//package com.example.shopping_list.ui.list_basket
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.shopping_list.App
//import com.example.shopping_list.data.DataRepository
//import com.example.shopping_list.data.room.tables.BasketDB
//import com.example.shopping_list.entity.ErrorApp
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ListBasketViewModel @Inject constructor(
//    private val errorApp: ErrorApp,
//    private val dataRepository: DataRepository): ViewModel() {
//
//    private var _items = MutableStateFlow(emptyList<BasketDB>())
//    var items = _items.asStateFlow()
//    private var _newBasket = MutableStateFlow(0L)
//    var newBasket = _newBasket.asStateFlow()
//
//    fun getListBasket() {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getListBasket()
//            }.fold(
//                onSuccess = {_items.value = it },
//                onFailure = { errorApp.errorApi(it.message!!)}
//            )
//        }
//    }
//    fun putBasket(basket: BasketDB){
//        App.basket = basket
//    }
//    fun addNewBasket(basketName: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.addBasket(basketName)
//            }.fold(
//                onSuccess = {_newBasket.value = it },
//                onFailure = { errorApp.errorApi(it.message!!)}
//            )
//        }
//    }
//}