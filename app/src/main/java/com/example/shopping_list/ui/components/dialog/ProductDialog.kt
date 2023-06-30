package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitApp
import com.example.shopping_list.entity.interfaces.SectionInterface
import com.example.shopping_list.entity.interfaces.UnitInterface
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
        mutableStateOf(Pair(product.article.unitA.idUnit, product.article.unitA.nameUnit)) }
    val enterValue = remember { mutableStateOf(product.value.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { MyTextH2(stringResource(R.string.change_quantity), Modifier) },
        text = { EditQuantityDialogLayout(enterValue, enterUnit, listUnit) },
        confirmButton = {

            val localProduct = Product(
                idProduct = product.idProduct,
                basketId = product.basketId,
                article = Article(
                    idArticle = product.article.idArticle,
                    nameArticle = product.article.nameArticle,
                    unitA = UnitApp( idUnit = enterUnit.value.first,nameUnit = enterUnit.value.second),
                    section = product.article.section,
                    isSelected = product.article.isSelected,
                    position = product.article.position,
                ),
                value = enterValue.value.toDouble(),
                putInBasket = product.putInBasket,
                isSelected = product.isSelected,
                position = product.position
            )
            TextButtonOK( onConfirm = { onConfirm(localProduct) })
        }
    )
}

@Composable
fun EditQuantityDialogLayout(
    enterValue: MutableState<String>,
    enterUnit: MutableState<Pair<Long, String>>,
    listUnit: List<UnitInterface>
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
    listSection: List<SectionInterface>,
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
    listSection: List<SectionInterface>
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
