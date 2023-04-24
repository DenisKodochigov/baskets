package com.example.shopping_list.ui.baskets

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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.BasketsRow
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.HeaderScreen1

@Composable
fun BasketsScreen(
    onBasketClick: (Int) -> Unit,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>) {

    viewModel.getListBasket()
    val uiState by viewModel.stateBasketScreen.collectAsState()
    bottomSheetContent.value = { BottomSheetContentBasket(onAddClick = {viewModel.addBasket(it)}) }

    BasketsScreenLayout(
        modifier = Modifier.semantics { contentDescription = "Baskets Screen" },
        onBasketClick = onBasketClick,
        itemList = uiState.baskets,
        onHeaderClick = {viewModel.getListBasket()}
    )
}

@Composable
fun BasketsScreenLayout(
    modifier: Modifier = Modifier,
    itemList: List<Basket>,
    onBasketClick: (Int) -> Unit,
    onHeaderClick: () -> Unit,
){
    val listState = rememberLazyListState()
    Column( ){
        HeaderScreen1(text = "Baskets", onHeaderClick)
        LazyColumn (state = listState, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)) {
            items(items = itemList){ item->
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
                    .background(Color.White)
                    .clickable { /*onBasketClick(item.idBasket)*/ }){
                    Text(
                        text = item.nameBasket,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
        itemList.forEach{ item ->
            BasketsRow(modifier = modifier, name = item.nameBasket)
        }
    }
 }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheetContentBasket(onAddClick: (String) -> Unit){

    var nameNewBasket by remember { mutableStateOf("")}
    val pb = 0.dp
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(1f)) {
        OutlinedTextField(
            value = nameNewBasket,
            singleLine = true,
            textStyle = TextStyle(fontSize =  20.sp),
            label = { Text(text = "New name basket") },
            onValueChange = { nameNewBasket = it},
            modifier = Modifier
                .padding(start = pb, top = pb, end = pb, bottom = pb)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onAddClick(nameNewBasket)
                keyboardController?.hide()
                nameNewBasket = ""
            }) ,
        )
        Spacer(Modifier.width(36.dp))
    }
}

@Preview
@Composable
fun BottomSheetContentBasketPreview(){
    BottomSheetContentBasket {}
}
@Preview
@Composable
fun BasketsScreenLayoutPreview(){
//    BasketsScreenLayout(Modifier, listOf(BasketDB(nameBasket = "Fruicts"), BasketDB(nameBasket = "Auto"))) {}
}