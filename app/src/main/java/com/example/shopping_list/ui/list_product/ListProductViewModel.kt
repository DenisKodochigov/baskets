package com.example.shopping_list.ui.list_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListProductViewModel @Inject constructor(): ViewModel() {

    private val dataRepository = DataRepository()
    private var _items = MutableStateFlow(emptyList<Product>())
    var items = _items.asStateFlow()

    fun getListProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.getProducts()
            }.fold(
                onSuccess = {_items.value = it },
                onFailure = { ErrorApp().errorApi(it.message!!)}
            )
        }
    }
    fun putProduct(product: Product){

    }
}