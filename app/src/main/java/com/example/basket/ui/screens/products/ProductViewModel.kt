package com.example.basket.ui.screens.products

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basket.data.DataRepository
import com.example.basket.entity.ErrorApp
import com.example.basket.entity.Product
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
        getNameBasket(basketId)
    }

    private fun getProductsOnStart(basketId: Long) {
        templateMy( { dataRepository.getListProducts(basketId) },
            getArticles =true, getUnit = true, getSection = true )}

    private fun getNameBasket(basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getNameBasket(basketId) }.fold(
                onSuccess = {_productsScreenState.update { currentState ->
                    currentState.copy(nameBasket = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun addProduct(product: Product, basketId: Long){
        templateMy( { dataRepository.addProduct(product, basketId) },
            getArticles =true, getUnit = true, getSection = true ) }
    fun putProductInBasket(product: Product, basketId: Long){
        templateMy( { dataRepository.putProductInBasket(product, basketId) } )}
    fun changeProduct(product: Product, basketId: Long){
        templateMy ({ dataRepository.changeProductInBasket(product, basketId) },
            getArticles =true, getUnit = true, getSection = true) }
    fun changeSections(productList: List<Product>, idSection: Long){
        templateMy({ dataRepository.changeSectionSelectedProduct(productList, idSection)},
            getArticles =true)}
    fun deleteSelectedProducts(productList: List<Product>){
        templateMy( { dataRepository.deleteSelectedProduct(productList) } )}
    fun changeSelected(productId: Long){
        templateSelection { if (it.idProduct == productId) it.isSelected = !it.isSelected } }
    fun unSelected(){
        templateSelection { it.isSelected = false } }
    private fun templateSelection( impl: (Product) -> Unit ){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val listItems = productsScreenState.value.products.value
                listItems.forEach { list -> list.forEach { product -> impl(product) } }
                _productsScreenState.update { currentState ->
                    currentState.copy(products = mutableStateOf(listItems))
                }
            }
        }
    }
    private fun templateMy(
        funDataRepository:() -> List<List<Product>>,
        getArticles: Boolean = false,
        getUnit: Boolean = false,
        getSection: Boolean = false )
    {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { funDataRepository() }.fold(
                onSuccess = { _productsScreenState.update { currentState ->
                    currentState.copy(products = mutableStateOf(it) ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
            if (getArticles){
                kotlin.runCatching { dataRepository.getListArticle() }.fold(
                    onSuccess = { _productsScreenState.update { currentState ->
                        currentState.copy( articles = mutableStateOf(it)) } },
                    onFailure = { errorApp.errorApi(it.message!!) }
                )
            }
            if (getSection){
                kotlin.runCatching { dataRepository.getSections() }.fold(
                    onSuccess = { _productsScreenState.update { currentState ->
                        currentState.copy( sections = mutableStateOf(it)) } },
                    onFailure = { errorApp.errorApi(it.message!!) }
                )
            }
            if (getUnit){
                kotlin.runCatching { dataRepository.getUnits() }.fold(
                    onSuccess = {
                        _productsScreenState.update { currentState ->
                            currentState.copy( unitApp = mutableStateOf(it)) } },
                    onFailure = { errorApp.errorApi(it.message!!) }
                )
            }
        }
    }
}