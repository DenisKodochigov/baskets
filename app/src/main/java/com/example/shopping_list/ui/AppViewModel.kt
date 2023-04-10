package com.example.shopping_list.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.ui.baskets.StateBasketScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(): ViewModel() {

//    private val errorApp = ErrorApp(),
//    private val dataRepository = DataRepository()
    private val _stateBasketScreen = MutableStateFlow(StateBasketScreen())
    val stateBasketScreen: StateFlow<StateBasketScreen> = _stateBasketScreen.asStateFlow()
    var vmEnterText by mutableStateOf("")
        private set

    init {
        resetBasketScreen()
    }

//    private var _items = MutableStateFlow(emptyList<BasketDB>())
//    var items = _items.asStateFlow()
//    private var _newBasket = MutableStateFlow(0L)
//    var newBasket = _newBasket.asStateFlow()


    private fun resetBasketScreen(){
        getListBasket()
//        _stateBasketScreen.value = StateBasketScreen(baskets = dataRepository.getListBasket())
    }
    private fun getListBasket() {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.getListBasket()
//            }.fold(
//                onSuccess = {
//                    _stateBasketScreen.update { currentState ->
//                        currentState.copy(baskets = it) } },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
    }

    fun selectBasket(basketId:Int){

    }

    fun updateState(enterText: String){
        vmEnterText = enterText
    }

//    fun putBasket(basket: BasketDB) {
//        App.basket = basket
//    }

//    fun addNewBasket(basketName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                dataRepository.addBasket(basketName)
//            }.fold(
//                onSuccess = { _newBasket.value = it },
//                onFailure = { errorApp.errorApi(it.message!!) }
//            )
//        }
//    }
}