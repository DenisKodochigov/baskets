package com.example.basket.ui.baskets

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
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.components.CollapsingToolbar
import com.example.basket.ui.components.HeaderScreen
import com.example.basket.ui.components.TextApp
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.components.dialog.EditBasketName
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.DismissBackground
import com.example.basket.utils.bottomBarAnimatedScroll
import com.example.basket.utils.log
import java.util.*
import kotlin.math.roundToInt

const val showLog = false

@SuppressLint("UnrememberedMutableState")
@Composable
fun BasketsScreen(
    onClickBasket: (Long) -> Unit,
    screen: ScreenDestination,
    showBottomSheet: MutableState<Boolean>) {

    val viewModel: BasketViewModel = hiltViewModel()
    viewModel.getListBasket()
    log(showLog, "BasketsScreen")
    BasketScreenCreateView(
        onClickBasket = onClickBasket,
        screen = screen,
        viewModel = viewModel,
        showBottomSheet = showBottomSheet,
    )
}

@Composable
fun BasketScreenCreateView(
    screen: ScreenDestination,
    onClickBasket: (Long) -> Unit,
    viewModel: BasketViewModel,
    showBottomSheet: MutableState<Boolean>,
) {
    val uiState by viewModel.basketScreenState.collectAsState()
    log(showLog, "BasketScreenCreateView ${uiState.baskets.size}")
    if (showBottomSheet.value)
        AddBasketBottomSheet(
            onAddClick = { viewModel.addBasket(it) }, onDismiss = { showBottomSheet.value = false })
    BasketsScreenLayout(
        onClickBasket = onClickBasket,
        uiState = uiState,
        screen = screen,
        changeNameBasket = { basket -> viewModel.changeNameBasket(basket) },
        deleteBasket = { basketId -> viewModel.deleteBasket(basketId) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketsScreenLayout(
    uiState: BasketScreenState,
    screen: ScreenDestination,
    onClickBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
    deleteBasket: (Long) -> Unit,
) {
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    log(showLog, "BasketsScreenLayout")
    Column(Modifier
        .fillMaxHeight()
        .bottomBarAnimatedScroll(
            height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
            offsetHeightPx = bottomBarOffsetHeightPx),) {
        BasketLazyColumn(
            uiState = uiState,
            screen = screen,
            onClickBasket = onClickBasket,
            deleteBasket = deleteBasket,
            changeNameBasket= changeNameBasket,
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
    deleteBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
) {
    val listState = rememberLazyListState()
    val editBasket: MutableState<Basket?> = remember { mutableStateOf(null) }
    val show = remember { mutableStateOf(true) }

    if (editBasket.value != null) {
        EditBasketName(
            basket = editBasket.value!! as BasketDB,
            onDismiss = { editBasket.value = null },
            onConfirm = {
                changeNameBasket(editBasket.value!!)
                editBasket.value = null
            },
        )
    }

    CollapsingToolbar(
        text = stringResource(screen.textHeader),
        idImage = screen.picture,
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver)).testTag(BASKET_LAZY)
    )
    {
//        item { HeaderImScreen(text = stringResource(R.string.baskets), idImage = R.drawable.bas) }
        items(items = uiState.baskets, key = { it.idBasket })
        { item ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        show.value = false
                        deleteBasket(item.idBasket)
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
                text = SimpleDateFormat("dd-MM", Locale.getDefault()).format(basket.dateB),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL),
            )
            TextApp(
                text = SimpleDateFormat("yyyy", Locale.getDefault()).format(basket.dateB),
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
            text = basket.quantity.toString(),
            textAlign = TextAlign.Left,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))
        )
    }
    log(showLog, "ElementColumBasket ${basket.nameBasket}")
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBasketBottomSheet(onAddClick: (String) -> Unit, onDismiss: () -> Unit) {

    log(showLog, "AddBasketBottomSheet")
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
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(horizontal = 56.dp).testTag(BASKETBOTTOMSHEET),
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
                .padding(24.dp, 24.dp, 24.dp, 32.dp)
        ) {
            Spacer(Modifier.height(1.dp))
            HeaderScreen(text = stringResource(R.string.add_basket))
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = nameNewBasket,
                singleLine = true,
                textStyle = styleApp(nameStyle = TypeText.NAME_SECTION),
                label = { Text(text = stringResource(R.string.new_name_basket)) },
                onValueChange = { nameNewBasket = it },
                modifier = Modifier.fillMaxWidth().testTag(BASKET_BS_INPUT_NAME),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddClick(nameNewBasket)
                        keyboardController?.hide()
                        nameNewBasket = ""
                        localFocusManager.clearFocus()
                        onDismiss()
                    }
                ),
            )
            Spacer(Modifier.height(36.dp))
            TextButtonOK(
                enabled = nameNewBasket != "",
                onConfirm = {
                    onAddClick(nameNewBasket)
                    onDismiss()
                })
            Spacer(Modifier.height(36.dp))
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun BottomSheetContentBasketPreview() {
    AddBasketBottomSheet({}, {})
}