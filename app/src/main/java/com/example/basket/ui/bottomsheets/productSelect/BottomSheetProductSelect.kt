package com.example.basket.ui.bottomsheets.productSelect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TypeText
import com.example.basket.entity.UPDOWN
import com.example.basket.ui.bottomsheets.productAdd.BottomSheetProductState
import com.example.basket.ui.bottomsheets.component.ButtonConfirm
import com.example.basket.ui.bottomsheets.component.FieldName
import com.example.basket.ui.components.ShowArrowVer
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.styleApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetProductSelect(uiState: BottomSheetInterface)
{
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)
    ModalBottomSheet(
        onDismissRequest = uiState.onDismissSelectArticleProduct,
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = Dimen.bsPaddingHor1),
        shape = shapesApp.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetProductSelectContent(uiState) })
}
@Composable fun BottomSheetProductSelectContent(uiState: BottomSheetInterface)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Dimen.bsPaddingHor, vertical = Dimen.bsItemPaddingVer),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        FieldName(uiState.enteredNameProduct)
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
        BoxExistingArticles(uiState)
        Spacer(modifier = Modifier.height(Dimen.bsConfirmationButtonTopHeight))
        ButtonConfirm {
            clearSelectedProduct(uiState)
            uiState.onConfirmationSelectArticleProduct(uiState)
        }
        Spacer(modifier = Modifier.height(Dimen.bsSpacerBottomHeight))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable fun BoxExistingArticles(uiState: BottomSheetInterface)
{
    val listItems = uiState.articles.value
        .filter { it.nameArticle.contains(uiState.enteredNameProduct.value, ignoreCase = true) }
        .sortedBy { it.nameArticle }

    val listState = rememberLazyGridState()
    val showArrowUp = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 } }.value
    val showArrowDown = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
                listState.layoutInfo.totalItemsCount - 1 } }.value
    Column {
        ShowArrowVer(direction = UPDOWN.UP, enable = showArrowUp && listItems.isNotEmpty(), drawLine = true)
        LazyVerticalGrid(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            columns = GridCells.Adaptive(minSize = 90.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .heightIn(min = Dimen.bsHeightMintList, max = Dimen.bsHeightMaxList)
                .fillMaxWidth()
        ) {
            items(listItems) { article ->
                TextApp(
                    text = article.nameArticle,
                    style = styleApp(nameStyle = TypeText.EDIT_TEXT),
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable {
                            uiState.enteredNameProduct.value = article.nameArticle
                            uiState.selectedProduct.value = article
                            uiState.selectedSection.value = article.section
                            uiState.selectedUnit.value = article.unitApp
                        }
                        .padding(4.dp)
                        .background(shape = shapesApp.medium, color = colorApp.surface)
                )
            }
        }
        ShowArrowVer(direction = UPDOWN.DOWN, enable = showArrowDown && listItems.isNotEmpty(), drawLine = true)
    }
}

fun clearSelectedProduct(uiState: BottomSheetInterface){
    if (uiState.selectedProduct.value != null){
        if (uiState.selectedProduct.value!!.nameArticle != uiState.enteredNameProduct.value){
            uiState.selectedProduct.value = null
        }
    }
}

@Preview
@Composable fun BottomSheetProductSelectLayoutPreview(){
    BottomSheetProductSelectContent(BottomSheetProductState())
}