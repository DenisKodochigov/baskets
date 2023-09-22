package com.example.basket.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basket.AppBase
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.TypeText
import com.example.basket.entity.UnitApp
import com.example.basket.ui.components.ButtonCircle
import com.example.basket.ui.components.HeaderScreen
import com.example.basket.ui.components.HeaderSection
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.dialog.ChangeColorSectionDialog
import com.example.basket.ui.components.dialog.ChangeNameSectionDialog
import com.example.basket.ui.components.dialog.EditUnitDialog
import com.example.basket.ui.theme.styleApp

@Composable fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    SettingsScreenInit(viewModel)
}

@Composable fun SettingsScreenInit(viewModel: SettingsViewModel){
    val uiState by viewModel.settingScreenState.collectAsState()

    SettingsScreenLayout(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        doChangeUnit = { unit -> viewModel.changeUnit(unit) },
        doDeleteUnits = { units -> viewModel.doDeleteUnits(units) },
        doChangeSection = { section -> viewModel.doChangeSection(section) },
        doDeleteSelected = { sections -> viewModel.doDeleteSections(sections) },
    )
}
@SuppressLint("UnrememberedMutableState")
@Composable fun SettingsScreenLayout(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenState,
    doChangeUnit: (UnitApp) -> Unit,
    doDeleteUnits: (List<UnitApp>) -> Unit,
    doChangeSection: (Section) -> Unit,
    doDeleteSelected: (List<Section>) -> Unit,
){
    Column (
        Modifier.verticalScroll(
            state = rememberScrollState(),
            enabled = true,
            flingBehavior = null,
            reverseScrolling = false)
        ){
        HeaderScreen(text = stringResource(R.string.settings_page))
        AddEditUnits(modifier, uiState, doChangeUnit, doDeleteUnits)
        AddEditSection(modifier, uiState, doChangeSection, doDeleteSelected)
        ChangeStyle()
//        FontStyleView()
    }
}

@Composable fun AddEditSection(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenState,
    doChangeSection: (Section) -> Unit,
    doDeleteSelected: (List<Section>) -> Unit,){

    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val itemList = uiState.section
    if (isSelectedId.value > 0L) {
        itemList.find { it.idSection == isSelectedId.value }?.let { it.isSelected = !it.isSelected }
        isSelectedId.value = 0
    }

    Box(modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))) {
        Column{
            HeaderSection(text = stringResource(R.string.edit_section_list), modifier)
            SectionLazyColumn( uiState = uiState,
                doChangeSection = doChangeSection,
                doDeleteSelected = doDeleteSelected,
                doSelected = { idItem -> isSelectedId.value = idItem })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
@Composable
fun SectionLazyColumn(
    uiState: SettingsScreenState,
    doChangeSection: (Section) -> Unit,
    doDeleteSelected: (List<Section>) -> Unit,
    doSelected: (Long) -> Unit
) {
    val editNameSection: MutableState<Section?> = remember { mutableStateOf(null) }
    val changeColorSection: MutableState<Section?> = remember { mutableStateOf(null) }

    if (editNameSection.value != null) {
        ChangeNameSectionDialog(
            section = editNameSection.value!!,
            onDismiss = { editNameSection.value = null},
            onConfirm = {
                doChangeSection(it)
                editNameSection.value = null },)
    }
    if (changeColorSection.value != null) {
        ChangeColorSectionDialog(
            section = changeColorSection.value!!,
            onDismiss = { changeColorSection.value = null},
            onConfirm = {
                doChangeSection(it)
                changeColorSection.value = null },)
    }
    Column() {
        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(min = 0.dp, max = 300.dp)
        ) {
            items(items = uiState.section) {item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .clickable { doSelected(item.idSection) }
                ) {
                    TextApp(
                        text = item.nameSection,
                        style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),//   .h1,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .clickable { editNameSection.value = item }
                            .padding(
                                horizontal = dimensionResource(R.dimen.lazy_padding_hor2),
                                vertical = dimensionResource(R.dimen.lazy_padding_ver2)
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Spacer(modifier = Modifier
                        .size(size = 32.dp)
                        .clip(shape = CircleShape)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                        .clickable { changeColorSection.value = item }
                        .background(color = Color(item.colorSection), shape = CircleShape))
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon( imageVector = Icons.Filled.Delete, null,
                        tint = MaterialTheme.colorScheme.primary ,
                        modifier = Modifier.clickable { doDeleteSelected(listOf(
                            SectionDB(nameSection = item.nameSection,
                                idSection = item.idSection,
                                colorSection = item.colorSection,
                                isSelected = true))) })
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ButtonCircle(Modifier.size(40.dp), Icons.Filled.AddCircle) { editNameSection.value = SectionDB() }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable fun AddEditUnits(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenState,
    doChangeUnit: (UnitApp) -> Unit,
    doDeleteUnits: (List<UnitApp>) -> Unit,)
{
    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val itemList = uiState.unitApp
    if (isSelectedId.value > 0L) {
        itemList.find { it.idUnit == isSelectedId.value }?.let { it.isSelected = !it.isSelected }
        isSelectedId.value = 0
    }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))) {
        Column {
            HeaderSection(text = stringResource(R.string.edit_unit_list), modifier)
            LazyColumnUnits(
                uiState = uiState,
                doDeleteSelected = doDeleteUnits,
                changeUnit = doChangeUnit,
                doSelected = { idItem -> isSelectedId.value = idItem })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LazyColumnUnits(
    uiState: SettingsScreenState,
    doDeleteSelected: (List<UnitApp>) -> Unit,
    changeUnit: (UnitApp) -> Unit,
    doSelected: (Long) -> Unit
) {
    val editUnit: MutableState<UnitApp?> = remember { mutableStateOf(null) }

    if (editUnit.value != null) {
        EditUnitDialog(
            unitApp = editUnit.value!!,
            onDismiss = { editUnit.value = null },
            onConfirm = {
                changeUnit(it)
                editUnit.value = null
            }
        )
    }

    Column {
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.heightIn(min = 0.dp, max = 300.dp)
        ) {
            items(uiState.unitApp) { item ->
                Box {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .clickable { doSelected(item.idUnit) }
                    ) {
                        Spacer( modifier = Modifier
                            .background(if (item.isSelected) Color.Red else Color.LightGray)
                            .width(8.dp)
                            .heightIn(min = 8.dp,max = 32.dp).fillMaxHeight()
                            .align(Alignment.CenterVertically))
                        TextApp(
                            text = item.nameUnit,
                            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ButtonCircle(Modifier.size(40.dp), Icons.Filled.AddCircle) { editUnit.value = UnitDB() }
            Spacer(modifier = Modifier.width(12.dp))
            ButtonCircle(Modifier.size(40.dp), Icons.Filled.ChangeCircle) {
                uiState.unitApp.find { it.isSelected }?.let { editUnit.value = it } }
            Spacer(modifier = Modifier.width(12.dp))
            ButtonCircle(Modifier.size(40.dp), Icons.Filled.Delete) {
                uiState.unitApp.find { it.isSelected }?.let { doDeleteSelected(uiState.unitApp) }
            }
        }
    }
}

@Composable fun ChangeStyle(){
    var sliderPosition by remember{ mutableFloatStateOf(AppBase.scale.toFloat()) }
    HeaderSection(text = stringResource(R.string.font_size), Modifier)
    Slider(
        value = sliderPosition,
        valueRange = 0f..2f,
        steps = 1,
        enabled = true,
        onValueChange = {sliderPosition = it},
        onValueChangeFinished = { AppBase.scale = sliderPosition.toInt() },
        colors = SliderDefaults.colors(
            thumbColor = Color(0xFF575757),
            activeTrackColor = Color(0xFFA2A2A2),
            inactiveTrackColor = Color(0xFFA2A2A2),
            inactiveTickColor = Color(0xFFA2A2A2),
            activeTickColor = Color(0xFF575757)
        )
    )

}
@Composable fun FontStyleView(){
    Text(text = "displayLarge", style = MaterialTheme.typography.displayLarge)
    Text(text = "displayMedium", style = MaterialTheme.typography.displayMedium)
    Text(text = "displaySmall", style = MaterialTheme.typography.displaySmall)
    Text(text = "headlineLarge", style = MaterialTheme.typography.headlineLarge)
    Text(text = "headlineMedium", style = MaterialTheme.typography.headlineMedium)
    Text(text = "headlineSmall", style = MaterialTheme.typography.headlineSmall)
    Text(text = "titleLarge", style = MaterialTheme.typography.titleLarge)
    Text(text = "titleMedium", style = MaterialTheme.typography.titleMedium)
    Text(text = "titleSmall", style = MaterialTheme.typography.titleSmall)
    Text(text = "bodyLarge", style = MaterialTheme.typography.bodyLarge)
    Text(text = "bodyMedium", style = MaterialTheme.typography.bodyMedium)
    Text(text = "bodySmall", style = MaterialTheme.typography.bodySmall)
    Text(text = "labelLarge", style = MaterialTheme.typography.labelLarge)
    Text(text = "labelMedium", style = MaterialTheme.typography.labelMedium)
    Text(text = "labelSmall", style = MaterialTheme.typography.labelSmall)
}

@Composable
@Preview
fun LazyColumnUnitsPreview(){
    LazyColumnUnits(SettingsScreenState(), {}, {}, {})
}