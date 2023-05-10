package com.example.shopping_list.ui.products

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.*
import kotlinx.coroutines.delay

@Composable
fun ProductsScreen(
    basketId: Long,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit,
){
    viewModel.getStateProducts(basketId)
    val uiState by viewModel.stateProductsScreen.collectAsState()

    Log.d("KDS", "ProductsScreen")
    bottomSheetContent.value = {
        BottomSheetContentProduct(
            uiState = uiState,
            bottomSheetHide = bottomSheetHide,
            onAddProduct = { product-> viewModel.addProduct( product, basketId )}
        )
    }
//    Log.d("KDS", "basketId = $basketId") ProductsScreenLayout
    ProductsScreenLayout(
        modifier = Modifier.padding(bottom =  dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState ,
        putProductInBasket = {product-> viewModel.putProductInBasket( product, basketId )},
        changeProductInBasket = {product-> viewModel.changeProductInBasket( product, basketId )},
        doChangeGroupSelected = {
                productList, idGroup -> viewModel.changeGroupSelected(productList,idGroup)},
        doDeleteSelected = {productList -> viewModel.deleteSelected(productList)},
        refreshPosition = {
                productList, direction -> viewModel.setPositionProductInBasket(productList, direction)}
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun ProductsScreenLayout(
    modifier: Modifier = Modifier,
    uiState: StateProductsScreen,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    doChangeGroupSelected: (MutableList<Product>, Long) -> Unit,
    doDeleteSelected: (MutableList<Product>) -> Unit,
    refreshPosition: (MutableList<Product>, Int) -> Unit,
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
        val idGroup =0L
        doChangeGroupSelected(itemList,idGroup)
        changeGroupSelected.value = false
    }
    if (deleteSelected.value) {
        doDeleteSelected(itemList)
        deleteSelected.value = false
    }

    Box( Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))){
        Column( modifier.fillMaxHeight()) {
            HeaderScreen(text = "Products", modifier)
            Spacer(Modifier.weight(1f) )
            LazyColumnProduct(modifier, itemList, uiState.unitA, putProductInBasket,
                changeProductInBasket, isSelectedId)
            ButtonSwipeProduct(itemList, refreshPosition)
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyColumnProduct(
    modifier: Modifier = Modifier,
    productList: MutableList<Product>,
    unitList: List<UnitA>,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    isSelected: MutableState<Long>)
{
    Log.d("KDS", "LazyColumnProduct")
    val listState = rememberLazyListState()
    val showDialog = remember {mutableStateOf(false)}
    val editProduct: MutableState<Product?> = remember {  mutableStateOf(null) }

    if (editProduct.value != null && showDialog.value){
        EditQuantityDialog(
            product = editProduct.value!!,
            listUnit = unitList,
            showDialog = showDialog.value,
            onDismiss = { showDialog.value = false },
            onConfirm = {
                changeProductInBasket(editProduct.value!!)
                showDialog.value = false
            },
            modifier = Modifier
        )
    }
    productList.sortWith(compareBy ({ !it.putInBasket }, { it.position }))

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
                },
                dismissContent = {
                    Box {
                        Row(modifier = Modifier
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
                            Text(
                                text = item.article.nameArticle,
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = dimensionResource(R.dimen.lazy_padding_hor),
                                        top = dimensionResource(R.dimen.lazy_padding_ver),
                                        bottom = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable { isSelected.value = item.idProduct }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.value.toString(),
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .width(50.dp)
                                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable {
                                        editProduct.value = item
                                        showDialog.value = true
                                    }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.article.unitA?.nameUnit ?: "",
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable { isSelected.value = item.idProduct }
                            )
                        }
                        if (item.putInBasket) Divider(
                            color = MaterialTheme.colors.onSurface,
                            thickness = 1.dp,
                            modifier = Modifier
                                .padding(top = 28.dp, start = 8.dp, end = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun BottomSheetContentProduct(
    uiState: StateProductsScreen,
    bottomSheetHide: () -> Unit,
    onAddProduct: (Product) -> Unit)
{
    Log.d("KDS", "BottomSheetContentProduct")
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember{ mutableStateOf("1")}
    val enterArticle = remember{ mutableStateOf(Pair<Long,String>(0,""))}
    val enterGroup = remember{ mutableStateOf(Pair<Long,String>(1,"All"))}
    val enterUnit = remember{ mutableStateOf(Pair<Long,String>(1,"шт"))}
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
//        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen(text = "Add product", Modifier.focusRequester(focusRequesterSheet))
        Spacer(Modifier.height(24.dp))
        MyExposedDropdownMenuBox(/** Select article*/
            listItems = uiState.articles.map{ Pair(it.idArticle, it.nameArticle) },
            label = "Select product",
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true )
        if (enterArticle.value.first > 0) {
            enterGroup.value = selectGroupWithArticle(enterArticle.value.first, uiState.articles)
            enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
            enterValue.value = "1"
        }
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
                modifier = Modifier.background(Color.LightGray)
                    .align(Alignment.CenterVertically)
                    .width(200.dp)
                    .padding(top = 4.dp),
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
                    value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble(),
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

@Composable
fun selectGroupWithArticle (id: Long, listArticle: List<Article>): Pair<Long, String>{
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) {
        if (article.group != null) {
            Pair(article.group!!.idGroup, article.group!!.nameGroup)
        } else Pair(0L,"")
    } else Pair(0L,"")
}

@Composable
fun selectUnitWithArticle (id: Long, listArticle: List<Article>): Pair<Long, String>{
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) {
        if (article.unitA != null) {
            Pair(article.unitA!!.idUnit, article.unitA!!.nameUnit)
        } else Pair(0L,"")
    } else Pair(0L,"")
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
    BottomSheetContentProduct(
        StateProductsScreen(),{}, {})
}

@Preview(showBackground = true)
@Composable
fun BasketsScreenPreview(){
//    BasketsScreen()
}

