package com.example.basket.ui.bottomsheets.bottomSheetProductSelect

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.basket.R
import com.example.basket.entity.TagsTesting
import com.example.basket.ui.bottomsheets.bottomSheetProductAdd.BottomSheetProductAddState

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetProductSelect(uiStateUp: BottomSheetProductAddState)
{
    val uiState by remember{ mutableStateOf(BottomSheetProductSelectState())}

    uiState.onConfirmation = uiStateUp.onConfirmation
    uiState.onDismiss = uiStateUp.onDismiss

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
        content = { BottomSheetProductSelectLayout(uiState) })
}
@Composable fun BottomSheetProductSelectLayout(uiState: BottomSheetProductSelectState) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
        ButtonConfirm(uiState)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver)))
    }
}
@Composable fun ButtonConfirm(uiState: BottomSheetProductSelectState)
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
@Composable fun BottomSheetProductSelectLayoutPreview(){
    BottomSheetProductSelectLayout(BottomSheetProductSelectState())
}