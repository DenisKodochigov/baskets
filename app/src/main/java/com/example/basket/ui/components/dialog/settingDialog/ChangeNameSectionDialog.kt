package com.example.basket.ui.components.dialog.settingDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.entity.Section
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.TypeText
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.styleApp


@Composable
fun ChangeNameSectionDialog(
    section: Section,
    onConfirm: (Section) -> Unit,
    onDismiss: () -> Unit,)
{
    val unitLocal = remember{ mutableStateOf(section) }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = shapesApp.small,
        confirmButton = { TextButtonOK( onConfirm = { onConfirm( unitLocal.value ) } ) },
        text = { EditNameSectionLayout(unitLocal) },
        title = {
            if (section.idSection > 0) MyTextH2(stringResource(R.string.edit_section))
            else MyTextH2(stringResource(R.string.add_section))
        },
    )
}

@Composable
fun EditNameSectionLayout(section: MutableState<Section>){
    val enterNameSection = remember{ mutableStateOf( section.value.nameSection )}
    Column {
        TextApp(text = "", style = styleApp(nameStyle = TypeText.EDIT_TEXT))
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)) {
//            TextFieldApp(modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Start,
//                enterValue = enterNameSection,
//                typeKeyboard = TypeKeyboard.TEXT)


            MyOutlinedTextFieldWithoutIcon(
                modifier = Modifier.fillMaxWidth(),
                enterValue = enterNameSection,
                typeKeyboard =  TypeKeyboard.TEXT)
            section.value = SectionDB(
                idSection = section.value.idSection,
                nameSection = enterNameSection.value
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

