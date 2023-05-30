package com.example.shopping_list.ui.baskets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.ErrorApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val errorApp: ErrorApp,
    private val dataRepository: DataRepository
): ViewModel() {

    private val _BasketScreenState = MutableStateFlow(BasketScreenState())
    val basketScreenState: StateFlow<BasketScreenState> = _BasketScreenState.asStateFlow()

    fun getListBasket() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListBasket() }.fold(
                onSuccess = { _BasketScreenState.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun changeNameBasket(basket: Basket){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeNameBasket(basket) }.fold(
                onSuccess = { _BasketScreenState.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun deleteBasket(basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteBasket(basketId) }.fold(
                onSuccess = { _BasketScreenState.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun addBasket(basketName: String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.addBasket(basketName)
            }.fold(
                onSuccess = {_BasketScreenState.update { currentState ->
                    currentState.copy(baskets = it as MutableList<Basket>) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }

    fun setPositionBasket( direction: Int){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.setPositionBasket(direction)
            }.fold(
                onSuccess = {_BasketScreenState.update { currentState ->
                    currentState.copy(baskets = it) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}