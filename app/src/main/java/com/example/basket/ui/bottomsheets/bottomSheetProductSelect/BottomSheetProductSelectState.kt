package com.example.basket.ui.bottomsheets.bottomSheetProductSelect

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.Product

class BottomSheetProductSelectState {
    val enteredProduct: MutableState<Article?> = mutableStateOf(null)
    var onConfirmation: (Product) -> Unit = {}
    var onDismiss:() -> Unit = {}
}