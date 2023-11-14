package com.example.basket.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.basket.R
import com.example.basket.entity.Section
import com.example.basket.ui.components.MyExposedDropdownMenuBox
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK

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
        title = { MyTextH2(stringResource(R.string.edit_section)) },
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