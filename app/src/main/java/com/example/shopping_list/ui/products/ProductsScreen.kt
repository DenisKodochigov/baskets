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
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.shopping_list.entity.ExposedDropdownMenu
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox

@Composable
fun ProductsScreen(
    basketId: Long,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
){
    viewModel.getStateProducts(basketId)
    val uiState by viewModel.stateProductsScreen.collectAsState()

    bottomSheetContent.value = {
        BottomSheetContentProduct(
            uiState = uiState,
            onNewArticle = { viewModel.newArticle(it)},
            onAddClick = { articleId, value->
                viewModel.newProduct( ProductEntity(
                    basketId = basketId,
                    articleId = articleId,
                    article = ArticleEntity( idArticle = articleId ),
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
        LazyColumn (
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 24.dp))
        {
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
    uiState: StateProductsScreen,
    onAddClick: (Long, Double) -> Unit,
    onNewArticle: (Pair<Long,String>) -> Unit){

    val screenHeight = LocalConfiguration.current.screenHeightDp

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .heightIn((screenHeight * 0.25).dp, (screenHeight * 0.75).dp)) {
        HeaderScreen(text = "Select product")
        Spacer(Modifier.height(36.dp))
        MyExposedDropdownMenuBox(
            listItems = uiState.articles.map{ Pair(it.idArticle, it.nameArticle) },
            label = "Select product",
            modifier = Modifier.fillMaxWidth(),
            onSelectItem = onNewArticle )
        Spacer(Modifier.height(16.dp))
        Row() {
            MyExposedDropdownMenuBox(
                listItems = uiState.group.map{ Pair(it.idGroup, it.nameGroup) },
                label = "group",
                modifier = Modifier.width(150.dp),
                onSelectItem = onNewArticle )
            MyExposedDropdownMenuBox(
                listItems = emptyList(),
                label = "",
                modifier = Modifier.width(100.dp),
                onSelectItem = onNewArticle )
            MyExposedDropdownMenuBox(
                listItems = uiState.unitA.map{ Pair(it.idUnit, it.nameUnit) },
                label = "unit",
                modifier = Modifier.width(120.dp),
                onSelectItem = onNewArticle )
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentProductPreview(){
    BottomSheetContentProduct(StateProductsScreen(),{ l: Long, d: Double -> } ,{})
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}