package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleDB
import com.example.shopping_list.data.room.tables.ProductDB
import com.example.shopping_list.data.room.tables.SectionDB
import com.example.shopping_list.data.room.tables.UnitDB
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitApp
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditQuantityDialog(
    product: Product,
    listUnit: List<UnitApp>,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit,
) {
    val enterUnit = remember {
        mutableStateOf(Pair(product.article.unitApp.idUnit, product.article.unitApp.nameUnit)) }
    val enterValue = remember { mutableStateOf(product.value.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { MyTextH2(stringResource(R.string.change_quantity), Modifier) },
        text = { EditQuantityDialogLayout(enterValue, enterUnit, listUnit) },
        confirmButton = {
            TextButtonOK(
                onConfirm = {
                    onConfirm(
                        ProductDB(
                            idProduct = product.idProduct,
                            basketId = product.basketId,
                            value = enterValue.value.toDouble(),
                            putInBasket = product.putInBasket,
                            position = product.position,
                            isSelected = product.isSelected,
                            articleId = product.article.idArticle,
                            article = ArticleDB(
                                idArticle = product.article.idArticle,
                                nameArticle = product.article.nameArticle,
                                unitApp = UnitDB( idUnit = enterUnit.value.first,nameUnit = enterUnit.value.second),
                                section = product.article.section as SectionDB,
                                isSelected = product.article.isSelected,
                                position = product.article.position,
                            )
                        )
                    )
                }
            )
        }
    )
}

@Composable
fun EditQuantityDialogLayout(
    enterValue: MutableState<String>,
    enterUnit: MutableState<Pair<Long, String>>,
    listUnit: List<UnitApp>
) {

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /** Value*/
        MyOutlinedTextFieldWithoutIconClearing(
            enterValue = enterValue,
            typeKeyboard = "digit",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(120.dp)
                .padding(top = 8.dp),
        )
        Spacer(Modifier.width(4.dp))
        /** Select unit*/
        MyExposedDropdownMenuBox(
            listItems = listUnit.map { Pair(it.idUnit, it.nameUnit) },
            label = stringResource(R.string.units),
            modifier = Modifier.width(120.dp),
            enterValue = enterUnit,
            filtering = false,
            readOnly = true
        )
    }
}


@Composable
fun SelectSectionDialog(
    listSection: List<Section>,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val enterSection = remember { mutableStateOf(Pair(listSection[0].idSection, listSection[0].nameSection)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { MyTextH2(stringResource(R.string.change_section), Modifier) },
        text = { SelectSectionDialogLayout(enterSection, listSection) },
        dismissButton = { },
        confirmButton = { TextButtonOK(onConfirm = { onConfirm(enterSection.value.first) }) }
    )
}

@Composable
fun SelectSectionDialogLayout(
    enterSection: MutableState<Pair<Long, String>>,
    listSection: List<Section>
) {

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /** Select section*/
        MyExposedDropdownMenuBox(
            listItems = listSection.map { Pair(it.idSection, it.nameSection) },
            label = stringResource(R.string.sections),
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterSection,
            filtering = false,
            readOnly = true
        )
    }
}
