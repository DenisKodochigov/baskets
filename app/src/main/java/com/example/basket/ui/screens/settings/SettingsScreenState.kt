package com.example.basket.ui.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

data class SettingsScreenState(
    var unitApp: List<UnitApp> = emptyList(),
    var section: List<Section> = emptyList(),
    var refresh: MutableState<Boolean> = mutableStateOf(false),
    var idImage: Int = 0,
    var screenTextHeader: String = "",
    var doChangeUnit: (UnitApp) -> Unit = {},
    var doChangeSection: (Section) -> Unit = {},
    var doDeleteUnits: (List<UnitApp>) -> Unit = {},
    var doDeleteSections: (List<Section>) -> Unit = {},
)