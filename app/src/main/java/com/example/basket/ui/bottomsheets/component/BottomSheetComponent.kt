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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.basket.entity.TypeKeyboard
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.components.TextFieldApp
import com.example.basket.ui.theme.Dimen

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
        ButtonDialogSelectProduct(uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(uiState.weightButton)
                .padding(end = 4.dp))

        Text(text = if (uiState.enteredNameProduct.value != "") uiState.enteredNameProduct.value
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
        Text(text = if (uiState.enteredNameSection.value != "") uiState.enteredNameSection.value
                    else uiState.selectedSection.value?.nameSection ?:
                                stringResource(id = R.string.bs_section_not_selected),
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
        if (amount) {
            FieldAmount(uiState, modifier = Modifier
                .fillMaxWidth().weight(weight = weightFieldAmount).padding(start = 12.dp))
        }
        Text(
            modifier = Modifier.fillMaxWidth().weight(weightText).padding(start = 12.dp),
            text = if (uiState.enteredNameUnit.value != "") uiState.enteredNameUnit.value
                    else uiState.selectedUnit.value?.nameUnit ?:
                                  stringResource(id = R.string.bs_unit_not_selected),
        )
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
        enabled = (uiState.selectedProduct.value?.idArticle == 0L) ||
                    (uiState.selectedProduct.value == null) ,
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
        enabled = (uiState.selectedProduct.value?.idArticle == 0L) ||
                (uiState.selectedProduct.value == null) ,
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
    Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
    Button(
        onClick = { uiState.onConfirmation(returnSelectedProduct(uiState)) },
        modifier = Modifier.clip(shape = MaterialTheme.shapes.large)
    ){
        Text(text = stringResource(id = R.string.ok))
    }
}

@Composable fun ButtonConfirmText(onConfirm: ()->Unit)
{
    Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
    TextButtonOK(onConfirm = onConfirm)
}
fun returnSelectedProduct(uiState: BottomSheetInterface): Product
{
    val section = if (uiState.selectedSection.value != null) {
        if (uiState.selectedSection.value!!.nameSection == uiState.enteredNameSection.value){
            uiState.selectedSection.value!!
        } else SectionDB(nameSection = uiState.enteredNameSection.value)
    } else SectionDB(nameSection = uiState.enteredNameSection.value)

    val unitA = if (uiState.selectedUnit.value != null) {
        if (uiState.selectedUnit.value!!.nameUnit == uiState.enteredNameUnit.value){
            uiState.selectedUnit.value!!
        } else UnitDB(nameUnit = uiState.enteredNameUnit.value)
    } else UnitDB(nameUnit = uiState.enteredNameUnit.value)

    val article = if (uiState.selectedProduct.value != null){
        if (uiState.selectedProduct.value!!.nameArticle != uiState.enteredNameProduct.value){
            ArticleDB(
                nameArticle = uiState.enteredNameProduct.value,
                sectionId = section.idSection,
                unitId = unitA.idUnit
            )
        } else { uiState.selectedProduct.value!!}
    } else {
        ArticleDB(
            nameArticle = uiState.enteredNameProduct.value,
            sectionId = section.idSection,
            unitId = unitA.idUnit
        )
    }

    return ProductDB(
        value = if (uiState.enteredAmount.value.isEmpty()) 1.0
        else uiState.enteredAmount.value.toDouble(),
        putInBasket = false,
        articleId = if (uiState.selectedProduct.value == null) 0
        else uiState.selectedProduct.value!!.idArticle,
        article = ArticleDB(
            idArticle = article.idArticle,
            nameArticle = article.nameArticle,
            position = 0,
            isSelected = false,
            section = section as SectionDB,
            unitApp = unitA as UnitDB
        )
    )
}