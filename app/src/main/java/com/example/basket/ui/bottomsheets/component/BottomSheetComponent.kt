package com.example.basket.ui.bottomsheets.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.TypeKeyboard
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.components.TextFieldApp
import com.example.basket.utils.log

@Composable fun FieldName(uiState: BottomSheetInterface)
{
    Row(modifier = Modifier.fillMaxWidth()){
        TextFieldApp(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Start,
            enterValue = uiState.enteredNameSection,
            typeKeyboard = TypeKeyboard.TEXT)
    }
}
@Composable
fun RowSelectedProduct(uiState: BottomSheetInterface)
{
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth())
    {
        ButtonDialogSelectProduct(uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))
        Text(text = uiState.selectedProduct.value?.nameArticle ?:
                if (uiState.enteredNameProduct.value != "") uiState.enteredNameProduct.value
                else stringResource(id = R.string.bs_product_not_selected),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1 - uiState.weightButton)
                .padding(start = 12.dp))
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
        Text(text = uiState.selectedSection.value?.nameSection ?:
        if (uiState.enteredNameSection.value != "") uiState.enteredNameSection.value
        else stringResource(id = R.string.bs_section_not_selected),
            modifier = Modifier
                .weight((1 - uiState.weightButton))
                .padding(start = 12.dp)
                .fillMaxWidth())
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
        if (amount) FieldAmount(uiState, modifier =
        Modifier
            .fillMaxWidth()
            .weight(weight = weightFieldAmount)
            .padding(start = 12.dp))
        Text(text = uiState.selectedUnit.value?.nameUnit ?:
        if (uiState.enteredNameUnit.value != "") uiState.enteredNameUnit.value
        else stringResource(id = R.string.bs_unit_not_selected),
            modifier = Modifier
                .fillMaxWidth()
                .weight(weightText)
                .padding(start = 12.dp))
    }
}
@Composable
fun ButtonDialogSelectProduct(uiState: BottomSheetInterface, modifier: Modifier)
{
    Button(
        onClick = { uiState.buttonDialogSelectArticleProduct.value = true },
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ) {
        Text(text = stringResource(id = R.string.products))
    }
}
@Composable
fun ButtonDialogSelectSection(uiState: BottomSheetInterface, modifier: Modifier)
{
    Button(
        onClick = {uiState.buttonDialogSelectSection.value = true},
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.sections))
    }
}
@Composable
fun ButtonDialogSelectUnit(uiState: BottomSheetInterface, modifier: Modifier)
{
    Button(
        onClick = {uiState.buttonDialogSelectUnit.value = true},
        modifier = modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.units))
    }
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
fun ButtonConfirm(uiState: BottomSheetInterface)
{
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
    Button(
        onClick = { uiState.onConfirmation(returnSelectedProduct(uiState)) },
        modifier = Modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.ok))
    }
}
@Composable fun ButtonConfirmText(uiState: BottomSheetInterface)
{
    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
    TextButtonOK(onConfirm = { uiState.onConfirmationSelectSection.invoke(uiState) })
}
fun returnSelectedProduct(uiState: BottomSheetInterface): Product
{
    return ProductDB(
        value = if (uiState.enteredAmount.value.isEmpty()) 1.0
        else uiState.enteredAmount.value.toDouble(),
        putInBasket = false,
        articleId = if (uiState.selectedProduct.value == null) 0
        else uiState.selectedProduct.value!!.idArticle,
        article = ArticleDB(
            idArticle = if (uiState.selectedProduct.value == null) 0L
            else uiState.selectedProduct.value!!.idArticle,
            nameArticle = if (uiState.selectedProduct.value == null) uiState.enteredNameProduct.value
            else uiState.selectedProduct.value!!.nameArticle,
            position = 0,
            isSelected = false,
            section = SectionDB(
                idSection = if (uiState.selectedSection.value == null) 0
                else uiState.selectedSection.value!!.idSection,
                nameSection = if (uiState.selectedSection.value == null) uiState.enteredNameSection.value
                else uiState.selectedSection.value!!.nameSection,
            ),
            unitApp = UnitDB(
                idUnit = if (uiState.selectedUnit.value == null) 0
                else uiState.selectedUnit.value!!.idUnit,
                nameUnit = if (uiState.selectedUnit.value == null) uiState.enteredNameUnit.value
                else uiState.selectedUnit.value!!.nameUnit,)
        )
    )
}