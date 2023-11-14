package com.example.basket.ui.bottomsheets.bottomSheetProduct

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

class BottomSheetProductState(
    override var articles: List<Article> = emptyList(),
    override var sections: List<Section> = emptyList(),
    override var unitApp: List<UnitApp> = emptyList(),
    override val selectedProduct: MutableState<Article?> = mutableStateOf(null),
    override val enteredNameProduct: MutableState<String> = mutableStateOf(""),
    override val selectedSection: MutableState<Section?> = mutableStateOf(null),
    override val enteredNameSection: MutableState<String> = mutableStateOf(""),
    override val selectedUnit: MutableState<UnitApp?> = mutableStateOf(null),
    override val enteredNameUnit: MutableState<String> = mutableStateOf(""),
    override val enteredAmount: MutableState<String> = mutableStateOf("1"),
    override val buttonDialogSelectArticleProduct: MutableState<Boolean> = mutableStateOf(false),
    override val buttonDialogSelectSection: MutableState<Boolean> = mutableStateOf(false),
    override val buttonDialogSelectUnit: MutableState<Boolean> = mutableStateOf(false),
    override var onDismissSelectArticleProduct:() -> Unit = {},
    override var onDismissSelectSection:() -> Unit = {},
    override var onDismissSelectUnit:() -> Unit = {},
    override var onConfirmationSelectArticleProduct: (BottomSheetInterface) -> Unit = {},
    override var onConfirmationSelectSection: (BottomSheetInterface) -> Unit = {},
    override var oConfirmationSelectUnit: (BottomSheetInterface) -> Unit = { },
    override val weightButton: Float = .4f,
    override var onConfirmation: (Product) -> Unit = {},
    var onDismiss:() -> Unit = {},
) : BottomSheetInterface {
}