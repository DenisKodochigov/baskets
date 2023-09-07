package com.example.shopping_list.ui.baskets

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.TypeText
import com.example.shopping_list.ui.components.HeaderImScreen
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.ui.components.dialog.EditBasketName
import com.example.shopping_list.ui.theme.TextDate
import com.example.shopping_list.ui.theme.styleApp
import com.example.shopping_list.utils.DismissBackground
import com.example.shopping_list.utils.log
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun BasketsScreen( onClickBasket: (Long) -> Unit, showBottomSheet: MutableState<Boolean> ) {

    val viewModel: BasketViewModel = hiltViewModel()
    viewModel.getListBasket()

    BasketScreenCreateView(
        onClickBasket = onClickBasket, viewModel = viewModel, showBottomSheet = showBottomSheet,)
}

@Composable
fun BasketScreenCreateView(
    onClickBasket: (Long) -> Unit,
    viewModel: BasketViewModel,
    showBottomSheet: MutableState<Boolean>,
){
    val uiState by viewModel.basketScreenState.collectAsState()

    if (showBottomSheet.value)
        AddBasketBottomSheet(
            onAddClick = {viewModel.addBasket(it)}, onDismiss = {showBottomSheet.value = false})
    BasketsScreenLayout(
        onClickBasket = onClickBasket,
        uiState = uiState,
        changeNameBasket = { basket -> viewModel.changeNameBasket(basket)},
        deleteBasket = { basketId -> viewModel.deleteBasket(basketId)},
    )
}

@Composable
fun BasketsScreenLayout(
    uiState: BasketScreenState,
    onClickBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
    deleteBasket: (Long) -> Unit,
){
    Column(Modifier.fillMaxHeight()) {
        BasketLazyColumn(uiState, onClickBasket, deleteBasket, changeNameBasket)
    }
 }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BasketLazyColumn(
    uiState: BasketScreenState,
    onClickBasket: (Long) -> Unit,
    deleteBasket: (Long) -> Unit,
    changeNameBasket: (Basket) -> Unit,
){

    val editBasket: MutableState<Basket?> = remember {  mutableStateOf(null) }
    val show = remember { mutableStateOf(true) }

    if (editBasket.value != null){
        EditBasketName(
            basket = editBasket.value!!,
            onDismiss = { editBasket.value = null },
            onConfirm = {
                changeNameBasket(editBasket.value!!)
                editBasket.value = null
            },
        )
    }
    LazyColumn (
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver)))
    {
        item { HeaderImScreen(text = stringResource(R.string.baskets), idImage = R.drawable.bas) }
        items(items = uiState.baskets, key = { it.idBasket })
        { item->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if ( it == DismissValue.DismissedToStart) {
                        show.value = false
                        deleteBasket(item.idBasket)
                    } else if (it == DismissValue.DismissedToEnd) { editBasket.value = item}
                      true
                }
            )
            AnimatedVisibility( visible = show.value, exit = fadeOut(spring())) {
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp).animateItemPlacement(),
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = { DismissBackground(dismissState) },
                    dismissContent = { ElementColumBasket( item, onClickBasket ) }
                )
            }
        }
    }
}

@Composable fun ElementColumBasket(basket: Basket,onClickBasket: (Long) -> Unit){
    val modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    Row(modifier = Modifier
            .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.corner_default)))
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClickBasket(basket.idBasket) },
        verticalAlignment = Alignment.CenterVertically)
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))){
            Text(
                text =  SimpleDateFormat("dd-MM", Locale.getDefault()).format(basket.dateB),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL),
                color = TextDate,
            )
            Text(
                text = SimpleDateFormat("yyyy", Locale.getDefault()).format(basket.dateB),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL),
                color = TextDate,
            )
        }
        Text(
            text = basket.nameBasket,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.weight(1f)
        )
        Text(
            text = basket.quantity.toString(),
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.lazy_padding_hor))
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddBasketBottomSheet(onAddClick: (String) -> Unit, onDismiss:() -> Unit) {

    var nameNewBasket by remember { mutableStateOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date().time)) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet( onDismissRequest = onDismiss, sheetState = sheetState ) {
        // Sheet content

        log(true, "BottomSheetContentBasket.showBottomSheet.value = ")
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn((screenHeight * 0.35).dp, (screenHeight * 0.75).dp)
                .padding(24.dp, 24.dp, 24.dp, 32.dp)
        ) {
            Spacer(Modifier.height(1.dp))
            HeaderScreen( text = stringResource(R.string.add_basket))
            OutlinedTextField(
                value = nameNewBasket,
                singleLine = true,
                textStyle = styleApp(nameStyle = TypeText.NAME_SECTION),
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
fun BottomSheetContentBasketPreview(){
    AddBasketBottomSheet ({},{})
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenLayoutPreview(){
}