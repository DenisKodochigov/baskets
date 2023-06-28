package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditUnitDialog(
    unitA: UnitA,
    onConfirm: (UnitA) -> Unit,
    onDismiss: () -> Unit,)
{
    val unitLocal = remember{ mutableStateOf(unitA) }
    AlertDialog(
        onDismissRequest = onDismiss ,
        title = {
            if (unitA.idUnit > 0) MyTextH2(stringResource(R.string.change_unit), Modifier)
            else MyTextH2(stringResource(R.string.add_unit), Modifier)
        },
        text = { EditUnitDialogLayout(unitLocal) },
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(unitLocal.value) } ) }
    )
}

@Composable
fun EditUnitDialogLayout( unitA: MutableState<UnitA>){
    Column {
        Text(text = "")
        LayoutAddEditUnit( unitA = unitA)
    }
}

@Composable
fun LayoutAddEditUnit( unitA: MutableState<UnitA>)
{
    val enterNameUnit = remember{ mutableStateOf( unitA.value.nameUnit )}

    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = enterNameUnit, "text")
//        unitA.value.nameUnit = enterNameUnit.value
        unitA.value = UnitEntity(nameUnit = enterNameUnit.value)
        Spacer(Modifier.height(12.dp))
    }
}
