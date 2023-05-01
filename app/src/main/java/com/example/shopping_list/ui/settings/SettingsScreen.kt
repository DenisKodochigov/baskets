package com.example.shopping_list.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.shopping_list.ui.AppViewModel

@Composable
fun SettingsScreen(
    onSettingsClick: (String) -> Unit = {},
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    bottomSheetContent.value =  null
}