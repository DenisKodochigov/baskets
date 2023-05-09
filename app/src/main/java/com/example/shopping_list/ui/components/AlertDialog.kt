package com.example.shopping_list.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
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
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA

@Composable
fun EditQuantityDialog(
    product: Product,
    listUnit: List<UnitA>,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean,
    modifier: Modifier = Modifier )
{
    val enterUnit = remember { mutableStateOf(
        Pair<Long,String>(product.article.unitA!!.idUnit, product.article.unitA!!.nameUnit)) }
    val enterValue = remember{ mutableStateOf(product.value.toString()) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss ,
            title = { Text(stringResource(R.string.change_quantity)) },
            text = { DialogLayout(enterValue, enterUnit, listUnit) },
            modifier = modifier,
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.exit))
                }
            },
            confirmButton = {
                TextButton( onClick = {
                    product.value = enterValue.value.toDouble()
                    product.article.unitA!!.idUnit = enterUnit.value.first
                    product.article.unitA!!.nameUnit = enterUnit.value.second
                    onConfirm(product)
                }) {
                    Text( text = stringResource(R.string.ok))
                }
            }
        )
    }
}


@Composable
fun DialogLayout(
    enterValue: MutableState<String>,
    enterUnit: MutableState<Pair<Long, String>>,
    listUnit: List<UnitA>){

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        /** Value*/
        MyOutlinedTextFieldWithoutIcon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(120.dp)
                .padding(top = 8.dp),
            enterValue = enterValue)
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
fun EditBasketName(
    basket: Basket,
    onConfirm: (Basket) -> Unit,
    onDismiss: () -> Unit,)
{
    val nameBasket = remember{ mutableStateOf(basket.nameBasket) }

    AlertDialog(
        onDismissRequest = onDismiss ,
        title = { Text(stringResource(R.string.change_quantity)) },
        text = { EditBasketNameDialogLayout(nameBasket) },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton( onClick = {
                basket.nameBasket = nameBasket.value
                onConfirm(basket)
            }) {
                Text( text = stringResource(R.string.ok))
            }
        }
    )
}


@Composable
fun EditBasketNameDialogLayout( enterValue: MutableState<String>){

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        MyOutlinedTextFieldWithoutIcon( enterValue = enterValue,
            modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth() )
    }
}
