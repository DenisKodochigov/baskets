package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditQuantityDialog(
    product: Product,
    listUnit: List<UnitA>,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit,)
{
    val enterUnit = remember { mutableStateOf(
        Pair(product.article.unitA.idUnit, product.article.unitA.nameUnit)) }
    val enterValue = remember{ mutableStateOf(product.value.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss ,
        title = { MyTextH2(stringResource(R.string.change_quantity), Modifier) },
        text = { EditQuantityDialogLayout(enterValue, enterUnit, listUnit) },
        confirmButton = {
            product.value = enterValue.value.toDouble()
            product.article.unitA.idUnit = enterUnit.value.first
            product.article.unitA.nameUnit = enterUnit.value.second
            TextButtonOK(onConfirm = { onConfirm(product) })
        }
    )
}

@Composable
fun EditQuantityDialogLayout(
    enterValue: MutableState<String>,
    enterUnit: MutableState<Pair<Long, String>>,
    listUnit: List<UnitA>){

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /** Value*/
        MyOutlinedTextFieldWithoutIconClearing( enterValue = enterValue, typeKeyboard = "digit",
            modifier = Modifier.align(Alignment.CenterVertically).width(120.dp).padding(top = 8.dp),)
        Spacer(Modifier.width(4.dp))
        /** Select unit*/
        MyExposedDropdownMenuBox(
            listItems = listUnit.map{ Pair(it.idUnit, it.nameUnit) },
            label = "Unit",
            modifier = Modifier.width(120.dp),
            enterValue = enterUnit,
            filtering = false,
            readOnly = true
        )
    }
}


@Composable
fun SelectGroupDialog(
    listGroup: List<GroupArticle>,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit)
{
    val enterGroup = remember { mutableStateOf(Pair(listGroup[0].idGroup, listGroup[0].nameGroup)) }

    AlertDialog(
        onDismissRequest = onDismiss ,
        title = { MyTextH2(stringResource(R.string.change_group), Modifier)},
        text = { SelectGroupDialogLayout(enterGroup, listGroup) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                MyTextH2(stringResource(R.string.exit), Modifier) }
        },
        confirmButton = {
            TextButton( onClick = { onConfirm(enterGroup.value.first) })
            { MyTextH2(stringResource(R.string.ok), Modifier) }
        }
    )
}
@Composable
fun SelectGroupDialogLayout(
    enterGroup: MutableState<Pair<Long, String>>,
    listGroup: List<GroupArticle>){

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /** Select group*/
        MyExposedDropdownMenuBox(
            listItems = listGroup.map{ Pair(it.idGroup, it.nameGroup) },
            label = "Groups",
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterGroup,
            filtering = false,
            readOnly = true
        )
    }
}
