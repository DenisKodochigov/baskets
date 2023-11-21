package com.example.basket.entity

import androidx.compose.runtime.MutableState

interface BottomSheetInterface {

    val articles: MutableState<List<Article>>
    val sections: MutableState<List<Section>>
    val unitApp: MutableState<List<UnitApp>>
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
    var onConfirmationSelectArticleProduct:(BottomSheetInterface) -> Unit
    var onConfirmationSelectSection:(BottomSheetInterface) -> Unit
    var oConfirmationSelectUnit:(BottomSheetInterface) -> Unit
    val weightButton: Float
    var onConfirmation: (Product) -> Unit
}
