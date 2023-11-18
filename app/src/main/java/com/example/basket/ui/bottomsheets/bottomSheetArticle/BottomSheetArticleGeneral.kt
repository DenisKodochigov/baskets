package com.example.basket.ui.bottomsheets.bottomSheetArticle

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
import com.example.basket.ui.screens.article.ArticleScreenState
import com.example.basket.ui.theme.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetArticleGeneral (uiStateA: ArticleScreenState)
{
    val uiState by remember{ mutableStateOf( BottomSheetArticleState( )) }
    uiState.onConfirmation = {
        uiStateA.onAddArticle(it.article)
        uiStateA.triggerRunOnClickFAB.value = false
    }
//    uiState.articles = createLisArticleFormDouble(uiStateA.articles)
    uiState.articles = uiStateA.articles.flatten()
    uiState.sections = uiStateA.sections
    uiState.unitApp = uiStateA.unitApp
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
        onDismissRequest = {uiStateA.triggerRunOnClickFAB.value = false},
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = Dimen.bsPaddingHor),
        shape = MaterialTheme.shapes.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetArticleLayOut(uiState) })

    if (uiState.buttonDialogSelectArticleProduct.value) BottomSheetProductSelect(uiState as BottomSheetInterface)
    if (uiState.buttonDialogSelectSection.value) BottomSheetSectionSelect(uiState as BottomSheetInterface)
    if (uiState.buttonDialogSelectUnit.value) BottomSheetUnitSelect(uiState as BottomSheetInterface)
}
@Composable
fun BottomSheetArticleLayOut (uiState: BottomSheetArticleState)
{
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
        GroupButtons(uiState)
        ButtonConfirm(uiState)
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
    }
}
@Composable fun GroupButtons(uiState: BottomSheetArticleState)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        RowSelectedProduct(uiState)
        RowSelectedSection(uiState)
        RowSelectedUnit( uiState, false)
    }
}