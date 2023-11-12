package com.example.basket.entity

import androidx.compose.runtime.MutableState

interface BottomSheetInterface {
    var articles: List<Article>
    var sections: List<Section>
    var unitApp: List<UnitApp>
    val selectedProduct: MutableState<Article?>
    val enteredNameProduct: MutableState<String>
    val selectedSection: MutableState<Section?>
    val enteredNameSection: MutableState<String>
    val selectedUnit: MutableState<UnitApp?>
    val enteredNameUnit: MutableState<String>
    val enteredAmount: MutableState<String>
    val buttonDialogSelectArticleProduct: MutableState<Boolean>
    val buttonDialogSelectSection: MutableState<Boolean>
    val buttonDialogSelectUnit: MutableState<Boolean>
    var onDismissSelectArticleProduct:() -> Unit
    var onDismissSelectSection:() -> Unit
    var onDismissSelectUnit:() -> Unit
    val weightButton: Float
    var onConfirmation: (Product) -> Unit
}
