package com.example.shopping_list.ui.baskets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.BasketsRow
import com.example.shopping_list.ui.components.HeaderScreen

@Composable
fun BasketsScreen(
    onBasketClick: (String) -> Unit = {},
    viewModel: AppViewModel = viewModel()) {

/** Flow new state screen  */
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
    BasketsScreen()
}
