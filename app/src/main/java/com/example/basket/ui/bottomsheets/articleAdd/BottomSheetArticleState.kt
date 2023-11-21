package com.example.basket.ui.bottomsheets.articleAdd

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

class BottomSheetArticleState(

    override val articles: MutableState<List<Article>> = mutableStateOf(emptyList()),
    override val sections: MutableState<List<Section>> = mutableStateOf(emptyList()),
    override val unitApp: MutableState<List<UnitApp>> = mutableStateOf(emptyList()),
    override val selectedProduct: MutableState<Article?> = mutableStateOf(null),
    override val enteredNameProduct: MutableState<String> = mutableStateOf(""),
    override var selectedSection: MutableState<Section?> = mutableStateOf(null),
    override var enteredNameSection: MutableState<String> = mutableStateOf(""),
    override val selectedUnit: MutableState<UnitApp?> = mutableStateOf(null),
    override val enteredNameUnit: MutableState<String> = mutableStateOf(""),
    override val enteredAmount: MutableState<String> = mutableStateOf("1"),
    override val buttonDialogSelectArticleProduct: MutableState<Boolean> = mutableStateOf(false),
    override var buttonDialogSelectSection: MutableState<Boolean> = mutableStateOf(false),
    override val buttonDialogSelectUnit: MutableState<Boolean> = mutableStateOf(false),
    override var onDismissSelectArticleProduct: () -> Unit = {},
    override var onDismissSelectSection: () -> Unit = {  },
    override var onDismissSelectUnit: () -> Unit = {},
    override var onConfirmationSelectArticleProduct: (BottomSheetInterface) -> Unit = {},
    override var onConfirmationSelectSection: (BottomSheetInterface) -> Unit = {},
    override var oConfirmationSelectUnit: (BottomSheetInterface) -> Unit = {},
    override val weightButton: Float = .4f,
    override var onConfirmation: (Product) -> Unit = {},
    var onDismiss: () -> Unit = {},
) : BottomSheetInterface {

//    constructor(
//        onDismissSelectSection: () -> Unit,
//        buttonDialogSelectSection: MutableState<Boolean>,
//        selectedSection: MutableState<Section?>,
//        enteredNameSection: MutableState<String>,
//        sections: List<Section>
//    ) : this() {
//        this.onDismissSelectSection = onDismissSelectSection
//        this.buttonDialogSelectSection = buttonDialogSelectSection
//        this.sections = sections
//        this.selectedSection = selectedSection
//        this.enteredNameSection = enteredNameSection
//    }




}