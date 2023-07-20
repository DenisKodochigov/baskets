package com.example.shopping_list.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.UnitDB
import com.example.shopping_list.entity.UnitApp
import com.example.shopping_list.ui.components.ButtonCircle
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.MyTextH1
import com.example.shopping_list.ui.components.SwitcherButton
import com.example.shopping_list.ui.components.dialog.EditUnitDialog
import com.example.shopping_list.utils.ColorPicker
import com.example.shopping_list.utils.ColorPicker1

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.settingScreenState.collectAsState()

    LayoutSettingsScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        doChangeUnit = { unit -> viewModel.changeUnit(unit) },
        doDeleteUnits = { units -> viewModel.doDeleteUnits(units) },
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun LayoutSettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsScreenState,
    doChangeUnit: (UnitApp) -> Unit,
    doDeleteUnits: (List<UnitApp>) -> Unit,
){
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val itemList = uiState.unitApp
    if (isSelectedId.value > 0L) {
        itemList.find { it.idUnit == isSelectedId.value }?.let { it.isSelected = !it.isSelected }
        isSelectedId.value = 0
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))) {
        Column(modifier.fillMaxHeight()) {
            HeaderScreen(text = stringResource(R.string.settings_page), modifier)
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                LazyColumnUnits(
                    uiState = uiState,
                    doDeleteSelected = doDeleteUnits,
                    changeUnit = doChangeUnit,
                    doSelected = { idItem -> isSelectedId.value = idItem })
                Spacer(modifier = Modifier.height(20.dp))
                ColorPicker1()
            }
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
            onConfirm = changeUnit
        )
    }

    Column( Modifier.verticalScroll(rememberScrollState())) {
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
                    Row(modifier = Modifier
                        .clip(shape = RoundedCornerShape(6.dp))
                        .fillMaxWidth()
                        .background(Color.White)
                        .clickable { doSelected(item.idUnit) }
                    ) {
                        Spacer( modifier = Modifier
                            .background(if (item.isSelected) Color.Red else Color.LightGray)
                            .width(8.dp)
                            .height(32.dp)
                            .align(Alignment.CenterVertically))
                        MyTextH1(
                            text = item.nameUnit,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(bottom = dimensionResource(R.dimen.lazy_padding_ver1))
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ButtonCircle(Modifier, Icons.Filled.AddCircle) { editUnit.value = UnitDB() }
            Spacer(modifier = Modifier.width(12.dp))
            ButtonCircle(Modifier, Icons.Filled.ChangeCircle) {
                uiState.unitApp.find { it.isSelected }?.let { editUnit.value = it } }
            Spacer(modifier = Modifier.width(12.dp))
            ButtonCircle(Modifier, Icons.Filled.Delete) {
                uiState.unitApp.find { it.isSelected }?.let { doDeleteSelected(uiState.unitApp) }
            }
        }
    }
}
@Composable
@Preview
fun LazyColumnUnitsPreview(){
    LazyColumnUnits(SettingsScreenState(), {}, {}, {})
}