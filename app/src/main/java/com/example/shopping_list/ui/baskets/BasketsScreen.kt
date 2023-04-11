package com.example.shopping_list.ui.baskets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.BasketsRow
import com.example.shopping_list.ui.components.HeaderScreen

@Composable
//fun BasketsScreen(onBasketClick: (String) -> Unit = {}, viewModel: AppViewModel ) {
fun BasketsScreen(onBasketClick: (String) -> Unit = {} ) {
/** Flow new state screen  */
    val viewModel = hiltViewModel<AppViewModel>()
    val uiState by viewModel.stateBasketScreen.collectAsState()

    BasketsScreenLayout(
        modifier = Modifier.semantics { contentDescription = "Baskets Screen" },
        onBasketClick = onBasketClick,
/** Send to screen new value  */
        itemList = uiState.baskets,
    )
}

@Composable
 fun BasketsScreenLayout(
    modifier: Modifier = Modifier,
    itemList: List<BasketDB>,
    onBasketClick: (String) -> Unit,
 ){
    Column( ){
        HeaderScreen(text = "Baskets")
//        var name by remember { mutableStateOf("") }

        LazyColumn {
            items(items = itemList){ item->
                Row ( modifier = Modifier.clickable { onBasketClick(item.idBasket.toString()) }){
                    Text(text = item.basketName.toString())
                }
            }
        }
        itemList.forEach{ item ->
            BasketsRow(modifier = modifier, name = item.basketName ?: "")
        }
    }
    HeaderScreen(text = "Baskets")
 }

@Preview
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}
