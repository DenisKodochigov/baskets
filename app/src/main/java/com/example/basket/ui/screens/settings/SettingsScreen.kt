package com.example.basket.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basket.AppBase
import com.example.basket.R
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Section
import com.example.basket.entity.TypeText
import com.example.basket.entity.UPDOWN
import com.example.basket.entity.UnitApp
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.components.ButtonCircle
import com.example.basket.ui.components.CollapsingToolbar
import com.example.basket.ui.components.HeaderSection
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.dialog.settingDialog.ChangeColorSectionDialog
import com.example.basket.ui.components.dialog.settingDialog.ChangeNameSectionDialog
import com.example.basket.ui.components.dialog.settingDialog.EditUnitDialog
import com.example.basket.ui.components.ShowArrowVer
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.styleApp


private val thumbSize = 30.dp
@Composable fun SettingsScreen(refreshScreen: MutableState<Boolean>, screen: ScreenDestination)
{
    val viewModel: SettingsViewModel = hiltViewModel()
    SettingsScreenInit( viewModel, refreshScreen = refreshScreen, screen = screen )
}

@Composable fun SettingsScreenInit(
    viewModel: SettingsViewModel, screen: ScreenDestination, refreshScreen: MutableState<Boolean>)
{
    val uiState by viewModel.settingScreenState.collectAsState()

    uiState.refresh = refreshScreen
    uiState.doChangeUnit = remember{{ unit -> viewModel.changeUnit(unit) }}
    uiState.doDeleteUnits = remember{{ units -> viewModel.doDeleteUnits(units) }}
    uiState.doChangeSection = remember{{ section -> viewModel.doChangeSection(section) }}
    uiState.doDeleteSections = remember{{ sections -> viewModel.doDeleteSections(sections) }}
    uiState.idImage = getIdImage(screen)
    uiState.screenTextHeader = stringResource(screen.textHeader)

    SettingsScreenLayout(
        modifier = Modifier.padding(bottom = Dimen.screenPaddingHor),
        uiState = uiState,
    )
}
@SuppressLint("UnrememberedMutableState")
@Composable fun SettingsScreenLayout(
    modifier: Modifier = Modifier, uiState: SettingsScreenState, )
{
    Column{
        CollapsingToolbar(
            text = uiState.screenTextHeader,
            idImage = uiState.idImage,
            refreshScreen = uiState.refresh,
            scrollOffset = 0)
        Column (
            Modifier.verticalScroll(
                state = rememberScrollState(),
                enabled = true,
                flingBehavior = null,
                reverseScrolling = false)
        ){
            AddEditUnits(modifier, uiState)
            AddEditSection(modifier, uiState)
            ChangeStyle(uiState)
            FontStyleView()
        }
    }
}
@Composable fun AddEditSection(modifier: Modifier = Modifier, uiState: SettingsScreenState)
{
    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val itemList = uiState.section
    if (isSelectedId.value > 0L) {
        itemList.find { it.idSection == isSelectedId.value }?.let { it.isSelected = !it.isSelected }
        isSelectedId.value = 0
    }
    Box(modifier.fillMaxSize().padding(horizontal = Dimen.screenPaddingHor)) {
        Column{
//            val plug = refreshScreen
            HeaderSection(text = stringResource(R.string.edit_section_list), modifier, uiState.refresh)
            SectionLazyColumn( uiState = uiState, doSelected = { idItem -> isSelectedId.value = idItem })
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionLazyColumn(uiState: SettingsScreenState, doSelected: (Long) -> Unit)
{
    val editNameSection: MutableState<Section?> = remember { mutableStateOf(null) }
    val changeColorSection: MutableState<Section?> = remember { mutableStateOf(null) }
    val listState = rememberLazyListState()

    if (editNameSection.value != null) {
        ChangeNameSectionDialog(
            section = editNameSection.value!!,
            onDismiss = { editNameSection.value = null},
            onConfirm = {
                uiState.doChangeSection(it)
                editNameSection.value = null },)
    }
    if (changeColorSection.value != null) {
        ChangeColorSectionDialog(
            section = changeColorSection.value!!,
            onDismiss = { changeColorSection.value = null},
            onConfirm = {
                uiState.doChangeSection(it)
                changeColorSection.value = null },)
    }

    val showArrowUp = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 }}.value
    val showArrowDown = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
                listState.layoutInfo.totalItemsCount - 1 } }.value
    Column {
        ShowArrowVer(direction = UPDOWN.UP, enable = showArrowUp && uiState.section.isNotEmpty(), drawLine = true)
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(min = 0.dp, max = 250.dp)
        ) {
            items(items = uiState.section) {item ->
                ContextLazySection(
                    item = item,
                    editNameSection = editNameSection,
                    changeColorSection = changeColorSection,
                    doDeleteSelected = uiState.doDeleteSections,
                    doSelected = doSelected,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
        ShowArrowVer(direction = UPDOWN.DOWN, enable =  showArrowDown && uiState.section.isNotEmpty(), drawLine = true)
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ButtonCircle(Modifier.size(40.dp), Icons.Filled.AddCircle) { editNameSection.value = SectionDB() }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable fun ContextLazySection(
    item: Section,
    editNameSection: MutableState<Section?>,
    changeColorSection: MutableState<Section?>,
    doDeleteSelected: (List<Section>) -> Unit,
    doSelected: (Long) -> Unit,
    modifier: Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
                    horizontal = Dimen.lazyPaddingHor2,
                    vertical = Dimen.lazyPaddingVer2
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier
            .size(size = 32.dp)
            .clip(shape = CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
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

@Composable fun AddEditUnits(modifier: Modifier = Modifier, uiState: SettingsScreenState)
{
    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val itemList = uiState.unitApp
    if (isSelectedId.value > 0L) {
        itemList.find { it.idUnit == isSelectedId.value }?.let { it.isSelected = !it.isSelected }
        isSelectedId.value = 0
    }
    Box( Modifier.fillMaxWidth().padding(horizontal = Dimen.screenPaddingHor)) {
        Column {
            HeaderSection(text = stringResource(R.string.edit_unit_list), modifier, uiState.refresh)
            LazyColumnUnits(uiState = uiState, doSelected = { idItem -> isSelectedId.value = idItem })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LazyColumnUnits(uiState: SettingsScreenState, doSelected: (Long) -> Unit)
{
    val editUnit: MutableState<UnitApp?> = remember { mutableStateOf(null) }

    if (editUnit.value != null) {
        EditUnitDialog(
            unitApp = editUnit.value!!,
            onDismiss = { editUnit.value = null },
            onConfirm = {
                uiState.doChangeUnit(it)
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
                Box (modifier = Modifier.animateItemPlacement()){
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
                            .heightIn(min = 8.dp, max = 32.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically))
                        TextApp(
                            text = item.nameUnit,
                            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
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
                uiState.unitApp.find { it.isSelected }?.let { uiState.doDeleteUnits(uiState.unitApp) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun ChangeStyle(uiState: SettingsScreenState){
    var sliderPosition by remember{ mutableFloatStateOf(AppBase.scale.toFloat()) }

    HeaderSection(
        text = stringResource(R.string.font_size),
        modifier = Modifier,
        refreshScreen = uiState.refresh
    )
    Slider(
        value = sliderPosition,
        modifier = Modifier.padding(horizontal = 16.dp),
        valueRange = 0f..2f,
        steps = 1,
        enabled = true,
        onValueChange = {
            sliderPosition = it
            AppBase.scale = sliderPosition.toInt()
            uiState.refresh.value = !uiState.refresh.value
        },
        thumb = {
            Box(
                modifier = Modifier
                    .thumb(size = 20.dp, shape = CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
            ){
                Text(text = "A",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = TextStyle(fontSize = when (sliderPosition) {
                        0f -> 12.sp
                        1f -> 18.sp
                        else -> 24.sp
                    })
                )
            }
        },
    )
}

fun Modifier.thumb(size: Dp = thumbSize, shape: Shape = CircleShape) =
    defaultMinSize(minWidth = size, minHeight = size).clip(shape)
@Composable fun FontStyleView(){
    val tg =  MaterialTheme.typography
    Text(text = "displayLarge ${tg.displayLarge.fontSize}", style = tg.displayLarge)
    Text(text = "displayMedium ${tg.displayMedium.fontSize}", style = tg.displayMedium)
    Text(text = "displaySmall ${tg.displaySmall.fontSize}", style = tg.displaySmall)
    Text(text = "headlineLarge ${tg.headlineLarge.fontSize}", style = tg.headlineLarge)
    Text(text = "headlineMedium ${tg.headlineMedium.fontSize}", style = tg.headlineMedium)
    Text(text = "headlineSmall ${tg.headlineSmall.fontSize}", style = tg.headlineSmall)
    Text(text = "titleLarge ${tg.titleLarge.fontSize}", style = tg.titleLarge)
    Text(text = "titleMedium ${tg.titleMedium.fontSize}", style = tg.titleMedium)
    Text(text = "titleSmall ${tg.titleSmall.fontSize}", style = tg.titleSmall)
    Text(text = "bodyLarge ${tg.bodyLarge.fontSize}", style = tg.bodyLarge)
    Text(text = "bodyMedium ${tg.bodyMedium.fontSize}", style = tg.bodyMedium)
    Text(text = "bodySmall ${tg.bodySmall.fontSize}", style = tg.bodySmall)
    Text(text = "labelLarge ${tg.labelLarge.fontSize}", style = tg.labelLarge)
    Text(text = "labelMedium ${tg.labelMedium.fontSize}", style = tg.labelMedium)
    Text(text = "labelSmall ${tg.labelSmall.fontSize}", style = tg.labelSmall)
}

@Composable
@Preview
fun LazyColumnUnitsPreview(){
    LazyColumnUnits(SettingsScreenState(), {})
}