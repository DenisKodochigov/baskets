package com.example.basket.ui.components.dialog.settingDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.entity.Section
import com.example.basket.ui.components.MyTextH1
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.shapesApp
import com.example.basket.utils.SelectColor

@Composable
fun ChangeColorSectionDialog(
    section: Section,
    onConfirm: (Section) -> Unit,
    onDismiss: () -> Unit,
){
    val itemLocal = remember { mutableStateOf(section) }
    AlertDialog(
        modifier = Modifier.border(width = 1.dp, shape = shapesApp.small, color = colorApp.primary),
        onDismissRequest = onDismiss,
        shape = shapesApp.small,
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(itemLocal.value) } ) },
        title = { MyTextH1(stringResource(R.string.change_color), Modifier, TextAlign.Center) },
        text = { ChangeColorSectionLayout(itemLocal) },
    )
}

@Composable
fun ChangeColorSectionLayout(section: MutableState<Section>) {
    val selectColor = remember{ mutableStateOf(Color.Transparent) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row( verticalAlignment = Alignment.CenterVertically) {
            MyTextH2(text = stringResource(id = R.string.section) + ": " + section.value.nameSection)
            Spacer(Modifier.width(12.dp))
            Spacer(modifier = Modifier
                .size(size = 35.dp)
                .clip(shape = CircleShape)
                .background(color = selectColor.value, shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = colorApp.outline,
                    shape = CircleShape
                )
            )
        }

        SelectColor(
            doSelectedColor = { selectedColor ->
                selectColor.value = Color(selectedColor)
                section.value = SectionDB(
                    idSection = section.value.idSection,
                    nameSection = section.value.nameSection,
                    colorSection = selectedColor.toLong())
           }
        )
    }
}