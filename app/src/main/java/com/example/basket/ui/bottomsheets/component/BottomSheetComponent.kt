package com.example.basket.ui.bottomsheets.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.TypeText
import com.example.basket.ui.components.ButtonApp
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.TextFieldApp
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.styleApp

@Composable fun FieldName(enterValue: MutableState<String>)
{
    Row(modifier = Modifier.fillMaxWidth()){
        TextFieldApp(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Start,
            enterValue = enterValue,
            typeKeyboard = TypeKeyboard.TEXT)
    }
}
@Composable
fun RowSelectedProduct(uiState: BottomSheetInterface)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth())
    {
        ButtonDialogSelectProduct( uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))

        TextApp(text = if (uiState.enteredNameProduct.value != "") uiState.enteredNameProduct.value
                         else stringResource(id = R.string.bs_not_selected),
            style = styleApp(nameStyle = TypeText.EDIT_TEXT),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth().weight(1 - uiState.weightButton).padding(start = 12.dp))
    }
}
@Composable
fun RowSelectedSection(uiState: BottomSheetInterface)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ButtonDialogSelectSection(uiState,
            modifier = Modifier
                .weight(uiState.weightButton)
                .padding(end = 4.dp)
                .fillMaxWidth())
        TextApp(text = if (uiState.enteredNameSection.value != "") uiState.enteredNameSection.value
                        else uiState.selectedSection.value?.nameSection ?:
                                stringResource(id = R.string.bs_not_selected),
            textAlign = TextAlign.Start,
            style = styleApp(nameStyle = TypeText.EDIT_TEXT),
            modifier = Modifier.weight((1 - uiState.weightButton)).padding(start = 12.dp).fillMaxWidth())
    }
}
@Composable
fun RowSelectedUnit(uiState: BottomSheetInterface, amount: Boolean = true)
{
    val weightFieldAmount = if (amount) (1 - uiState.weightButton) * 0.3f else 0f
    val weightText = 1 - uiState.weightButton - weightFieldAmount

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        ButtonDialogSelectUnit(uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))
        if (amount) {
            FieldAmount(uiState, modifier = Modifier
                .fillMaxWidth()
                .weight(weight = weightFieldAmount)
                .padding(start = 12.dp))
        }
        TextApp(
            modifier = Modifier.fillMaxWidth().weight(weightText).padding(start = 12.dp),
            textAlign = TextAlign.Start,
            style = styleApp(nameStyle = TypeText.EDIT_TEXT),
            text = if (uiState.enteredNameUnit.value != "") uiState.enteredNameUnit.value
                    else uiState.selectedUnit.value?.nameUnit ?:
                                  stringResource(id = R.string.bs_unit_not_selected),
        )
    }
}
@Composable
fun ButtonDialogSelectProduct(uiState: BottomSheetInterface, modifier: Modifier)
{
    ButtonApp(
        modifier = modifier,
        text = stringResource(id = R.string.product),
        onClick = { uiState.buttonDialogSelectArticleProduct.value = true},
        enabled = true )
}
@Composable
fun ButtonDialogSelectSection(uiState: BottomSheetInterface, modifier: Modifier)
{
    ButtonApp(
        modifier = modifier,
        text = stringResource(id = R.string.section),
        onClick = {uiState.buttonDialogSelectSection.value = true},
        enabled = (uiState.selectedProduct.value?.idArticle == 0L) ||
                (uiState.selectedProduct.value == null))
}
@Composable
fun ButtonDialogSelectUnit(uiState: BottomSheetInterface, modifier: Modifier)
{
    ButtonApp(
        modifier = modifier,
        text = stringResource(id = R.string.units),
        onClick =  {uiState.buttonDialogSelectUnit.value = true},
        enabled = (uiState.selectedProduct.value?.idArticle == 0L) ||
                    (uiState.selectedProduct.value == null), )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun FieldAmount(uiState: BottomSheetInterface, modifier: Modifier)
{
    TextFieldApp(
        modifier = modifier
            .width(80.dp)
            .height(40.dp),
        textAlign = TextAlign.Center,
        enterValue = uiState.enteredAmount,
        typeKeyboard = TypeKeyboard.DIGIT)
}

@Composable
fun ButtonConfirm(onConfirm: ()->Unit)
{
    Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
    ButtonApp(text = stringResource(id = R.string.ok), onClick = onConfirm )
}
