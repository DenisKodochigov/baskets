package com.example.shopping_list.ui

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
import android.util.Log
import com.example.shopping_list.entity.Basket

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
                        currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun changeNameBasket(basket: Basket){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeNameBasket(basket) }.fold(
                onSuccess = { _stateBasketScreen.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun deleteBasket(basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteBasket(basketId) }.fold(
                onSuccess = { _stateBasketScreen.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun getStateProducts(basketId: Long){
        getListProducts(basketId)
        getListArticle()
        getListGroup()
        getListUnit()
    }

    private fun getListProducts(basketId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListProducts(basketId) }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListArticle() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle() }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(articles = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListGroup() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getGroups() }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(group = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getListUnit() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = { _stateProductsScreen.update { currentState ->
                    currentState.copy(unitA = it) } },
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
                    currentState.copy(baskets = it as MutableList<Basket>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun addProduct(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.addProduct(product, basketId)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
        getListArticle()
        getListGroup()
        getListUnit()
    }

    fun putProductInBasket(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.putProductInBasket(product, basketId)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun setPositionProductInBasket(products: List<Product>, direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.setPositionProductInBasket(products, direction)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun setPositionBasket(baskets: List<Basket>, direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.setPositionBasket(baskets, direction)
            }.fold(
                onSuccess = {_stateBasketScreen.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeProductInBasket(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeProductInBasket(product, basketId)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun newArticle(name: Pair<Long,String>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.newArticle(name.second)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(articles = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeGroupSelected(productList: MutableList<Product>, idGroup: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.changeGroupSelected(productList, idGroup) }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun deleteSelected(productList: MutableList<Product>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.deleteSelectedProduct(productList)
            }.fold(
                onSuccess = {_stateProductsScreen.update { currentState ->
                    currentState.copy(products = it as MutableList<Product>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}