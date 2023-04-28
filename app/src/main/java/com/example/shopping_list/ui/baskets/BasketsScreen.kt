package com.example.shopping_list.ui.baskets

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.HeaderScreen1

@Composable
fun BasketsScreen(
    onBasketClick: (Long) -> Unit,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>) {

    viewModel.getListBasket()
    val uiState by viewModel.stateBasketScreen.collectAsState()
    bottomSheetContent.value = { BottomSheetContentBasket(onAddClick = {viewModel.newBasket(it)}) }

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
    onBasketClick: (Long) -> Unit,
    onHeaderClick: () -> Unit,
){
//    Log.d("KDS", "New state BasketsScreenLayout, ${itemList.size}")
    val listState = rememberLazyListState()
    Column( modifier ){
        HeaderScreen1(text = "Baskets", onHeaderClick)
        LazyColumn (state = listState, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)) {
            items(items = itemList){ item->
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
                    .background(Color.White)
                    .clickable { onBasketClick(item.idBasket) }){
                    Text(
                        text = item.nameBasket,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
 }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheetContentBasket(onAddClick: (String) -> Unit){

    var nameNewBasket by remember { mutableStateOf("")}
    val pb = 0.dp
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    Column(
        Modifier.fillMaxWidth()
            .heightIn((screenHeight * 0.25).dp, (screenHeight * 0.75).dp)
            .padding(24.dp, 24.dp, 24.dp, 32.dp)) {
        OutlinedTextField(
            value = nameNewBasket,
            singleLine = true,
            textStyle = TextStyle(fontSize =  20.sp),
            label = { Text(text = "New name basket") },
            onValueChange = { nameNewBasket = it},
            modifier = Modifier
                .padding(start = pb, top = pb, end = pb, bottom = pb)
                .fillMaxWidth(),
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