package com.example.basket.ui.components.dialog.settingDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.UnitApp
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK

@Composable
fun EditUnitDialog(
    unitApp: UnitApp,
    onConfirm: (UnitApp) -> Unit,
    onDismiss: () -> Unit,)
{
    val unitLocal = remember{ mutableStateOf(unitApp) }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.small,
        confirmButton = { TextButtonOK( onConfirm = {
            onConfirm( unitLocal.value )
        } ) },
        text = { EditUnitDialogLayout(unitLocal) },
        title = {
            if (unitApp.idUnit > 0) MyTextH2(stringResource(R.string.change_unit))
            else MyTextH2(stringResource(R.string.add_unit))
        },
    )
}

@Composable
fun EditUnitDialogLayout(unitApp: MutableState<UnitApp>){
    Column {
        Text(text = "")
        LayoutAddEditUnit( unitApp = unitApp)
    }
}

@Composable
fun LayoutAddEditUnit(unitApp: MutableState<UnitApp>)
{
    val enterNameUnit = remember{ mutableStateOf( unitApp.value.nameUnit ) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(
            modifier = Modifier.fillMaxWidth(), enterValue = enterNameUnit,  TypeKeyboard.TEXT)
        unitApp.value = UnitDB(idUnit = unitApp.value.idUnit, nameUnit = enterNameUnit.value)
        Spacer(Modifier.height(12.dp))
    }
}