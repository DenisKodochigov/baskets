package com.example.shopping_list.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.data.DataRepository
import com.example.shopping_list.entity.ErrorApp
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp
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
        getListSection()
    }
    private fun getListUnit() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getUnits() }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitApp = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    private fun getListSection() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.getSections() }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( section = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun changeUnit(unit: UnitApp) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeUnit(unit) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitApp = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }

    fun doDeleteUnits(units: List<UnitApp>) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteUnits(units) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( unitApp = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun changeSectionColor(sectionId: Long, colorLong: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeSectionColor(sectionId, colorLong) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( section = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
}