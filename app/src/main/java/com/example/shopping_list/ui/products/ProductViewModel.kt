package com.example.shopping_list.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.Product
import com.example.shopping_list.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel  @Inject constructor(
    private val errorApp: ErrorApp,
    private val dataRepository: DataRepository
): ViewModel() {

    private val _productsScreenState = MutableStateFlow(ProductsScreenState())
    val productsScreenState: StateFlow<ProductsScreenState> = _productsScreenState.asStateFlow()

    fun getStateProducts(basketId: Long){
        getProductsOnStart(basketId)
        getArticles()
        getSections()
        getUnits()
        getNameBasket(basketId)
    }

    private fun getProductsOnStart(basketId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListProducts(basketId) }.fold(
                onSuccess = { _productsScreenState.update { currentState ->
                    currentState.copy( products = it ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListArticle() }.fold(
                onSuccess = { _productsScreenState.update { currentState ->
                    currentState.copy( articles = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getSections() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getSections() }.fold(
                onSuccess = { _productsScreenState.update { currentState ->
                    currentState.copy( sections = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getUnits() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _productsScreenState.update { currentState -> currentState.copy( unitApp = it) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    private fun getNameBasket(basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getNameBasket(basketId) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(nameBasket = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun addProduct(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.addProduct(product, basketId) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(products = it) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
        getArticles()
        getSections()
        getUnits()
    }

    fun putProductInBasket(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.putProductInBasket(product, basketId) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(products = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeProduct(product: Product, basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeProductInBasket(product, basketId) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(products = it) } },
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun changeSectionSelected(productList: List<Product>, idSection: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeSectionSelectedProduct(productList, idSection) }.fold(
                onSuccess = {
                    _productsScreenState.update{ currentState -> currentState.copy(products = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
        getArticles()
    }

    fun deleteSelectedProducts(productList: List<Product>){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteSelectedProduct(productList) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(products = it) }},
                onFailure = {
                    errorApp.errorApi(it.message!!)}
            )
        }
    }
}