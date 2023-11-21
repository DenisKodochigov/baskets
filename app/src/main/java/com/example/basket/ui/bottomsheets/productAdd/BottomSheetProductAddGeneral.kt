package com.example.basket.ui.bottomsheets.productAdd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.domain.logicBottomSheet.choiceArticle
import com.example.basket.domain.logicBottomSheet.choiceSection
import com.example.basket.domain.logicBottomSheet.choiceUnit
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.Product
import com.example.basket.entity.TagsTesting
import com.example.basket.ui.bottomsheets.component.ButtonConfirm
import com.example.basket.ui.bottomsheets.component.RowSelectedProduct
import com.example.basket.ui.bottomsheets.component.RowSelectedSection
import com.example.basket.ui.bottomsheets.component.RowSelectedUnit
import com.example.basket.ui.bottomsheets.productSelect.BottomSheetProductSelect
import com.example.basket.ui.bottomsheets.sectionSelect.BottomSheetSectionSelect
import com.example.basket.ui.bottomsheets.unitSelect.BottomSheetUnitSelect
import com.example.basket.ui.screens.products.ProductsScreenState
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.shapesApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetProductAddGeneral (uiStateP: ProductsScreenState)
{
    val uiState by remember{ mutableStateOf(BottomSheetProductState()) }
    uiState.onConfirmation = {
        uiStateP.onAddProduct(it)
//        uiStateP.triggerRunOnClickFAB.value = false
    }
    uiState.articles.value = uiStateP.articles.value
    uiState.sections.value = uiStateP.sections.value
    uiState.unitApp.value = uiStateP.unitApp.value
    uiState.onDismissSelectArticleProduct = { uiState.buttonDialogSelectArticleProduct.value = false }
    uiState.onDismissSelectSection = { uiState.buttonDialogSelectSection.value = false }
    uiState.onDismissSelectUnit = { uiState.buttonDialogSelectUnit.value = false }
    uiState.onConfirmationSelectArticleProduct = { uiState.buttonDialogSelectArticleProduct.value = false }
    uiState.onConfirmationSelectSection = { uiState.buttonDialogSelectSection.value = false }
    uiState.oConfirmationSelectUnit = { uiState.buttonDialogSelectUnit.value = false }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)

    ModalBottomSheet(
        onDismissRequest = {uiStateP.triggerRunOnClickFAB.value = false},
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = Dimen.bsPaddingHor),
        shape = shapesApp.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetProductAddGeneralLayOut(uiState) })

    if (uiState.buttonDialogSelectArticleProduct.value) BottomSheetProductSelect(uiState)
    if (uiState.buttonDialogSelectSection.value) BottomSheetSectionSelect(uiState)
    if (uiState.buttonDialogSelectUnit.value) BottomSheetUnitSelect(uiState)
}
@Composable
fun BottomSheetProductAddGeneralLayOut (uiState: BottomSheetProductState)
{
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
        GroupButtons(uiState)
        ButtonConfirm( onConfirm = { uiState.onConfirmation(returnSelectedProduct(uiState)) })
        Spacer(modifier = Modifier.height(Dimen.bsSpacerHeight1))
    }
}
@Composable fun GroupButtons(uiState: BottomSheetInterface)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        RowSelectedProduct(uiState)
        RowSelectedSection(uiState)
        RowSelectedUnit(uiState)
    }
}
fun returnSelectedProduct(uiState: BottomSheetInterface): Product
{

    val unitA = choiceUnit(unt = uiState.selectedUnit.value,
        enterNameUnit = uiState.enteredNameUnit.value,
        unit0 = uiState.unitApp.value[0])

    val section = choiceSection(section = uiState.selectedSection.value,
        enterNameSection = uiState.enteredNameSection.value,
        section0 = uiState.sections.value[0] )

    val article = choiceArticle(article = uiState.selectedProduct.value,
        enterNAmeArticle = uiState.enteredNameProduct.value,
        section = section,
        unt = unitA)

    uiState.selectedProduct.value = null
    if (uiState.selectedSection.value != null){
        if (uiState.selectedSection.value!!.idSection == 0L) uiState.selectedSection.value = null }
    if (uiState.selectedUnit.value != null){
        if (uiState.selectedUnit.value!!.idUnit == 0L) uiState.selectedUnit.value = null }
    uiState.enteredNameProduct.value = ""
    uiState.enteredNameSection.value = ""
    uiState.enteredNameUnit.value = ""
    return ProductDB(
        value = if (uiState.enteredAmount.value.isEmpty()) 1.0
        else uiState.enteredAmount.value.toDouble(),
        putInBasket = false,
        articleId = article.idArticle,
        article = article
    )
}
@Preview
@Composable fun BottomSheetProductSelectGeneralPreview(){
    BottomSheetProductAddGeneralLayOut(BottomSheetProductState())
}