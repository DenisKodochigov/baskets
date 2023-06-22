package com.example.shopping_list.ui.baskets

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.ui.components.ButtonSwipe
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.ui.components.dialog.EditBasketName
import com.example.shopping_list.ui.theme.TextDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun BasketsScreen(
    onClickBasket: (Long) -> Unit,
    bottomSheetHide: () -> Unit,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>) {

    val viewModel: BasketViewModel = hiltViewModel()
    viewModel.getListBasket()
    val uiState by viewModel.basketScreenState.collectAsState()

    bottomSheetContent.value = {
        BottomSheetContentBasket(
            onAddClick = {viewModel.addBasket(it)}, bottomSheetHide = bottomSheetHide) }

    LayoutBasketsScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        onClickBasket = onClickBasket,
        itemList = uiState.baskets,
        changeNameBasket = { basket -> viewModel.changeNameBasket(basket)},
        deleteBasket = { basketId -> viewModel.deleteBasket(basketId)},
        refreshPosition = { direction -> viewModel.setPositionBasket(direction)},
        bottomSheetVisible = bottomSheetVisible
    )
}

@Composable
fun LayoutBasketsScreen(
    modifier: Modifier = Modifier,
    itemList: List<Basket>,
    bottomSheetVisible: MutableState<Boolean>,
    onClickBasket: (Long) -> Unit,
    refreshPosition: (Int) -> Unit,
    changeNameBasket: (Basket) -> Unit,
    deleteBasket: (Long) -> Unit,
){
//    Log.d("KDS", "New state BasketsScreenLayout, ${itemList.size}")
    val isSelectedId: MutableState<Long> = remember {  mutableStateOf(0L) }
    val unSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    if (isSelectedId.value > 0L) {
        val item = itemList.find { it.idBasket == isSelectedId.value }
        if (item != null) { item.isSelected = !item.isSelected }
        isSelectedId.value = 0
    }
    if (unSelected.value) {
        itemList.forEach { it.isSelected = false }
        unSelected.value = false
    }
    Box( Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))) {
        Column(modifier = modifier.fillMaxHeight()) {
            HeaderScreen(text = stringResource(R.string.baskets), modifier)
            Column(Modifier.fillMaxHeight().weight(1f)) {
                if (!bottomSheetVisible.value) Spacer(Modifier.weight(1f))
                LazyColumnBasket(itemList, onClickBasket, deleteBasket, changeNameBasket)
            }
            ButtonSwipe(refreshPosition)
        }
    }
 }

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LazyColumnBasket(
    basketList: List<Basket>,
    onClickBasket: (Long) -> Unit,
    deleteBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
){
    val listState = rememberLazyListState()
    val firstItem = remember { mutableStateOf(Pair<Int, Long>(0, 0)) }
    val showDialog = remember {mutableStateOf(false)}
    val coroutineScope = rememberCoroutineScope()
    val editBasket: MutableState<Basket?> = remember {  mutableStateOf(null) }

    if (editBasket.value != null && showDialog.value){
        EditBasketName(
            basket = editBasket.value!!,
            onDismiss = { showDialog.value = false },
            onConfirm = {
                changeNameBasket(editBasket.value!!)
                showDialog.value = false
            },
        )
    }
    if (basketList.isNotEmpty()) {
        if (firstItem.value.first != basketList[0].position ||
            firstItem.value.second != basketList[0].idBasket) {
            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
            firstItem.value = Pair(basketList[0].position, basketList[0].idBasket)
        }
    }
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver)))
    {
        items(items = basketList, key = { it.idBasket })
        { item->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if ( it == DismissValue.DismissedToStart) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            deleteBasket(item.idBasket) }, 1000)
                    } else if (it == DismissValue.DismissedToEnd) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            editBasket.value = item
                            showDialog.value = true }, 1000)
                    }
                    true
                }
            )
            if (dismissState.isDismissed(DismissDirection.EndToStart) ||
                dismissState.isDismissed(DismissDirection.StartToEnd)) {
                LaunchedEffect(Unit) { delay(300); dismissState.reset() }
            }
            SwipeToDismiss(state = dismissState,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .animateItemPlacement(),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd ||
                        direction == DismissDirection.EndToStart) 0.4f else 0.7f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Edit
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val colorIcon = when (direction) {
                        DismissDirection.StartToEnd -> Color.Green
                        DismissDirection.EndToStart -> Color.Red
                    }
                    val scale by animateFloatAsState(if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = alignment) {
                        Icon( icon, null, modifier = Modifier.scale(scale), tint = colorIcon)
                    }
                },
                dismissContent = {
                    val modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.corner_default)))
                            .fillMaxWidth()
                            .background(Color.White)
                            .clickable { onClickBasket(item.idBasket) })
                    {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = modifier
                                .padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))){
                            Text(
                                text =  SimpleDateFormat("dd-MM", Locale.getDefault()).format(item.dateB),
                                style = MaterialTheme.typography.h4,
                                color = TextDate,
                            )
                            Text(
                                text = SimpleDateFormat("yyyy", Locale.getDefault()).format(item.dateB),
                                style = MaterialTheme.typography.h4,
                                color = TextDate,
                            )
                        }
                        Text(
                            text = item.nameBasket,
                            style = MaterialTheme.typography.h1,
                            modifier = modifier.weight(1f)
                        )
                        Text(
                            text = item.quantity.toString(),
                            style = MaterialTheme.typography.h1,
                            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheetContentBasket( onAddClick: (String) -> Unit, bottomSheetHide: () -> Unit) {

    var nameNewBasket by remember { mutableStateOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date().time)) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val focusRequesterSheet = remember { FocusRequester() }

    Column(
        Modifier
            .fillMaxWidth()
            .heightIn((screenHeight * 0.35).dp, (screenHeight * 0.75).dp)
            .padding(24.dp, 24.dp, 24.dp, 32.dp)
    ) {
        Spacer(Modifier.height(1.dp))
        HeaderScreen(
            text = stringResource(R.string.add_basket),
            Modifier.focusRequester(focusRequesterSheet)
        )
        OutlinedTextField(
            value = nameNewBasket,
            singleLine = true,
            textStyle = MaterialTheme.typography.h1,
            label = { Text(text = stringResource(R.string.new_name_basket)) },
            onValueChange = { nameNewBasket = it },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddClick(nameNewBasket)
                    keyboardController?.hide()
                    nameNewBasket = ""
                    localFocusManager.clearFocus()
                    bottomSheetHide()
                }
            ),
        )
        Spacer(Modifier.height(36.dp))
        TextButtonOK(
            enabled = nameNewBasket != "",
            onConfirm = {
                onAddClick(nameNewBasket)
                bottomSheetHide()
            })
        Spacer(Modifier.height(36.dp))
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun BottomSheetContentBasketPreview(){
    BottomSheetContentBasket ({},{})
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenLayoutPreview(){
}