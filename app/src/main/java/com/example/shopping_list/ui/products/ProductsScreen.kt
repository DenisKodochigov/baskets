package com.example.shopping_list.ui.products

import android.annotation.SuppressLint
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.shopping_list.data.room.tables.ArticleDB
import com.example.shopping_list.data.room.tables.ProductDB
import com.example.shopping_list.data.room.tables.SectionDB
import com.example.shopping_list.data.room.tables.UnitDB
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.components.HeaderImScreen
import com.example.shopping_list.ui.components.HeaderScreen
import com.example.shopping_list.ui.components.HeaderSection
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.shopping_list.ui.components.MyTextH1
import com.example.shopping_list.ui.components.MyTextH1End
import com.example.shopping_list.ui.components.showFABs
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.ui.components.dialog.EditQuantityDialog
import com.example.shopping_list.ui.components.dialog.SelectSectionDialog
import com.example.shopping_list.ui.components.selectSectionWithArticle
import com.example.shopping_list.ui.components.selectUnitWithArticle
import com.example.shopping_list.ui.theme.SectionColor
import com.example.shopping_list.utils.createDoubleListProduct
import com.example.shopping_list.utils.log

const val showLog = false
@Composable
fun ProductsScreen(
    basketId: Long,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>) {

    val viewModel: ProductViewModel = hiltViewModel()
    viewModel.getStateProducts(basketId)

    ProductScreenCreateView(
        basketId = basketId,
        viewModel = viewModel,
        bottomSheetVisible = bottomSheetVisible,
        bottomSheetContent = bottomSheetContent
    )
}

@Composable
fun ProductScreenCreateView(
    basketId: Long,
    viewModel: ProductViewModel,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
){
    val uiState by viewModel.productsScreenState.collectAsState()
    log( showLog,"ProductScreenCreateView. ${uiState.products.size}")
    bottomSheetContent.value = {
        LayoutAddEditProduct(
            uiState = uiState,
            onAddProduct = { product: Product -> viewModel.addProduct(product, basketId) })
    }
    LayoutProductsScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        putProductInBasket = { product -> viewModel.putProductInBasket(product, basketId) },
        changeProduct = { product -> viewModel.changeProduct(product, basketId) },
        doChangeSectionSelected = { productList, idSection ->
            viewModel.changeSectionSelected(productList, idSection) },
        doDeleteSelected = { productList -> viewModel.deleteSelectedProducts(productList) },
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
    changeProduct: (Product) -> Unit,
    doChangeSectionSelected: (List<Product>, Long) -> Unit,
    doDeleteSelected: (List<Product>) -> Unit,
) {
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

    log( showLog,"LayoutProductsScreen. ${uiState.products.size}")
    val itemList = uiState.products

    if (isSelectedId.value > 0L) {
        itemList.find { it.idProduct == isSelectedId.value }?.let { item->
            item.isSelected = !item.isSelected }
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

    Box( Modifier.fillMaxSize()) {
        Column(modifier.fillMaxHeight()) {
            Column(Modifier.fillMaxHeight().weight(1f) ) {
                Spacer(modifier = Modifier.weight(1f, !bottomSheetVisible.value))
                LazyColumnProduct(
                    uiState = uiState,
                    putProductInBasket = putProductInBasket,
                    changeProduct = changeProduct,
                    doSelected = { idItem -> isSelectedId.value = idItem } )
            }
        }
        startScreen = showFABs(
            startScreen = startScreen,
            isSelected = itemList.find { it.isSelected } != null,
            modifier = Modifier.height(200.dp).align(alignment = Alignment.BottomCenter),
            doDeleted = { deleteSelected.value = true },
            doChangeSection = { changeSectionSelected.value = true },
            doUnSelected = { unSelected.value = true }
        )
    }
}

@Composable
fun LazyColumnProduct(
    uiState: ProductsScreenState,
    putProductInBasket: (Product) -> Unit,
    changeProduct: (Product) -> Unit,
    doSelected: (Long) -> Unit
) {
    log( showLog,"LazyColumnProduct. ${uiState.products.size}")
    val listState = rememberLazyListState()
    val editProduct: MutableState<Product?> = remember { mutableStateOf(null) }

    if (editProduct.value != null ) {
        EditQuantityDialog(
            product = editProduct.value!!,
            listUnit = uiState.unitApp,
            onDismiss = { editProduct.value = null },
            onConfirm = {
                changeProduct(editProduct.value!!)
                editProduct.value = null
            }
        )
    }
    val listSection: List<List<Product>> = createDoubleListProduct(uiState.products)

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        item {
            HeaderImScreen(idImage = R.drawable.fon3,
                text = stringResource(R.string.products_in_basket) + " " + uiState.nameBasket, )
        }
        items(items = listSection) { item ->
            Column(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SectionColor)) {
                HeaderSection(text = item[0].article.section.nameSection, modifier = Modifier)
                LayoutColumProducts(
                    products = item,
                    putProductInBasket = putProductInBasket,
                    editProduct = { product -> editProduct.value = product },
                    doSelected = doSelected )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LayoutColumProducts(
    products: List<Product> ,
    putProductInBasket: (Product) -> Unit,
    editProduct: (Product) -> Unit,
    doSelected: (Long) -> Unit
) {

    log( showLog,"LayoutColumProducts. ${products.size}")
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        for (product in products){
            key(product.idProduct){
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                            putProductInBasket( product ) }
                        false
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    dismissThresholds = { direction ->
                        FractionalThreshold( if (direction == DismissDirection.StartToEnd) 0.4f else 0.4f)
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
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            contentAlignment = alignment ) {
                            if (dismissState.progress.fraction != 1.0f) Icon(icon, null)
                        }
                    }
                ) {
                    SectonProduct(product, doSelected, editProduct) //{ product -> editProductValue( product )}
                }
            }
        }
    }
}

@Composable
fun SectonProduct(
    sectionItems: Product,
    doSelected: (Long)->Unit,
    editProduct: (Product) -> Unit,
){
//    log( showLog,"ElementColum")

    Box(Modifier.padding(horizontal = 6.dp)) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(Color.White)
        ) {
//            log( showLog,"product:${item.article.nameArticle}, selected = ${item.isSelected}")
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .background(if (sectionItems.isSelected) Color.Red else Color.LightGray)
                    .height(32.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { doSelected(sectionItems.idProduct) }
            )
            MyTextH1(
                sectionItems.article.nameArticle,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .clickable { doSelected(sectionItems.idProduct) }
                    .padding(
                        start = dimensionResource(R.dimen.lazy_padding_hor),
                        top = dimensionResource(R.dimen.lazy_padding_ver),
                        bottom = dimensionResource(R.dimen.lazy_padding_ver)
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            val num = if (sectionItems.value.rem(1).equals(0.0)) sectionItems.value.toInt()
            else sectionItems.value
            MyTextH1End(num.toString(),
                Modifier
                    .width(70.dp)
                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                    .clickable { editProduct(sectionItems) })
            Spacer(modifier = Modifier.width(4.dp))
            MyTextH1(
                sectionItems.article.unitApp.nameUnit,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .width(50.dp)
                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                    .clickable { doSelected(sectionItems.idProduct) }
            )
        }
        if ( sectionItems.putInBasket ) Divider(
            color = MaterialTheme.colors.onSurface,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 8.dp, end = 8.dp)
        )
    }
}

@Composable
fun LayoutAddEditProduct(uiState: ProductsScreenState, onAddProduct: (Product) -> Unit) {

    log( showLog, "LayoutAddEditProduct")
    val nameSection = stringResource(R.string.name_section)
    val unitStuff = stringResource(R.string.unit_st)
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
            uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L,
            enterUnit.value.second)
        enterSection.value = Pair(
            uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L,
            enterSection.value.second)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)) {
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
                listItems = uiState.unitApp.map { Pair(it.idUnit, it.nameUnit) },
                label = stringResource(R.string.units),
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                readOnly = true,
                filtering = false,
                enabled = enterArticle.value.first <= 0
            )
        }
        Spacer(Modifier.height(36.dp))

        TextButtonOK(
            enabled = enterArticle.value.second != "",
            onConfirm = {
                onAddProduct(
                    ProductDB(
                        value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble(),
                        putInBasket = false,
                        articleId = enterArticle.value.first,
                        article = ArticleDB(
                            idArticle = enterArticle.value.first,
                            nameArticle = enterArticle.value.second,
                            position = 0,
                            section = SectionDB(enterSection.value.first, enterSection.value.second),
                            unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
                            isSelected = false,
                        )
                ))
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

