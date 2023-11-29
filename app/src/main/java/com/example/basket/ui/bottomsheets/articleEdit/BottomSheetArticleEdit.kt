package com.example.basket.ui.bottomsheets.articleEdit

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
import androidx.compose.ui.res.stringResource
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Product
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TypeText
import com.example.basket.ui.bottomsheets.articleAdd.BottomSheetArticleState
import com.example.basket.ui.bottomsheets.component.ButtonConfirm
import com.example.basket.ui.bottomsheets.component.FieldName
import com.example.basket.ui.components.ChipsSections
import com.example.basket.ui.components.ChipsUnit
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.screens.article.ArticleScreenState
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.styleApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetArticleEdit(uiStateA: ArticleScreenState) {

    val uiState by remember{ mutableStateOf( BottomSheetArticleState( )) }
    uiState.onConfirmation = {
        uiStateA.changeArticle(it.article)
        uiStateA.editArticle.value = null
    }
    uiState.sections.value = uiStateA.sections
    uiState.unitApp.value = uiStateA.unitApp
    uiState.selectedProduct.value = uiStateA.editArticle.value
    uiState.enteredNameProduct.value = uiStateA.editArticle.value?.nameArticle ?: ""
    uiState.onDismissSelectArticleProduct = { uiStateA.editArticle.value = null }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)

    ModalBottomSheet(
        onDismissRequest = { uiStateA.editArticle.value = null },
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
        content = { BottomSheetArticleEditContent(uiState) })
}
@Composable fun BottomSheetArticleEditContent(uiState: BottomSheetArticleState) {
    Column( horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimen.bsPaddingVer))
    {
        TextApp(text = stringResource(R.string.change_article),
            style = styleApp(nameStyle = TypeText.NAME_SECTION))
        EditArticleField(uiState)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        SelectorSections(uiState)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        SelectorUnits(uiState)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        ButtonConfirm( onConfirm = { uiState.onConfirmation(returnEditArticle(uiState)) })
        Spacer(Modifier.height(Dimen.bsSpacerBottomHeight))
    }
}

@Composable fun EditArticleField(uiState: BottomSheetArticleState)
{
    FieldName(enterValue = uiState.enteredNameProduct)
}
@Composable fun SelectorSections(uiState: BottomSheetArticleState)
{ /** Select section*/
    ChipsSections(
        listSection = uiState.sections.value,
        sectionArticle = uiState.selectedProduct.value?.section ?: SectionDB(),
        onClick = { uiState.selectedSection.value = it})
}
@Composable fun SelectorUnits(uiState: BottomSheetArticleState)
{ /** Select unit*/
    ChipsUnit(
        listUnit = uiState.unitApp.value,
        unitArticle = uiState.selectedProduct.value?.unitApp ?: UnitDB(),
        onClick = {  uiState.selectedUnit.value = it })
}

fun returnEditArticle(uiState: BottomSheetArticleState): Product
{
    val section = if (uiState.selectedSection.value == null) {
        uiState.selectedProduct.value?.section ?: SectionDB()
    } else {
        uiState.selectedSection.value
    }

    val unitApp = if (uiState.selectedUnit.value == null) {
        uiState.selectedProduct.value?.unitApp ?: UnitDB()
    } else {
        uiState.selectedUnit.value
    }

    val article = ArticleDB(
        idArticle = uiState.selectedProduct.value?.idArticle ?: 0L,
        nameArticle = uiState.enteredNameProduct.value,
        section = section as SectionDB,
        unitApp = unitApp as UnitDB,
        position = 0,
        isSelected = false,
    )

    return ProductDB(
        value = 1.0,
        putInBasket = false,
        articleId = article.idArticle,
        article = article
    )
}