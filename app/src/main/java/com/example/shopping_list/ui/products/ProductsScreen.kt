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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.components.ButtonSwipe
import com.example.shopping_list.ui.components.FabAnimation
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.shopping_list.ui.components.MyTextH1
import com.example.shopping_list.ui.components.MyTextH1End
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.ui.components.dialog.EditQuantityDialog
import com.example.shopping_list.ui.components.dialog.SelectSectionDialog
import com.example.shopping_list.ui.components.selectSectionWithArticle
import com.example.shopping_list.ui.components.selectUnitWithArticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(
    basketId: Long,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>) {

    val viewModel: ProductViewModel = hiltViewModel()
    viewModel.getStateProducts(basketId)
    val uiState by viewModel.productsScreenState.collectAsState()

//    Log.d("KDS", "ProductsScreen")
    bottomSheetContent.value = {
        LayoutAddEditProduct(
            uiState = uiState,
            onAddProduct = { product -> viewModel.addProduct(product, basketId) }
        )
    }
//    Log.d("KDS", "basketId = $basketId") ProductsScreenLayout
    LayoutProductsScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        putProductInBasket = { product -> viewModel.putProductInBasket(product, basketId) },
        changeProductInBasket = { product -> viewModel.changeProductInBasket(product, basketId) },
        doChangeSectionSelected = { productList, idSection -> viewModel.changeSectionSelectedProduct(productList, idSection) },
        doDeleteSelected = { productList -> viewModel.deleteSelectedProducts(productList) },
        movePosition = { direction -> viewModel.movePositionProductInBasket(basketId, direction) },
        bottomSheetVisible = bottomSheetVisible
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutProductsScreen(
    modifier: Modifier = Modifier,
    uiState: ProductsScreenState,
    bottomSheetVisible: MutableState<Boolean>,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    doChangeSectionSelected: (List<Product>, Long) -> Unit,
    doDeleteSelected: (List<Product>) -> Unit,
    movePosition: (Int) -> Unit,
) {
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

//    Log.d("KDS", "ProductsScreenLayout")
    val itemList = uiState.products
    if (isSelectedId.value > 0L) {
        val item = itemList.find { it.idProduct == isSelectedId.value }
        if (item != null) {
            item.isSelected = !item.isSelected
        }
        isSelectedId.value = 0
    }
    if (unSelected.value) {
        itemList.forEach { it.isSelected = false }
        unSelected.value = false
    }
    if (changeSectionSelected.value) {
        SelectSectionDialog(
            listSection = uiState.sections,
            onDismiss = { changeSectionSelected.value = false },
            onConfirm = {
                if (it != 0L) doChangeSectionSelected(itemList, it)
                changeSectionSelected.value = false
            },
        )
    }
    if (deleteSelected.value) {
        doDeleteSelected(itemList)
        deleteSelected.value = false
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.screen_padding_hor))) {
        Column(modifier.fillMaxHeight()) {
            HeaderScreen(
                text = stringResource(R.string.products_in_basket) + " " + uiState.nameBasket, modifier)
            Column(Modifier.fillMaxHeight().weight(1f) ) {

                Spacer(modifier = Modifier.weight(1f, !bottomSheetVisible.value))
                LazyColumnProduct(
                    uiState = uiState,
                    putProductInBasket = putProductInBasket,
                    changeProductInBasket = changeProductInBasket,
                    doSelected = { idItem -> isSelectedId.value = idItem })
            }
            ButtonSwipe(movePosition)
        }
        if (itemList.find { it.isSelected } != null) {
            startScreen = true
            Box(Modifier.align(alignment = Alignment.BottomCenter).height(200.dp)) {
                FabAnimation(show = true, offset = 0.dp, icon = Icons.Filled.Delete, onClick = { deleteSelected.value = true })
                FabAnimation(show = true, offset = 64.dp, icon = Icons.Filled.Dns, onClick = { changeSectionSelected.value = true })
                FabAnimation(show = true, offset = 128.dp, icon = Icons.Filled.RemoveDone, onClick = { unSelected.value = true })
            }
        } else if (startScreen){
            Box(Modifier.align(alignment = Alignment.BottomCenter).height(200.dp)) {
                FabAnimation(show = false, offset = 0.dp, icon = Icons.Filled.Delete, onClick = { deleteSelected.value = true })
                FabAnimation(show = false, offset = 64.dp, icon = Icons.Filled.Dns, onClick = { changeSectionSelected.value = true })
                FabAnimation(show = false, offset = 128.dp, icon = Icons.Filled.RemoveDone, onClick = { unSelected.value = true })
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyColumnProduct(
    uiState: ProductsScreenState,
    putProductInBasket: (Product) -> Unit,
    changeProductInBasket: (Product) -> Unit,
    doSelected: (Long) -> Unit
) {
    Log.d("KDS", "LazyColumnProduct")
    val listState = rememberLazyListState()
    val showDialog = remember { mutableStateOf(false) }
    val editProduct: MutableState<Product?> = remember { mutableStateOf(null) }
    val firstItem = remember { mutableStateOf(Pair<Int, Long>(0, 0)) }
    val coroutineScope = rememberCoroutineScope()

    if (editProduct.value != null && showDialog.value) {
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
            firstItem.value.second != uiState.products[0].idProduct
        ) {
            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
            firstItem.value = Pair(uiState.products[0].position, uiState.products[0].idProduct)
        }
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items(items = uiState.products, key = { it.idProduct }) { item ->
            val dismissState = rememberDismissState(confirmStateChange = {
                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                    android.os.Handler(Looper.getMainLooper()).postDelayed({
                        putProductInBasket(item)
                    }, 1000)
                }
                true
            })
            if (dismissState.isDismissed(DismissDirection.EndToStart) || dismissState.isDismissed(
                    DismissDirection.StartToEnd
                )
            ) {
                LaunchedEffect(Unit) {
                    delay(300)
                    dismissState.reset()
                }
            }
            SwipeToDismiss(state = dismissState,
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .animateItemPlacement(),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.4f else 0.4f)
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
                        Modifier.fillMaxSize(), contentAlignment = alignment
                    ) {
                        Icon(icon, null, modifier = Modifier.scale(scale))
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
                                .clickable { doSelected(item.idProduct) }
                        )
                        MyTextH1(
                            item.article.nameArticle,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { doSelected(item.idProduct) }
                                .padding(
                                    start = dimensionResource(R.dimen.lazy_padding_hor),
                                    top = dimensionResource(R.dimen.lazy_padding_ver),
                                    bottom = dimensionResource(R.dimen.lazy_padding_ver)
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val num = if (item.value.rem(1).equals(0.0)) item.value.toInt()
                        else item.value
                        MyTextH1End(num.toString(),
                            Modifier
                                .width(70.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                .clickable {
                                    editProduct.value = item
                                    showDialog.value = true
                                })
                        Spacer(modifier = Modifier.width(4.dp))
                        MyTextH1(
                            item.article.unitA.nameUnit,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .width(50.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                .clickable { doSelected(item.idProduct) }
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

@Composable
fun LayoutAddEditProduct(
    uiState: ProductsScreenState,
    onAddProduct: (Product) -> Unit
) {
//    Log.d("KDS", "BottomSheetContentProduct")
    val nameSection = stringResource(R.string.name_section)
    val unitStuff = stringResource(R.string.name_unit1)
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, unitStuff)) }
    val focusRequesterSheet = remember { FocusRequester() }

    enterArticle.value = Pair(
        uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L,
        enterArticle.value.second)

    if (enterArticle.value.first > 0) {
        enterSection.value = selectSectionWithArticle(enterArticle.value.first, uiState.articles)
        enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
    } else  {
        enterUnit.value = Pair(
            uiState.unitA.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L,
            enterUnit.value.second)
        enterSection.value = Pair(
            uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L,
            enterSection.value.second)
    }
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
//        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen(
            text = stringResource(R.string.add_product),
            modifier = Modifier.focusRequester(focusRequesterSheet)
        )
        Spacer(Modifier.height(12.dp))
        /** Select article*/
        MyExposedDropdownMenuBox(
            listItems = uiState.articles.map { Pair(it.idArticle, it.nameArticle) },
            label = stringResource(R.string.select_product),
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true
        )

        Spacer(Modifier.height(12.dp))
        /** Select section*/
        MyExposedDropdownMenuBox(
            listItems = uiState.sections.map { Pair(it.idSection, it.nameSection) },
            label = stringResource(R.string.section),
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterSection,
            filtering = true,
            readOnly = enterArticle.value.first > 0,
            enabled = enterArticle.value.first <= 0
        )
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            /** Value*/
            MyOutlinedTextFieldWithoutIconClearing(
                typeKeyboard = "digit",
                modifier = Modifier.width(90.dp),
                enterValue = enterValue
            )
            Spacer(Modifier.width(4.dp))
            /** Select unit*/
            MyExposedDropdownMenuBox(
                listItems = uiState.unitA.map { Pair(it.idUnit, it.nameUnit) },
                label = stringResource(R.string.units),
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                readOnly = true,
                filtering = false,
                enabled = enterArticle.value.first <= 0
            )
        }
        Spacer(Modifier.height(36.dp))
        val article = ArticleEntity(
            idArticle = enterArticle.value.first,
            nameArticle = enterArticle.value.second,
            sectionId = enterSection.value.first,
            unitId = enterUnit.value.first
        )
        article.section = SectionEntity(enterSection.value.first, enterSection.value.second)
        article.unitA = UnitEntity(enterUnit.value.first, enterUnit.value.second)

        val product = ProductEntity(
            articleId = article.idArticle,
            value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble()
        )
        product.article = article
        TextButtonOK(
            enabled = enterArticle.value.second != "",
            onConfirm = {
            onAddProduct(product)
            enterArticle.value = Pair(0, "")
            enterValue.value = "1"
        })
        Spacer(Modifier.height(36.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenLayoutPreview() {

}

@Preview(showBackground = true)
@Composable
fun LayoutAddEditProductPreview() {
    LayoutAddEditProduct( ProductsScreenState()) {}
}


