package com.example.shopping_list.ui.settings

import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp

data class SettingsScreenState (
    var unitApp: List<UnitApp> = emptyList(),
    var section: List<Section> = emptyList()
)