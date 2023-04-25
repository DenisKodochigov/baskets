package com.example.shopping_list.ui.products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.HeaderScreen

@Composable
fun ProductsScreen(
    basketId: Long,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
){
    viewModel.getListProducts(basketId)
    viewModel.getListArticle()
    val uiState by viewModel.stateProductsScreen.collectAsState()

    bottomSheetContent.value = {
        BottomSheetContentProduct(
            itemList = uiState.articles,
            onNewArticle = { viewModel.newArticle(it)},
            onAddClick = { articleId, value->
                viewModel.newProduct(ProductEntity(
                    basketId = basketId,
                    articleId = articleId,
                    article = ArticleEntity(idArticle = articleId),
                    value = value
            ))}
        )
    }
//    Log.d("KDS", "basketId = $basketId")
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
    Log.d("KDS", "ProductsScreenLayout ${itemList.size}")
    Column( modifier ) {
        HeaderScreen(text = "Products")
        LazyColumn (state = listState, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)) {
            items(items = itemList){ item->
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
                    .background(Color.White)
                    .clickable {
//                    onProductClick(item.idBasket)
                    }){
                    Text(text = item.article.nameArticle,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun BottomSheetContentProduct(
    itemList: List<Article>,
    onAddClick: (Long, Double) -> Unit,
    onNewArticle: (String) -> Unit){

    Log.d("KDS", "BottomSheetContentProduct ${itemList.size}")

    Column(
        Modifier
            .padding(16.dp)
            .fillMaxWidth(1f)) {
        HeaderScreen(text = "Select product")
        ListArticle(itemList, onAddClick)
        EditTextNewArticle(onNewArticle)
        Spacer(Modifier.width(36.dp))
    }
}

@Composable
fun ListArticle(itemList: List<Article>, onAddClick: (Long, Double) -> Unit){
    val listState = rememberLazyListState()
    LazyColumn (state = listState, modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)) {
        items(items = itemList){ item->
            Row ( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
                .background(Color.White)
                .clickable {
                    //Показать диалог с выбором значения
                    onAddClick(item.idArticle, 0.0)
                }){
                Text(text = item.nameArticle,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTextNewArticle(onNewArticle: (String) -> Unit){

    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    var nameNewArticle by remember { mutableStateOf("") }
    val pb = 0.dp
    val modifier = Modifier.padding(start = pb, top = pb, end = pb, bottom = pb).fillMaxWidth()
    OutlinedTextField(
        value = nameNewArticle,
        singleLine = true,
        textStyle = TextStyle(fontSize =  20.sp),
        label = { Text(text = "New article") },
        onValueChange = { nameNewArticle = it},
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onNewArticle(nameNewArticle)
                keyboardController?.hide()
                localFocusManager.clearFocus()
                nameNewArticle = ""
            }
        ) ,
        leadingIcon = { nameNewArticle = onAddIconEditText(onNewArticle,nameNewArticle) },
        trailingIcon = { nameNewArticle = onAddIconEditText(onNewArticle,nameNewArticle) }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun onAddIconEditText(onNewArticle: (String) -> Unit, nameNewArticle: String): String {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    Icon(
        Icons.Filled.Add,
        contentDescription = "Add",
        Modifier.clickable(
            onClick = {
                keyboardController?.hide()
                onNewArticle(nameNewArticle)
                localFocusManager.clearFocus() }
        )
    )
    return ""
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentProductPreview(){
    BottomSheetContentProduct(emptyList(),{ l: Long, d: Double -> } ,{})
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}