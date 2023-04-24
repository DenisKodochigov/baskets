package com.example.shopping_list.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.baskets.StateBasketScreen
import com.example.shopping_list.ui.products.StateProductsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val errorApp: ErrorApp,
    private val dataRepository: DataRepository
): ViewModel() {

    private val _stateBasketScreen = MutableStateFlow(StateBasketScreen())
    val stateBasketScreen: StateFlow<StateBasketScreen> = _stateBasketScreen.asStateFlow()
    private val _stateProductsScreen = MutableStateFlow(StateProductsScreen())
    val stateProductsScreen: StateFlow<StateProductsScreen> = _stateProductsScreen.asStateFlow()

    fun getListBasket() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListBasket() }.fold(
                onSuccess = { _stateBasketScreen.update { currentState ->
                        currentState.copy(baskets = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun getListProducts(basketId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListProducts(basketId) }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(products = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun newBasket(basketName: String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.newBasket(basketName)
            }.fold(
                onSuccess = {_stateBasketScreen.update { currentState ->
                    currentState.copy(baskets = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
    fun newProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.newProduct(product)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun newArticle(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.newArticle(name)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(articles = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
    fun getListArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle() }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(articles = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
}