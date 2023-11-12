package com.example.basket.ui.screens.baskets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basket.data.DataRepository
import com.example.basket.entity.Basket
import com.example.basket.entity.ErrorApp
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

    private val _basketScreenState = MutableStateFlow(BasketScreenState())
    val basketScreenState: StateFlow<BasketScreenState> = _basketScreenState.asStateFlow()

    fun getListBasket() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getListBasket() }.fold(
                onSuccess = { _basketScreenState.update { currentState ->
                    currentState.copy(baskets = it ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun changeNameBasket(basket: Basket){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeNameBasket(basket) }.fold(
                onSuccess = { _basketScreenState.update { currentState ->
                    currentState.copy(baskets = it ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun deleteBasket(basketId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteBasket(basketId) }.fold(
                onSuccess = { _basketScreenState.update { currentState ->
                    currentState.copy( baskets = it ) } },
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun addBasket(basketName: String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.addBasket(basketName) }.fold(
                onSuccess = {_basketScreenState.update { currentState ->
                    currentState.copy( baskets = it ) }},
                onFailure = { errorApp.errorApi(it.message!!)}
            )
        }
    }
}