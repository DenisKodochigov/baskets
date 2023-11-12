package com.example.basket.ui.bottomsheets.bottomSheetsOld

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Product
import com.example.basket.entity.TagsTesting
import com.example.basket.ui.components.ChipsSections
import com.example.basket.ui.components.ChipsUnit
import com.example.basket.ui.components.HeaderScreen
import com.example.basket.ui.components.MyExposedDropdownMenuBox
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.screens.products.ProductsScreenState
import com.example.basket.utils.selectSectionWithArticle
import com.example.basket.utils.selectUnitWithArticle


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetProductAddEdit(uiState: ProductsScreenState,)
{
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)
    ModalBottomSheet(
        onDismissRequest = {uiState.triggerRunOnClickFAB.value = false},
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
        content = { BottomSheetProductContent(uiState, uiState.onAddProduct) })
}

@Composable
fun BottomSheetProductContent(uiState: ProductsScreenState, onAddProduct: (Product) -> Unit)
{
    val nameSection = stringResource(R.string.name_section)
    val unitStuff = stringResource(R.string.unit_st)
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, unitStuff)) }

    enterArticle.value = Pair(
        uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L,
        enterArticle.value.second
    )

    if (enterArticle.value.first > 0) {
        enterSection.value = selectSectionWithArticle(enterArticle.value.first, uiState.articles)
        enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
    } else {
        enterUnit.value = Pair(
            uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L,
            enterUnit.value.second
        )
        enterSection.value = Pair(
            uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L,
            enterSection.value.second
        )
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_item_padding_hor)))
    {
        HeaderScreen( text = stringResource(R.string.add_product))
        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
        /** Select product*/
        MyExposedDropdownMenuBox(
            listItems = uiState.articles.map { Pair(it.idArticle, it.nameArticle) }.sortedBy { it.second },
            label = stringResource(R.string.select_product),
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true
        )
        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
        ) {
        /** Value*/
        MyOutlinedTextFieldWithoutIconClearing(
            typeKeyboard = "digit",
            title = stringResource(id = R.string.quantity),
            enterValue = enterValue,
            modifier = Modifier
                .width(dimensionResource(id = R.dimen.bottom_sheet_value_width))
//                .align(alignment = Alignment.CenterHorizontally)
        )
//        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
            Spacer(Modifier.width(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
        /** Select unit*/
        ChipsUnit(
            edit = false,
            listUnit = uiState.unitApp,
            unitArticle = uiState.articles.find { it.idArticle == enterArticle.value.first }?.unitApp,
            onClick = { enterUnit.value = Pair(it.idUnit,it.nameUnit)})
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_1)))
        /** Select section*/
        ChipsSections(
            edit = false,
            listSection = uiState.sections,
            sectionArticle = uiState.articles.find { it.idArticle == enterArticle.value.first }?.section,
            onClick = { enterSection.value = Pair(it.idSection,it.nameSection)})
        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
        TextButtonOK(
            enabled = enterArticle.value.second != "",
            onConfirm = {
                onAddProduct(
                    ProductDB(
                        value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble(),
                        putInBasket = false,
                        articleId = enterArticle.value.first,
                        article = ArticleDB(
                            idArticle = enterArticle.value.first,
                            nameArticle = enterArticle.value.second,
                            position = 0,
                            section = SectionDB(
                                enterSection.value.first,
                                enterSection.value.second
                            ),
                            unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
                            isSelected = false,
                        )
                    )
                )
                enterArticle.value = Pair(0, "")
                enterValue.value = "1"
            })
        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_1)))
    }

}