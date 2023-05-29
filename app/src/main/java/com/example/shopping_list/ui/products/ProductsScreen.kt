package com.example.shopping_list.ui.products

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.components.ButtonSwipe
import com.example.shopping_list.ui.components.FabChangeGroupProducts
import com.example.shopping_list.ui.components.FabDeleteProducts
import com.example.shopping_list.ui.components.FabUnSelectProducts
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.LayoutAddEditProduct
import com.example.shopping_list.ui.components.MyTextH1
import com.example.shopping_list.ui.components.dialog.EditQuantityDialog
import com.example.shopping_list.ui.components.dialog.SelectGroupDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(
    basketId: Long,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit,
){
    val viewModel: ProductViewModel = hiltViewModel()
    viewModel.getStateProducts(basketId)
    val uiState by viewModel.stateProductsScreen.collectAsState()

    Log.d("KDS", "ProductsScreen")
    bottomSheetContent.value = {
        LayoutAddEditProduct(
            uiState = uiState,
            bottomSheetHide = bottomSheetHide,
            onAddProduct = { product-> viewModel.addProduct( product, basketId )}
        )
    }
//    Log.d("KDS", "basketId = $basketId") ProductsScreenLayout
    LayoutProductsScreen(
        modifier = Modifier.padding(bottom =  dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        putProductInBasket = { product->
            viewModel.putProductInBasket( product, basketId )},
        changeProductInBasket = { product->
            viewModel.changeProductInBasket( product, basketId )},
        doChangeGroupSelected = { productList, idGroup ->
            viewModel.changeGroupSelectedProduct(productList,idGroup)},
        doDeleteSelected = {productList ->
            viewModel.deleteSelectedProducts(productList)},
        movePosition = { direction ->
            viewModel.movePositionProductInBasket( basketId, direction )}
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutProductsScreen(
    modifier: Modifier = Modifier,
    uiState: StateProductsScreen,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    doChangeGroupSelected: (List<Product>, Long) -> Unit,
    doDeleteSelected: (List<Product>) -> Unit,
    movePosition: (Int) -> Unit,
){
    val isSelectedId: MutableState<Long> = remember {  mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val changeGroupSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }

    Log.d("KDS", "ProductsScreenLayout")
    val itemList = uiState.products
    if (isSelectedId.value > 0L) {
        val item = itemList.find { it.idProduct == isSelectedId.value }
        if (item != null) { item.isSelected = !item.isSelected }
        isSelectedId.value = 0
    }
    if (unSelected.value) {
       itemList.forEach { it.isSelected = false }
       unSelected.value = false
    }
    if (changeGroupSelected.value) {
        SelectGroupDialog(
            listGroup = uiState.group,
            onDismiss = {changeGroupSelected.value = false },
            onConfirm = {
                if (it != 0L) doChangeGroupSelected( itemList, it)
                changeGroupSelected.value = false },)
    }
    if (deleteSelected.value) {
        doDeleteSelected(itemList)
        deleteSelected.value = false
    }

    Box( Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))){
        Column( modifier.fillMaxHeight()) {
            HeaderScreen(text = "Products in basket: ${uiState.nameBasket} ", modifier)
            Column(Modifier.fillMaxHeight().weight(1f)) {
                Spacer(modifier = Modifier.weight(1f))
                LazyColumnProduct(
                    uiState = uiState,
                    putProductInBasket = putProductInBasket,
                    changeProductInBasket = changeProductInBasket,
                    isSelected = isSelectedId)
            }
            ButtonSwipe(movePosition)
        }
        if ( itemList.find { it.isSelected } != null) {
            Column(Modifier.align(alignment = Alignment.BottomCenter)) {
                FabDeleteProducts( deleteSelected )
                FabChangeGroupProducts( changeGroupSelected )
                FabUnSelectProducts ( unSelected )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyColumnProduct(
    uiState: StateProductsScreen,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    isSelected: MutableState<Long>)
{
    Log.d("KDS", "LazyColumnProduct")
    val listState = rememberLazyListState()
    val showDialog = remember {mutableStateOf(false)}
    val editProduct: MutableState<Product?> = remember {  mutableStateOf(null) }
    val firstItem = remember { mutableStateOf(Pair<Int, Long>(0, 0)) }
    val coroutineScope = rememberCoroutineScope()

    if (editProduct.value != null && showDialog.value){
        EditQuantityDialog(
            product = editProduct.value!!,
            listUnit = uiState.unitA,
            onDismiss = { showDialog.value = false },
            onConfirm = {
                changeProductInBasket(editProduct.value!!)
                showDialog.value = false
            }
        )
    }
    if (uiState.products.isNotEmpty()) {
        if (firstItem.value.first != uiState.products[0].position ||
            firstItem.value.second != uiState.products[0].idProduct) {
            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
            firstItem.value = Pair(uiState.products[0].position, uiState.products[0].idProduct)
        }
    }
//    productList.sortWith(compareBy ({ !it.putInBasket }, { it.position }))
    val productList = uiState.products.filter { !it.putInBasket }.sortedBy { it.position }
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items( items = productList, key = { it.idProduct }) {
        item ->
            val dismissState = rememberDismissState( confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            putProductInBasket(item) }, 1000) }
                    true })
            if (dismissState.isDismissed(DismissDirection.EndToStart) || dismissState.isDismissed(DismissDirection.StartToEnd)) {
                LaunchedEffect(Unit) {
                    delay(300)
                    dismissState.reset() }
            }
            SwipeToDismiss(state = dismissState,
                modifier = Modifier.padding(vertical = 1.dp).animateItemPlacement(),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.1f else 0.4f)
                },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.AddShoppingCart
                        DismissDirection.EndToStart -> Icons.Default.AddShoppingCart
                    }
                    val scale by animateFloatAsState(if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = alignment) {
                        Icon( icon, null, modifier = Modifier.scale(scale) )
                    }
                }
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(6.dp))
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .background(if (item.isSelected) Color.Red else Color.LightGray)
                                .width(8.dp)
                                .height(32.dp)
                                .align(Alignment.CenterVertically)
                                .clickable { isSelected.value = item.idProduct }
                        )
                        MyTextH1( item.article.nameArticle, Modifier.weight(1f)
                                .clickable {isSelected.value = item.idProduct }
                                .padding(
                                    start = dimensionResource(R.dimen.lazy_padding_hor),
                                    top = dimensionResource(R.dimen.lazy_padding_ver),
                                    bottom = dimensionResource(R.dimen.lazy_padding_ver))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val num = if (item.value.rem(1).equals(0.0)) item.value.toInt()
                        else item.value
                        MyTextH1( num.toString(), Modifier.width(70.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                .clickable {
                                    editProduct.value = item
                                    showDialog.value = true
                                })
                        Spacer(modifier = Modifier.width(4.dp))
                        MyTextH1(
                            item.article.unitA.nameUnit, Modifier.width(40.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                .clickable { isSelected.value = item.idProduct }
                        )
                    }
                    if (item.putInBasket) Divider(
                        color = MaterialTheme.colors.onSurface,
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(top = 36.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenLayoutPreview(){
//    ProductsScreenLayout(
//        Modifier,
//        uiState = StateProductsScreen( mutableListOf(
//        ProductEntity(
//            idProduct = 1, basketId = 1, value = 1.0, putInBasket = false,
//            article = ArticleEntity(
//                idArticle = 1, nameArticle = "Milk",
//                group = GroupEntity(idGroup = 1, nameGroup = "All") as GroupArticle,
//                unitA = UnitEntity(idUnit = 1, nameUnit = "in.") as UnitA
//            )))),
//        putProductInBasket = {},
//        changeProductInBasket = { },
//        doDeleteSelected = {}
//    )
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentProductPreview(){
    LayoutAddEditProduct(
        StateProductsScreen(),{}, {})
}

@Preview(showBackground = true)
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}

