package com.example.basket.ui.bottomsheets.bottomSheetProductAdd

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

class BottomSheetProductAddState {
    val enteredProduct: MutableState<Article?> = mutableStateOf(null)
    val selectedSection: MutableState<Section?> = mutableStateOf(null)
    val selectedUnit: MutableState<UnitApp?> = mutableStateOf(null)
    val enteredAmount: MutableState<String> = mutableStateOf("0")
    val buttonDialogSelectProduct: MutableState<Boolean> = mutableStateOf(false)
    val buttonDialogSelectSection: MutableState<Boolean> = mutableStateOf(false)
    val buttonDialogSelectUnit: MutableState<Boolean> = mutableStateOf(false)
    val buttonConfirm: MutableState<Boolean?> = mutableStateOf(null)
    var onConfirmation: (Product) -> Unit = {}
    var onDismiss:() -> Unit = {}
    val weightButton: Float = .4f
}