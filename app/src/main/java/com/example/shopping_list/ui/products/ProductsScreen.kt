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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.shopping_list.ui.components.showFABs
import com.example.shopping_list.ui.components.TextButtonOK
import com.example.shopping_list.ui.components.dialog.EditQuantityDialog
import com.example.shopping_list.ui.components.dialog.SelectSectionDialog
import com.example.shopping_list.ui.theme.SectionColor
import com.example.shopping_list.utils.DismissBackground
import com.example.shopping_list.utils.log
import com.example.shopping_list.utils.selectSectionWithArticle
import com.example.shopping_list.utils.selectUnitWithArticle

const val showLog = true
@Composable
fun ProductsScreen(basketId: Long, showBottomSheet: MutableState<Boolean>) {

    val viewModel: ProductViewModel = hiltViewModel()
    viewModel.getStateProducts(basketId)

    ProductScreenCreateView(
        basketId = basketId,
        viewModel = viewModel,
        showBottomSheet = showBottomSheet,)
}

@Composable
fun ProductScreenCreateView(
    basketId: Long,
    viewModel: ProductViewModel,
    showBottomSheet: MutableState<Boolean>,
){
    val uiState by viewModel.productsScreenState.collectAsState()
    log( showLog,"ProductScreenCreateView. ${uiState.products.size}")

    if (showBottomSheet.value)
        LayoutAddEditProduct(
            uiState = uiState,
            onAddProduct = { product: Product -> viewModel.addProduct(product, basketId) },
            onDismiss = { showBottomSheet.value = false})

    LayoutProductsScreen(
        uiState = uiState,
        putProductInBasket = { product -> viewModel.putProductInBasket(product, basketId) },
        changeProduct = { product -> viewModel.changeProduct(product, basketId) },
        doChangeSectionSelected = { productList, idSection ->
            viewModel.changeSectionSelected(productList, idSection) },
        doDeleteSelected = { productList -> viewModel.deleteSelectedProducts(productList) },
        doSelected = { articleId -> viewModel.changeSelected(articleId) },
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutProductsScreen(
    uiState: ProductsScreenState,
    putProductInBasket: (Product) -> Unit,
    changeProduct: (Product) -> Unit,
    doChangeSectionSelected: (List<Product>, Long) -> Unit,
    doDeleteSelected: (List<Product>) -> Unit,
    doSelected: (Long) -> Unit
) {
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

    log( showLog,"LayoutProductsScreen. ${uiState.products.size}")

    if (isSelectedId.value > 0L) {
        doSelected(isSelectedId.value)
        isSelectedId.value = 0
    }
    if (unSelected.value) {
        uiState.products.forEach { productList -> productList.forEach { it.isSelected = false } }
        unSelected.value = false
    }
    if (changeSectionSelected.value) {
        SelectSectionDialog(
            listSection = uiState.sections,
            onDismiss = { changeSectionSelected.value = false },
            onConfirm = {
                if (it != 0L) doChangeSectionSelected( uiState.products.flatten(), it)
                changeSectionSelected.value = false
            },
        )
    }
    if (deleteSelected.value) {
        doDeleteSelected(uiState.products.flatten())
        deleteSelected.value = false
    }

    Box( Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxHeight() ) {
            LazyColumnProduct(
                uiState = uiState,
                putProductInBasket = putProductInBasket,
                changeProduct = changeProduct,
                doSelected = { idItem -> isSelectedId.value = idItem } )
        }
        startScreen = showFABs(
            startScreen = startScreen,
            isSelected = uiState.products.flatten().find { it.isSelected } != null,
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
                editProduct.value = null
                changeProduct (it)
            }
        )
    }

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
        items(items = uiState.products) { item ->
            Column(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                .background(
                    if (uiState.products.size == 1 ) SectionColor
                    else {
                        if (item[0].article.section.colorSection != 0L) Color(item[0].article.section.colorSection)
                        else SectionColor
                    }
                )) {
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

@OptIn(ExperimentalMaterial3Api::class)
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
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                            putProductInBasket( product ) }
                        false
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = { DismissBackground(dismissState) },
                    dismissContent = { SectionProduct(product, doSelected, editProduct) },
                )
            }
        }
    }
}


@Composable
fun SectionProduct(
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
//                .background(Color.White)
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
            MyTextH1(text = num.toString(), textAlign = TextAlign.End,
                modifier = Modifier
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
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 8.dp, end = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutAddEditProduct(uiState: ProductsScreenState, onAddProduct: (Product) -> Unit, onDismiss:() -> Unit) {

    log( showLog, "LayoutAddEditProduct")
    val nameSection = stringResource(R.string.name_section)
    val unitStuff = stringResource(R.string.unit_st)
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, unitStuff)) }
    val sheetState = rememberModalBottomSheetState()

    enterArticle.value = Pair(
        uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L,
        enterArticle.value.second
    )

    if (enterArticle.value.first > 0) {
        enterSection.value = selectSectionWithArticle(enterArticle.value.first, uiState.articles)
        enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
    } else {
        enterUnit.value = Pair(
            uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L,
            enterUnit.value.second
        )
        enterSection.value = Pair(
            uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L,
            enterSection.value.second
        )
    }

    ModalBottomSheet( onDismissRequest = onDismiss, sheetState = sheetState ) {

        Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp)) {
            HeaderScreen( text = stringResource(R.string.add_product))
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
                                section = SectionDB(
                                    enterSection.value.first,
                                    enterSection.value.second
                                ),
                                unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
                                isSelected = false,
                            )
                        )
                    )
                    enterArticle.value = Pair(0, "")
                    enterValue.value = "1"
                })
            Spacer(Modifier.height(36.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenLayoutPreview() {

}

