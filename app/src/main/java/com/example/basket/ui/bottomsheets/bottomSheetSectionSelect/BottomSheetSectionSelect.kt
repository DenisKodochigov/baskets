package com.example.basket.ui.bottomsheets.bottomSheetSectionSelect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.UPDOWN
import com.example.basket.ui.bottomsheets.bottomSheetProduct.BottomSheetProductState
import com.example.basket.ui.bottomsheets.component.ButtonConfirmText
import com.example.basket.ui.bottomsheets.component.FieldName
import com.example.basket.ui.components.ShowArrowVer
import com.example.basket.ui.theme.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetSectionSelect(uiState: BottomSheetInterface)
{
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true, confirmValueChange = { true },)
    ModalBottomSheet(
        onDismissRequest = uiState.onDismissSelectSection,
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = Dimen.bsPaddingHor1),
        shape = MaterialTheme.shapes.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState,
        content = { BottomSheetSectionSelectContent(uiState) })
}
@Composable fun BottomSheetSectionSelectContent(uiState: BottomSheetInterface)
{
    Column(modifier = Modifier.fillMaxWidth().padding(
            horizontal = Dimen.bsPaddingHor,
            vertical = Dimen.bsItemPaddingVer
        ),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        FieldName(uiState.enteredNameSection)
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
        BoxExistingSection(uiState)
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
        ButtonConfirmText ( {uiState.onConfirmationSelectSection(uiState)} )
        Spacer(modifier = Modifier.height(Dimen.bsItemPaddingVer))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable fun BoxExistingSection(uiState: BottomSheetInterface)
{
    val listItems = uiState.sections.filter {
        if (uiState.enteredNameSection.value != null) {
                    it.nameSection.contains(uiState.enteredNameSection.value, ignoreCase = true)
       } else true
    }

    val listState = rememberLazyGridState()
    val showArrowUp = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 } }.value
    val showArrowDown = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
                listState.layoutInfo.totalItemsCount - 1 } }.value
    Column {
        ShowArrowVer(direction = UPDOWN.UP, enable = showArrowUp && listItems.isNotEmpty(), drawLine = true)
        LazyVerticalGrid(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            columns = GridCells.Adaptive(minSize = 90.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.height(70.dp).fillMaxWidth()
        ) {
            items(items = listItems) { section ->
                Text(
                    text = section.nameSection,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { uiState.enteredNameSection.value = section.nameSection
                                        uiState.selectedSection.value = section}
                        .animateItemPlacement()
                        .padding(4.dp)
                        .background(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        }
        ShowArrowVer(direction = UPDOWN.DOWN, enable = showArrowDown && listItems.isNotEmpty(), drawLine = true)
    }
}

@Preview
@Composable fun BottomSheetSectionSelectLayoutPreview(){
    BottomSheetSectionSelectContent(BottomSheetProductState())
}