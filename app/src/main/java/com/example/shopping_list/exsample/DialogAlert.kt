package com.example.shopping_list.exsample

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon

@Composable
fun EditQuantityDialog(
    product: Product,
    listUnit: List<UnitA>,
    changeProductInBasket: (Product) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier )
{
    val enterUnit = remember { mutableStateOf(
        Pair<Long,String>(product.article.unitA!!.idUnit, product.article.unitA!!.nameUnit))}
    val enterValue = remember{ mutableStateOf(product.value.toString())}

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.change_quantity)) },
            text = { DialogLayout(enterValue, enterUnit, listUnit) },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(R.string.exit)) } },
            confirmButton = {
                TextButton(onClick = {
                    product.value = enterValue.value.toDouble()
                    product.article.unitA!!.idUnit = enterUnit.value.first
                    product.article.unitA!!.nameUnit = enterUnit.value.second
                    changeProductInBasket(product)
                }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
fun DialogLayout(
    enterValue: MutableState<String>,
    enterUnit: MutableState<Pair<Long,String>>,
    listUnit: List<UnitA>){

    Row(Modifier.fillMaxWidth()) {
        MyOutlinedTextFieldWithoutIcon(/** Value*/
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(100.dp)
                .padding(top = 8.dp),
            enterValue = enterValue)
        Spacer(Modifier.width(4.dp))
        MyExposedDropdownMenuBox(/** Select unit*/
            listItems = listUnit.map{ Pair(it.idUnit, it.nameUnit) },
            label = "Unit",
            modifier = Modifier.width(120.dp),
            enterValue = enterUnit,
            filtering = false)
    }
}
