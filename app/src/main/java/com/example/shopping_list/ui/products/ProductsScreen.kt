package com.example.shopping_list.ui.products

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.BasketsRow
import com.example.shopping_list.ui.components.HeaderScreen

@Composable
fun ProductsScreen(
    basketId: Int,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
){
    viewModel.getListProducts(basketId)
    val uiState by viewModel.stateProductsScreen.collectAsState()
    bottomSheetContent.value = { BottomSheetContentProduct(onAddClick = {viewModel.addProduct(it)}) }
    Log.d("KDS", "basketId = $basketId")
    ProductsScreenLayout(
        modifier = Modifier.semantics { contentDescription = "Baskets Screen" },
        itemList = uiState.products,
    )
}

@Composable
fun ProductsScreenLayout(
    modifier: Modifier = Modifier,
    itemList: List<Product>,
){
    val listState = rememberLazyListState()
    Column {
        HeaderScreen(text = "Products")
        LazyColumn (state = listState, modifier = Modifier.fillMaxSize()) {
            items(items = itemList){ item->
                Row ( modifier = Modifier.clickable {
//                    onProductClick(item.idBasket)
                }){
                    Text(text = item.nameProduct)
                }
            }
        }
        itemList.forEach{ item ->
            BasketsRow(modifier = modifier, name = item.nameProduct)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheetContentProduct(onAddClick: (String) -> Unit){

    var nameNewProduct by remember { mutableStateOf("") }
    val pb = 0.dp
    val keyboardController = LocalSoftwareKeyboardController.current

    Column( Modifier.padding(16.dp).fillMaxWidth(1f)) {
        OutlinedTextField(
            value = nameNewProduct,
            singleLine = true,
            textStyle = TextStyle(fontSize =  20.sp),
            label = { Text(text = "New name product") },
            onValueChange = { nameNewProduct = it},
            modifier = Modifier
                .padding(start = pb, top = pb, end = pb, bottom = pb )
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onAddClick(nameNewProduct)
                keyboardController?.hide()
                nameNewProduct = ""
            }) ,
        )
        Spacer(Modifier.width(36.dp))
    }
}
@Preview
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}