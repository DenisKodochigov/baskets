package com.example.shopping_list.ui.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.SectionDB
import com.example.shopping_list.entity.Section
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.shopping_list.ui.components.MyTextH1
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.utils.colorPicker


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddChangeSectionDialog(
    section: Section,
    onConfirm: (Section) -> Unit,
    onDismiss: () -> Unit,
) {

    val itemLocal = remember { mutableStateOf(section) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(itemLocal.value) } ) },
        title = {
            if (section.idSection > 0) MyTextH1(stringResource(R.string.add_change_section), Modifier, TextAlign.Center)
            else MyTextH1(stringResource(R.string.add_unit), Modifier, TextAlign.Center) },
        text = { LayoutAddEditUnit(itemLocal) },
    )
}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable fun LayoutAddEditUnit(section: MutableState<Section>) {

    val colorInt = remember{ mutableStateOf(0L) }
    val itemName = remember{ mutableStateOf( section.value.nameSection )}

    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(text = "")
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = itemName, "text")
        colorInt.value = colorPicker()
        section.value = SectionDB(
            idSection = section.value.idSection,
            nameSection = itemName.value,
            colorSection = colorInt.value)
        Spacer(Modifier.height(12.dp))
    }
}
