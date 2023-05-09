package com.example.shopping_list.ui.baskets

import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.HeaderScreen
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import com.example.shopping_list.R
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.components.ButtonSwipeBacket
import com.example.shopping_list.ui.components.EditBasketName
import com.example.shopping_list.ui.components.EditQuantityDialog
import kotlinx.coroutines.delay

@Composable
fun BasketsScreen(
    onClickBasket: (Long) -> Unit,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>) {

    viewModel.getListBasket()
    val uiState by viewModel.stateBasketScreen.collectAsState()
    bottomSheetContent.value = { BottomSheetContentBasket(onAddClick = {viewModel.newBasket(it)}) }

    BasketsScreenLayout(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        onClickBasket = onClickBasket,
        itemList = uiState.baskets,
        changeNameBasket = { basket -> viewModel.changeNameBasket(basket)},
        deleteBasket = { basketId -> viewModel.deleteBasket(basketId)},
        refreshPosition = {
                basketList, direction -> viewModel.setPositionBasket(basketList, direction)}
    )
}

@Composable
fun BasketsScreenLayout(
    modifier: Modifier = Modifier,
    itemList: MutableList<Basket>,
    onClickBasket: (Long) -> Unit,
    refreshPosition: (MutableList<Basket>, Int) -> Unit,
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
    Log.d("KDS", "BasketsScreenLayout")
    Column( modifier = modifier.fillMaxHeight()
        .padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))){
        HeaderScreen(text = "Baskets", modifier)
        Spacer( Modifier.weight(1f))
        LazyColumnBasket( itemList, onClickBasket, deleteBasket, changeNameBasket)
        ButtonSwipeBacket(itemList, refreshPosition)
    }
 }

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyColumnBasket(
    basketList: MutableList<Basket>,
    onClickBasket: (Long) -> Unit,
    deleteBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
){
    val listState = rememberLazyListState()
    val showDialog = remember {mutableStateOf(false)}
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
                modifier = Modifier.padding(vertical = 1.dp).animateItemPlacement(),
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
                    val modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor),
                            vertical = dimensionResource(R.dimen.lazy_padding_ver))
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.corner_default)))
                            .fillMaxWidth()
                            .background(Color.White)
                            .clickable { onClickBasket(item.idBasket) })
                    {
                        Text(
                            text = item.nameBasket,
                            style = MaterialTheme.typography.h1,
                            modifier = modifier.weight(1f)
                        )
                        Text(
                            text = item.quantity.toString(),
                            style = MaterialTheme.typography.h1,
                            modifier = modifier
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheetContentBasket(onAddClick: (String) -> Unit){

    var nameNewBasket by remember { mutableStateOf("")}
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Column(
        Modifier.fillMaxWidth()
            .heightIn((screenHeight * 0.35).dp, (screenHeight * 0.75).dp)
            .padding(24.dp, 24.dp, 24.dp, 32.dp)) {
        OutlinedTextField(
            value = nameNewBasket,
            singleLine = true,
            textStyle = MaterialTheme.typography.h1,
            label = { Text(text = "New name basket") },
            onValueChange = { nameNewBasket = it},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAddClick(nameNewBasket)
                    keyboardController?.hide()
                    nameNewBasket = ""
                    localFocusManager.clearFocus()
                }
            ) ,
        )
        Spacer(Modifier.width(36.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentBasketPreview(){
    BottomSheetContentBasket {}
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenLayoutPreview(){
//    BasketsScreenLayout(Modifier, listOf(BasketDB(nameBasket = "Fruicts"), BasketDB(nameBasket = "Auto"))) {}
}