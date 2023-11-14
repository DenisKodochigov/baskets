package com.example.basket.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.basket.R
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.entity.Basket
import com.example.basket.entity.TagsTesting.DIALOG_EDIT_BASKET
import com.example.basket.entity.TagsTesting.DIALOG_EDIT_BASKET_INPUT_NAME
import com.example.basket.entity.TypeKeyboard
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK

@Composable
fun EditBasketName(
    basket: BasketDB,
    onConfirm: (Basket) -> Unit,
    onDismiss: () -> Unit,)
{
    val nameBasket = remember{ mutableStateOf(basket.nameBasket) }

    AlertDialog(
        onDismissRequest = onDismiss ,
        modifier = Modifier.testTag(DIALOG_EDIT_BASKET),
        shape = MaterialTheme.shapes.small,
        title = { MyTextH2(stringResource(R.string.change_name_basket)) },
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
        MyOutlinedTextFieldWithoutIcon( enterValue = enterValue, typeKeyboard =  TypeKeyboard.TEXT,
            modifier = Modifier.fillMaxWidth().testTag(DIALOG_EDIT_BASKET_INPUT_NAME) )
    }
}