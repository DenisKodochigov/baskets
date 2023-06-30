package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shopping_list.R
import com.example.shopping_list.entity.interfaces.BasketInterface
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditBasketName(
    basket: BasketInterface,
    onConfirm: (BasketInterface) -> Unit,
    onDismiss: () -> Unit,)
{
    val nameBasket = remember{ mutableStateOf(basket.nameBasket) }

    AlertDialog(
        onDismissRequest = onDismiss ,
        title = { MyTextH2(stringResource(R.string.change_name_basket), Modifier) },
        text = { EditBasketNameDialogLayout(nameBasket) },
        confirmButton = {
            TextButtonOK( onConfirm = {
                basket.nameBasket = nameBasket.value
                onConfirm(basket) } )
        }
    )
}

@Composable
fun EditBasketNameDialogLayout( enterValue: MutableState<String>){
    Column {
        Text(text = "")
        MyOutlinedTextFieldWithoutIcon( enterValue = enterValue, typeKeyboard = "text",
            modifier = Modifier.fillMaxWidth() )
    }
}