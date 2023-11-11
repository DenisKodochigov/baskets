package com.example.basket.ui.bottomsheets.bottomSheetProductAdd

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.Product
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TypeKeyboard
import com.example.basket.ui.bottomsheets.bottomSheetProductSelect.BottomSheetProductSelect
import com.example.basket.ui.components.TextFieldApp
import com.example.basket.ui.screens.products.ProductsScreenState
import com.example.basket.utils.log

const val showLog = true

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetProductAddGeneral (uiStateP: ProductsScreenState, onAddProduct: (Product) -> Unit)
{
    val uiState by remember{ mutableStateOf(BottomSheetProductAddState()) }
    uiState.onConfirmation = onAddProduct
    uiState.onDismiss = uiStateP.onDismiss

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)
    ModalBottomSheet(
        onDismissRequest = uiState.onDismiss,
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_padding_hor)),
        shape = MaterialTheme.shapes.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetProductAddGeneralLayOut(uiState) })
    if (uiState.buttonDialogSelectProduct.value){
        log(showLog, "BottomSheetProductAddGeneral buttonDialogSelectProduct.value = ${uiState.buttonDialogSelectProduct.value}")
        BottomSheetProductSelect(uiState)
    }
}
@Composable
fun BottomSheetProductAddGeneralLayOut (uiState: BottomSheetProductAddState)
{
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
        GroupButtons(uiState)
        ButtonConfirm(uiState)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
    }
}

@Composable fun GroupButtons(uiState: BottomSheetProductAddState)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        RowSelectedProduct(uiState)
        RowSelectedSection(uiState)
        RowSelectedUnit(uiState)
    }
}

@Composable fun RowSelectedProduct(uiState: BottomSheetProductAddState)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth())
    {
        ButtonDialogSelectProduct(uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))
        Text(text = uiState.enteredProduct.value?.nameArticle ?: "Product not selected",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1 - uiState.weightButton)
                .padding(start = 12.dp))
    }
}

@Composable fun RowSelectedSection(uiState: BottomSheetProductAddState)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ButtonDialogSelectSection(uiState,
            modifier = Modifier
                .weight(uiState.weightButton)
                .padding(end = 4.dp)
                .fillMaxWidth())
        Text(text = uiState.selectedSection.value?.nameSection ?: "Section not selected",
            modifier = Modifier
                .weight((1 - uiState.weightButton))
                .padding(start = 12.dp)
                .fillMaxWidth())
    }
}

@Composable fun RowSelectedUnit(uiState: BottomSheetProductAddState)
{
    val weightFieldAmount = (1 - uiState.weightButton) * 0.3f
    val weightText = 1 - uiState.weightButton - weightFieldAmount

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ButtonDialogSelectUnit(uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))
        FieldAmount(uiState, modifier =
        Modifier
            .fillMaxWidth()
            .weight(weight = weightFieldAmount)
            .padding(start = 12.dp))
        Text(text = uiState.selectedUnit.value?.nameUnit ?: "Unit not selected",
            modifier = Modifier
                .fillMaxWidth()
                .weight(weightText)
                .padding(start = 12.dp))
    }
}

@Composable fun ButtonDialogSelectProduct(uiState: BottomSheetProductAddState, modifier: Modifier)
{
    Button(
        onClick = {
            uiState.buttonDialogSelectProduct.value = true
            log(showLog, "ButtonDialogSelectProduct buttonDialogSelectProduct.value = ${uiState.buttonDialogSelectProduct.value}")
                  },
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ) {
        Text(text = stringResource(id = R.string.products))
    }
}

@Composable fun ButtonDialogSelectSection(uiState: BottomSheetProductAddState, modifier: Modifier)
{
    Button(
        onClick = {uiState.buttonDialogSelectSection.value = true},
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.sections))
    }
}

@Composable fun ButtonDialogSelectUnit(uiState: BottomSheetProductAddState, modifier: Modifier)
{
    Button(
        onClick = {uiState.buttonDialogSelectUnit.value = true},
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.units))
    }
}
@SuppressLint("SuspiciousIndentation")
@Composable fun FieldAmount(uiState: BottomSheetProductAddState, modifier: Modifier)
{
    TextFieldApp(
        modifier = modifier,
        textAlign = TextAlign.Center,
        enterValue = uiState.enteredAmount,
        typeKeyboard = TypeKeyboard.DIGIT)
}

@Composable fun ButtonConfirm(uiState: BottomSheetProductAddState)
{
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
    Button(
        onClick = {uiState.onConfirmation},
        modifier = Modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.add_product))
    }
}
@Preview
@Composable fun BottomSheetProductSelectGeneralPreview(){
    BottomSheetProductAddGeneralLayOut(BottomSheetProductAddState())
}