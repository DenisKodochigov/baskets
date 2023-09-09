package com.example.basket.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.MyTextH1
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.utils.SelectColor
import com.example.basket.R

@Composable
fun EditUnitDialog(
    unitApp: UnitApp,
    onConfirm: (UnitApp) -> Unit,
    onDismiss: () -> Unit,)
{
    val unitLocal = remember{ mutableStateOf(unitApp) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButtonOK( onConfirm = {
            onConfirm( unitLocal.value )
        } ) },
        text = { EditUnitDialogLayout(unitLocal) },
        title = {
            if (unitApp.idUnit > 0) MyTextH2(stringResource(R.string.change_unit), Modifier)
            else MyTextH2(stringResource(R.string.add_unit), Modifier)
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
    val enterNameUnit = remember{ mutableStateOf( unitApp.value.nameUnit )}

    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = enterNameUnit, "text")
        unitApp.value = UnitDB(idUnit = unitApp.value.idUnit, nameUnit = enterNameUnit.value)
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun ChangeNameSectionDialog(
    section: Section,
    onConfirm: (Section) -> Unit,
    onDismiss: () -> Unit,)
{
    val unitLocal = remember{ mutableStateOf(section) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButtonOK( onConfirm = {
            onConfirm( unitLocal.value )
        } ) },
        text = { EditNameSectionLayout(unitLocal) },
        title = {
            if (section.idSection > 0) MyTextH2(stringResource(R.string.change_unit), Modifier)
            else MyTextH2(stringResource(R.string.add_unit), Modifier)
        },
    )
}

@Composable
fun EditNameSectionLayout(section: MutableState<Section>){
    val enterNameSection = remember{ mutableStateOf( section.value.nameSection )}
    Column {
        Text(text = "")
        Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            MyOutlinedTextFieldWithoutIcon(
                modifier = Modifier.fillMaxWidth(),
                enterValue = enterNameSection,
                "text"
            )
            section.value = SectionDB(
                idSection = section.value.idSection,
                nameSection = enterNameSection.value
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ChangeColorSectionDialog(
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
        text = { ChangeColorSectionLayout(itemLocal) },
    )
}

@Composable fun ChangeColorSectionLayout(section: MutableState<Section>) {

    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Text(text = section.value.nameSection)
        SelectColor(
            doSelectedColor = { selectColor ->
                section.value = SectionDB(
                    idSection = section.value.idSection,
                    nameSection = section.value.nameSection,
                    colorSection = selectColor.toLong())})
        Spacer(Modifier.height(12.dp))
    }
}