package com.example.basket.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.Product
import com.example.basket.entity.UnitApp
import com.example.basket.ui.components.MyExposedDropdownMenuBox
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.R

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
        shape = MaterialTheme.shapes.small,
        title = { MyTextH2(stringResource(R.string.change_quantity)) },
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
        shape = MaterialTheme.shapes.small,
        title = { MyTextH2(stringResource(R.string.change_section)) },
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
