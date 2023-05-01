package com.example.shopping_list.ui.products

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.ButtonMy
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon

@Composable
fun ProductsScreen(
    basketId: Long,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit,
){
    viewModel.getStateProducts(basketId)
    val uiState by viewModel.stateProductsScreen.collectAsState()

    bottomSheetContent.value = {
        BottomSheetContentProduct(
            uiState = uiState,
            bottomSheetHide = bottomSheetHide,
            onAddProduct = { product-> viewModel.addProduct( product, basketId )}
        )
    }
//    Log.d("KDS", "basketId = $basketId") ProductsScreenLayout
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
//    Log.d("KDS", "ProductsScreenLayout ${itemList.size}")
    Column( modifier ) {
        HeaderScreen(text = "Products", Modifier)
        LazyColumn (
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 24.dp))
        {
            items(items = itemList){ item->
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color.White)
                    .clickable {
//                    onProductClick(item.idBasket)
                    }){
                    Text(text = item.article.nameArticle,
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f).padding(start = 12.dp, top = 12.dp, bottom = 12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.value.toString(),
                        fontSize = 20.sp,
                        modifier = Modifier.width(40.dp).padding(vertical = 12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.article.unitA?.nameUnit ?: "",
                        fontSize = 20.sp,
                        modifier = Modifier.width(40.dp).padding(vertical = 12.dp))
                }
            }
        }
    }
}

@Composable
fun BottomSheetContentProduct(
    uiState: StateProductsScreen,
    bottomSheetHide: () -> Unit,
    onAddProduct: (Product) -> Unit){

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember{ mutableStateOf(0.0)}
    val enterArticle = remember{ mutableStateOf(Pair<Long,String>(0,""))}
    val enterGroup = remember{ mutableStateOf(Pair<Long,String>(0,""))}
    val enterUnit = remember{ mutableStateOf(Pair<Long,String>(0,""))}
    val focusRequesterSheet = remember { FocusRequester() }

    if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
        val id:Long = uiState.unitA.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
        enterUnit.value = Pair(id, enterUnit.value.second )
    }
    if (enterGroup.value.first == 0L && enterGroup.value.second != "") {
        val id:Long = uiState.group.find { it.nameGroup == enterGroup.value.second }?.idGroup ?: 0L
        enterGroup.value = Pair(id, enterGroup.value.second )
    }
    if (enterArticle.value.first == 0L && enterArticle.value.second != "") {
        val id:Long = uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
        enterArticle.value = Pair(id, enterArticle.value.second )
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.75).dp)) {
        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen(text = "Add product", Modifier.focusRequester(focusRequesterSheet))
        Spacer(Modifier.height(24.dp))
        MyExposedDropdownMenuBox(/** Select article*/
            listItems = uiState.articles.map{ Pair(it.idArticle, it.nameArticle) },
            label = "Select product",
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true)
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            MyExposedDropdownMenuBox(/** Select group*/
                listItems = uiState.group.map{ Pair(it.idGroup, it.nameGroup) },
                label = "Group",
                modifier = Modifier.weight(1f),
                enterValue = enterGroup,
                filtering = true)
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextFieldWithoutIcon( /** Value*/
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(80.dp)
                    .padding(top = 8.dp),
                enterValue = enterValue)
            Spacer(Modifier.width(4.dp))
            MyExposedDropdownMenuBox(/** Select unit*/
                listItems = uiState.unitA.map{ Pair(it.idUnit, it.nameUnit) },
                label = "Unit",
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false)
        }
        Spacer(Modifier.height(36.dp))
        Row(Modifier.fillMaxWidth()) {
            ButtonMy(Modifier.weight(1f),"Add") {
                onAddProduct(
                    ProductEntity(
                    value = enterValue.value,
                        article = ArticleEntity(
                            idArticle = enterArticle.value.first,
                            nameArticle = enterArticle.value.second,
                            group = GroupEntity(
                                idGroup = if (enterGroup.value.first != 0L) enterGroup.value.first else 1,
                                nameGroup = enterGroup.value.second
                            ),
                            unitA = UnitEntity(
                                idUnit = enterUnit.value.first,
                                nameUnit = enterUnit.value.second
                            )
                        )
                    )
                )
                enterArticle.value = Pair(0,"")
            }
            Spacer(Modifier.width(12.dp))
            ButtonMy(Modifier.weight(1f), "Cancel"){
                enterArticle.value = Pair(0,"")
                bottomSheetHide() }
        }
        Spacer(Modifier.height(72.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenLayoutPreview(){
    ProductsScreenLayout( Modifier, emptyList())
}
@Preview(showBackground = true)
@Composable
fun BottomSheetContentProductPreview(){
    BottomSheetContentProduct(
        StateProductsScreen(),{}, {})
}
@Preview(showBackground = true)
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}