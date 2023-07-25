package com.example.shopping_list.ui.components.dialog

import androidx.compose.material.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shopping_list.R
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.utils.ColorPicker

@Composable
fun ChoiceColorDialog(
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit,
) {

    val colorInt = remember{ mutableStateOf(0L) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(colorInt.value) } ) },
        title = {  MyTextH2(stringResource(R.string.choise_color), Modifier) },
        text = { colorInt.value = ColorPicker() },
    )
}
