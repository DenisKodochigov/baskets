package com.example.basket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basket.data.DataRepository
import com.example.basket.entity.ErrorApp
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp
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
    fun doChangeSection(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.changeSection(section) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( section = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
    fun doDeleteSections(sections: List<Section>) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching { dataRepository.deleteSections(sections) }.fold(
                onSuccess = {
                    _settingScreenState.update { currentState -> currentState.copy( section = it)}},
                onFailure = { errorApp.errorApi(it.message!!) }
            )
        }
    }
}