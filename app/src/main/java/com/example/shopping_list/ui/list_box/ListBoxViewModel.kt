package com.example.shopping_list.ui.list_box

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.Box
import com.example.shopping_list.entity.ErrorApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListBoxViewModel @Inject constructor(): ViewModel() {
    private val dataRepository = DataRepository()
    private var _items = MutableStateFlow(emptyList<Box>())
    var items = _items.asStateFlow()

    fun getListBox() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                dataRepository.getBoxes()
            }.fold(
                onSuccess = {_items.value = it },
                onFailure = { ErrorApp().errorApi(it.message!!)}
            )
        }
    }
    fun putBox(box: Box){

    }
}