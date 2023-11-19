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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.contentColorFor
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
import com.example.basket.ui.components.ShowArrowVer
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.components.dialog.EditBasketName
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.ItemSwipe
import com.example.basket.utils.animatedScroll
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableState")
@Composable
fun BasketsScreen(onClickBasket: (Long) -> Unit, screen: ScreenDestination)
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
    uiState.changeNameBasket = remember { { basket -> viewModel.changeNameBasket(basket) }}
    uiState.deleteBasket = remember {{ basketId -> viewModel.deleteBasket(basketId) }}
    uiState.onAddClick = remember {{ viewModel.addBasket(it) }}
    uiState.onDismiss = remember {{ uiState.triggerRunOnClickFAB.value = false }}
    uiState.idImage = getIdImage(screen)
    uiState.screenTextHeader = stringResource(screen.textHeader)

    screen.textFAB = stringResource(id = R.string.baskets)
    screen.onClickFAB = { uiState.triggerRunOnClickFAB.value = true}

    if (uiState.triggerRunOnClickFAB.value) AddBasketBottomSheet( uiState = uiState)

    BasketsScreenLayout(onClickBasket = onClickBasket, uiState = uiState)
}


@Composable
fun BasketsScreenLayout(
    uiState: BasketScreenState,
    onClickBasket: (Long) -> Unit,
) {
    val offsetHeightPx = remember { mutableFloatStateOf(0f) }
    Column(
        Modifier
            .fillMaxHeight()
            .animatedScroll(
                height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                offsetHeightPx = offsetHeightPx
            ),
     ){
        BasketLazyColumn(
            uiState = uiState,
            onClickBasket = onClickBasket,
            scrollOffset =-offsetHeightPx.floatValue.roundToInt())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BasketLazyColumn(
    uiState: BasketScreenState,
    scrollOffset:Int,
    onClickBasket: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val editBasket: MutableState<Basket?> = remember { mutableStateOf(null) }

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
        text = uiState.screenTextHeader,
        idImage = uiState.idImage,
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    ShowArrowVer(direction = UPDOWN.UP, enable = showArrowUp && uiState.baskets.isNotEmpty(), drawLine = false)
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.testTag(BASKET_LAZY)
    )
    {
        items(items = uiState.baskets, key = { it.idBasket })
        { item ->
            ItemSwipe(
                frontFon = { ElementColumBasket(
                        item, onClickBasket, modifier = Modifier.animateItemPlacement()) },
                actionDragLeft = { uiState.deleteBasket(item.idBasket)},
                actionDragRight = { editBasket.value = item },
            )
        }
    }
    ShowArrowVer(direction = UPDOWN.DOWN, enable = showArrowDown && uiState.baskets.isNotEmpty(), drawLine = false)
}

@Composable
fun ElementColumBasket(basket: Basket, onClickBasket: (Long) -> Unit, modifier: Modifier
){
    Row(
        modifier = modifier
            .clip(shape = shapesApp.extraSmall)
            .heightIn(min = 24.dp, max = 56.dp)
            .fillMaxWidth()
            .background(color = colorApp.surface)
            .padding(horizontal = Dimen.lazyPaddingHor, vertical = Dimen.lazyPaddingVer)
            .clickable { onClickBasket(basket.idBasket) },
        verticalAlignment = Alignment.CenterVertically
    ){
        TextApp(
            text = basket.nameBasket,
            textAlign = TextAlign.Left,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(1f))
        TextApp(
            text = if (basket.quantity != 0) { basket.quantity.toString() } else "0",
            textAlign = TextAlign.Left,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.padding(horizontal = Dimen.lazyPaddingHor)
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
            .padding(horizontal = Dimen.bsPaddingHor)
            .testTag(BASKETBOTTOMSHEET),
        shape = shapesApp.small,
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
                .padding(Dimen.bsItemPaddingHor)
        ) {
            Spacer(Modifier.height(1.dp))
            HeaderScreen(text = stringResource(R.string.new_basket))
            Spacer(Modifier.height(Dimen.bsSpacerHeight))
            OutlinedTextField(
                value = nameNewBasket,
                singleLine = true,
                textStyle = styleApp(nameStyle = TypeText.NAME_SECTION),
                label = {
                    TextApp(
                        text = stringResource(R.string.new_basket),
                        style = styleApp( nameStyle = TypeText.NAME_SECTION))
                },
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
            Spacer(Modifier.height(Dimen.bsSpacerHeight1))
            TextButtonOK(
                enabled = nameNewBasket != "",
                onConfirm = {
                    uiState.onAddClick(nameNewBasket)
                    uiState.onDismiss()
                })
            Spacer(Modifier.height(Dimen.bsSpacerHeight1))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun BottomSheetContentBasketPreview() {
    AddBasketBottomSheet(BasketScreenState())
}