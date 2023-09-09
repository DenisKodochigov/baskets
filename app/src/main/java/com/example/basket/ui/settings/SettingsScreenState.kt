package com.example.basket.ui.settings

import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

data class SettingsScreenState (
    var unitApp: List<UnitApp> = emptyList(),
    var section: List<Section> = emptyList()
)