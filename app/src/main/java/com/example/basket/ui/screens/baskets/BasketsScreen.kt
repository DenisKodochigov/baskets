package com.example.basket.ui.screens.baskets

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basket.R
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.entity.Basket
import com.example.basket.entity.SizeElement
import com.example.basket.entity.TagsTesting.BASKETBOTTOMSHEET
import com.example.basket.entity.TagsTesting.BASKET_BS_INPUT_NAME
import com.example.basket.entity.TagsTesting.BASKET_LAZY
import com.example.basket.entity.TypeText
import com.example.basket.entity.UPDOWN
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.components.CollapsingToolbar
import com.example.basket.ui.components.HeaderScreen
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.components.dialog.EditBasketName
import com.example.basket.ui.components.showArrowVer
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.DismissBackground
import com.example.basket.utils.bottomBarAnimatedScroll
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableState")
@Composable
fun BasketsScreen(onClickBasket: (Long) -> Unit, screen: ScreenDestination,)
{
    val viewModel: BasketViewModel = hiltViewModel()
    viewModel.getListBasket()
    BasketScreenCreateView(
        onClickBasket = onClickBasket,
        screen = screen,
        viewModel = viewModel )
}

@Composable
fun BasketScreenCreateView(
    screen: ScreenDestination,
    onClickBasket: (Long) -> Unit,
    viewModel: BasketViewModel,
) {
    val uiState by viewModel.basketScreenState.collectAsState()
    uiState.changeNameBasket = { basket -> viewModel.changeNameBasket(basket) }
    uiState.deleteBasket = { basketId -> viewModel.deleteBasket(basketId) }
    uiState.onAddClick = { viewModel.addBasket(it) }
    uiState.onDismiss = { uiState.triggerRunOnClickFAB.value = false }
    screen.textFAB = stringResource(id = R.string.baskets)
    screen.onClickFAB = { uiState.triggerRunOnClickFAB.value = true}

    if (uiState.triggerRunOnClickFAB.value) AddBasketBottomSheet( uiState = uiState)

    BasketsScreenLayout(onClickBasket = onClickBasket, uiState = uiState, screen = screen,)
}


@Composable
fun BasketsScreenLayout(
    uiState: BasketScreenState,
    screen: ScreenDestination,
    onClickBasket: (Long) -> Unit,
) {
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    Column(Modifier.fillMaxHeight()
            .bottomBarAnimatedScroll(
                height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                offsetHeightPx = bottomBarOffsetHeightPx
            ),) {
        BasketLazyColumn(
            uiState = uiState,
            screen = screen,
            onClickBasket = onClickBasket,
            scrollOffset =-bottomBarOffsetHeightPx.floatValue.roundToInt())
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BasketLazyColumn(
    uiState: BasketScreenState,
    screen: ScreenDestination,
    scrollOffset:Int,
    onClickBasket: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val editBasket: MutableState<Basket?> = remember { mutableStateOf(null) }
    val show = remember { mutableStateOf(true) }

    val showArrowUp = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 }}.value
    val showArrowDown = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
            listState.layoutInfo.totalItemsCount - 1}}.value
    if (editBasket.value != null) {
        EditBasketName(
            basket = editBasket.value!! as BasketDB,
            onDismiss = { editBasket.value = null },
            onConfirm = {
                uiState.changeNameBasket(editBasket.value!!)
                editBasket.value = null
            },
        )
    }

    CollapsingToolbar(
        text = stringResource(screen.textHeader),
        idImage = getIdImage(screen),
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    showArrowVer(direction = UPDOWN.UP, enable = showArrowUp && uiState.baskets.isNotEmpty(), drawLine = false)
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
            .testTag(BASKET_LAZY)
    )
    {
        items(items = uiState.baskets, key = { it.idBasket })
        { item ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        show.value = false
                        uiState.deleteBasket(item.idBasket)
                    } else if (it == DismissValue.DismissedToEnd) {
                        editBasket.value = item
                    }
                    false
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .animateItemPlacement(),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                background = { DismissBackground(dismissState) },
                dismissContent = { ElementColumBasket(item, onClickBasket) }
            )
        }
    }
    showArrowVer(direction = UPDOWN.DOWN, enable = showArrowDown && uiState.baskets.isNotEmpty(), drawLine = false)
}

@Composable
fun ElementColumBasket(basket: Basket, onClickBasket: (Long) -> Unit) {

    val modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .clickable { onClickBasket(basket.idBasket) },
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))
        ) {
            TextApp(
                text =  if (basket.dateB != 0L) {
                    SimpleDateFormat("dd-MM", Locale.getDefault()).format(basket.dateB)
                } else "",
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL),
            )
            TextApp(
                text = if (basket.dateB != 0L) {
                    SimpleDateFormat("dd-yyyy", Locale.getDefault()).format(basket.dateB)
                } else "",
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)
            )
        }
        TextApp(
            text = basket.nameBasket,
            textAlign = TextAlign.Left,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.weight(1f)
        )
        TextApp(
            text = if (basket.quantity != 0) { basket.quantity.toString() } else "",
            textAlign = TextAlign.Left,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))
        )
    }
}
@Composable
fun ItemAddBasketLazyColumn(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.extraSmall)
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = MaterialTheme.colorScheme.surface),
    )
    {
        TextApp(
            text = stringResource(id = R.string.add_basket),
            textAlign = TextAlign.Center,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL),
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                )
                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBasketBottomSheet(uiState: BasketScreenState) {

    var nameNewBasket by remember {
        mutableStateOf(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date().time)
        )
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },
    )

    val windowInsets = if (edgeToEdgeEnabled) WindowInsets(0) else BottomSheetDefaults.windowInsets

    ModalBottomSheet(
        onDismissRequest = uiState.onDismiss,
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_padding_hor))
            .testTag(BASKETBOTTOMSHEET),
        shape = MaterialTheme.shapes.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = windowInsets,
        sheetState = sheetState
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn((screenHeight * 0.35).dp, (screenHeight * 0.75).dp)
                .padding(dimensionResource(id = R.dimen.bottom_sheet_item_padding_hor))
        ) {
            Spacer(Modifier.height(1.dp))
            HeaderScreen(text = stringResource(R.string.new_basket))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
            OutlinedTextField(
                value = nameNewBasket,
                singleLine = true,
                textStyle = styleApp(nameStyle = TypeText.NAME_SECTION),
                label = { Text(text = stringResource(R.string.new_name_basket)) },
                onValueChange = { nameNewBasket = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(BASKET_BS_INPUT_NAME),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        uiState.onAddClick(nameNewBasket)
                        keyboardController?.hide()
                        nameNewBasket = ""
                        localFocusManager.clearFocus()
                        uiState.onDismiss()
                    }
                ),
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_1)))
            TextButtonOK(
                enabled = nameNewBasket != "",
                onConfirm = {
                    uiState.onAddClick(nameNewBasket)
                    uiState.onDismiss()
                })
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_1)))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun BottomSheetContentBasketPreview() {
    AddBasketBottomSheet(BasketScreenState())
}