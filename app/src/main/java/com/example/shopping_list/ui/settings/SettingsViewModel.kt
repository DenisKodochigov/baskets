package com.example.shopping_list.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.interfaces.UnitInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val errorApp: ErrorApp,
    private val dataRepository: DataRepository
): ViewModel() {
    private val _settingScreenState = MutableStateFlow(SettingsScreenState())
    val settingScreenState: StateFlow<SettingsScreenState> = _settingScreenState.asStateFlow()

    init {
        getListUnit()
    }
    private fun getListUnit() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitA = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun changeUnit(unit: UnitInterface) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeUnit(unit) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitA = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun doDeleteUnits(units: List<UnitInterface>) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteUnits(units) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitA = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
}