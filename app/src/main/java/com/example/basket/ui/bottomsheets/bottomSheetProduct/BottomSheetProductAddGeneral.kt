package com.example.basket.ui.bottomsheets.bottomSheetProduct

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.TagsTesting
import com.example.basket.ui.bottomsheets.bottomSheetProductSelect.BottomSheetProductSelect
import com.example.basket.ui.bottomsheets.bottomSheetSectionSelect.BottomSheetSectionSelect
import com.example.basket.ui.bottomsheets.bottomSheetUnitSelect.BottomSheetUnitSelect
import com.example.basket.ui.bottomsheets.component.ButtonConfirm
import com.example.basket.ui.bottomsheets.component.RowSelectedProduct
import com.example.basket.ui.bottomsheets.component.RowSelectedSection
import com.example.basket.ui.bottomsheets.component.RowSelectedUnit
import com.example.basket.ui.screens.products.ProductsScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetProductAddGeneral (uiStateP: ProductsScreenState)
{
    val uiState by remember{ mutableStateOf(BottomSheetProductState()) }
    uiState.onConfirmation = {
        uiStateP.onAddProduct(it)
        uiStateP.triggerRunOnClickFAB.value = false
    }
    uiState.articles = uiStateP.articles
    uiState.sections = uiStateP.sections
    uiState.unitApp = uiStateP.unitApp
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
            .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_padding_hor)),
        shape = MaterialTheme.shapes.small,
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
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
        GroupButtons(uiState)
        ButtonConfirm(uiState)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
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

@Preview
@Composable fun BottomSheetProductSelectGeneralPreview(){
    BottomSheetProductAddGeneralLayOut(BottomSheetProductState())
}