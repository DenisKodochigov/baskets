package com.example.basket.ui.bottomsheets.basketAdd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.TypeText
import com.example.basket.ui.bottomsheets.component.ButtonConfirm
import com.example.basket.ui.components.HeaderScreen
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.screens.baskets.BasketScreenState
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.styleApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetBasketAdd(uiState: BasketScreenState)
{
    uiState.enteredName.value =""
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, confirmValueChange = { true },)
    ModalBottomSheet(
        onDismissRequest = uiState.onDismiss,
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = Dimen.bsPaddingHor1),
        shape = shapesApp.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetBasketAddContent(uiState) })
}

@Composable fun BottomSheetBasketAddContent(uiState: BasketScreenState)
{
    Column( horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(Dimen.bsItemPaddingHor)
    ) {
        HeaderScreen(text = stringResource(R.string.new_basket))
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        FieldNameBasket(uiState)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        ButtonOK(uiState)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
    }
}

@Composable fun FieldNameBasket(uiState: BasketScreenState)
{
    MyOutlinedTextFieldWithoutIcon(
        modifier = Modifier.fillMaxWidth().testTag(TagsTesting.BASKET_BS_INPUT_NAME),
        enterValue = uiState.enteredName,
        typeKeyboard = TypeKeyboard.TEXT,
        label = {
            TextApp(text = stringResource(R.string.new_basket),
            style = styleApp( nameStyle = TypeText.NAME_SECTION)
        )},
        keyboardActionsOnDone = {
            uiState.onAddClick(uiState.enteredName.value)
            uiState.enteredName.value = ""
            uiState.onDismiss()
        }
    )
}
@Composable fun ButtonOK(uiState: BasketScreenState){
    ButtonConfirm(onConfirm = {
        uiState.onAddClick(uiState.enteredName.value)
        uiState.onDismiss() })
}