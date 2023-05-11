package com.example.shopping_list.ui.article

import android.annotation.SuppressLint
import android.os.Handler
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.components.*
import kotlinx.coroutines.delay

@Composable
fun ArticlesScreen(
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit,
){
    viewModel.getArticles()
    val uiState by viewModel.stateArticlesScreen.collectAsState()

    Log.d("KDS", "ArticlesScreen")
    bottomSheetContent.value = {
        BottomSheetContentArticle(
            uiState = uiState,
            bottomSheetHide = bottomSheetHide,
            onAddArticle = { article-> viewModel.addArticle( article)}
        )
    }
//    Log.d("KDS", "basketId = $basketId") ProductsScreenLayout
    ScreenLayoutArticle(
        modifier = Modifier.padding(bottom =  dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState ,
        changeArticle = { article-> viewModel.changeArticle( article )},
        doChangeGroupSelected = {
                articles, idGroup -> viewModel.changeGroupSelectedArticle(articles,idGroup)},
        doDeleteSelected = {articles -> viewModel.deleteSelectedArticle(articles)},
        refreshPosition = { direction -> viewModel.setPositionArticle( direction)}
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun ScreenLayoutArticle(
    modifier: Modifier = Modifier,
    uiState: StateArticlesScreen,
    changeArticle: (Article) -> Unit,
    doChangeGroupSelected: (MutableList<Article>, Long) -> Unit,
    doDeleteSelected: (MutableList<Article>) -> Unit,
    refreshPosition: (Int) -> Unit,
){
    val isSelectedId: MutableState<Long> = remember {  mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val changeGroupSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
//
    Log.d("KDS", "ScreenLayoutArticle")
    val itemList = uiState.article
    if (isSelectedId.value > 0L) {
        val item = itemList.find { it.idArticle == isSelectedId.value }
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
            LazyColumnArticle(modifier, uiState, doDeleteSelected, changeArticle, isSelectedId)
            ButtonSwipeArticle( refreshPosition )
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
fun LazyColumnArticle(
    modifier: Modifier = Modifier,
    uiState: StateArticlesScreen,
    doDeleteSelected: (MutableList<Article>) -> Unit,
    changeArticle: (Article) -> Unit,
    isSelected: MutableState<Long>)
{
    Log.d("KDS", "LazyColumnArticle")

    val listState = rememberLazyListState()
    val showDialog = remember { mutableStateOf(false) }
    val editArticle: MutableState<Article?> = remember {  mutableStateOf(null) }

    if (editArticle.value != null && showDialog.value){
//        EditQuantityDialog(
//            product = editArticle.value!!,
//            listUnit = unitList,
//            showDialog = showDialog.value,
//            onDismiss = { showDialog.value = false },
//            onConfirm = {
//                changeProductInBasket(editArticle.value!!)
//                showDialog.value = false
//            },
//            modifier = Modifier
//        )
    }
    val articleList: List<Article> = uiState.article.sortedBy { it.position }

    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items(items = articleList, key = {it.idArticle})
        { item ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if ( it == DismissValue.DismissedToStart) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            doDeleteSelected(mutableListOf(item)) }, 1000)
                    } else if (it == DismissValue.DismissedToEnd) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            editArticle.value = item
                            showDialog.value = true }, 1000)
                    }
                true
            })
            if (dismissState.isDismissed(DismissDirection.EndToStart) || dismissState.isDismissed(
                    DismissDirection.StartToEnd)) {
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
                        DismissDirection.StartToEnd -> Icons.Default.Edit
                        DismissDirection.EndToStart -> Icons.Default.Delete
                    }
                    val colorIcon = when (direction) {
                        DismissDirection.StartToEnd -> Color.Green
                        DismissDirection.EndToStart -> Color.Red
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)
                    Box(
                        Modifier.fillMaxSize(), contentAlignment = alignment) {
                        Icon( icon, null, modifier = Modifier.scale(scale), tint = colorIcon )
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
                                    .clickable { isSelected.value = item.idArticle }
                            )
                            Text(
                                text = item.nameArticle,
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = dimensionResource(R.dimen.lazy_padding_hor),
                                        top = dimensionResource(R.dimen.lazy_padding_ver),
                                        bottom = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable { isSelected.value = item.idArticle }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.group?.nameGroup ?: "",
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable {
                                        editArticle.value = item
                                        showDialog.value = true
                                    }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.unitA?.nameUnit ?: "",
                                style = MaterialTheme.typography.h1,
                                modifier = Modifier
                                    .width(40.dp)
                                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                                    .clickable { isSelected.value = item.idArticle }
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomSheetContentArticle(
    uiState: StateArticlesScreen,
    bottomSheetHide: () -> Unit,
    onAddArticle: (Article) -> Unit)
{
//    Log.d("KDS", "BottomSheetContentProduct")
//    val screenHeight = LocalConfiguration.current.screenHeightDp
//    val enterValue = remember{ mutableStateOf("1")}
//    val enterArticle = remember{ mutableStateOf(Pair<Long,String>(0,""))}
//    val enterGroup = remember{ mutableStateOf(Pair<Long,String>(1,"All"))}
//    val enterUnit = remember{ mutableStateOf(Pair<Long,String>(1,"шт"))}
//    val focusRequesterSheet = remember { FocusRequester() }
//
//    if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
//        val id:Long = uiState.unitA.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
//        enterUnit.value = Pair(id, enterUnit.value.second )
//    }
//    if (enterGroup.value.first == 0L && enterGroup.value.second != "") {
//        val id:Long = uiState.group.find { it.nameGroup == enterGroup.value.second }?.idGroup ?: 0L
//        enterGroup.value = Pair(id, enterGroup.value.second )
//    }
//    if (enterArticle.value.first == 0L && enterArticle.value.second != "") {
//        val id:Long = uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
//        enterArticle.value = Pair(id, enterArticle.value.second )
//    }
//    Column(
//        Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 24.dp)
//            .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.75).dp)) {
////        Log.d("KDS", "BottomSheetContentProduct.Column")
//        HeaderScreen(text = "Add product", Modifier.focusRequester(focusRequesterSheet))
//        Spacer(Modifier.height(24.dp))
//        MyExposedDropdownMenuBox(/** Select article*/
//            listItems = uiState.articles.map{ Pair(it.idArticle, it.nameArticle) },
//            label = "Select product",
//            modifier = Modifier.fillMaxWidth(),
//            enterValue = enterArticle,
//            filtering = true )
//        if (enterArticle.value.first > 0) {
//            enterGroup.value = selectGroupWithArticle(enterArticle.value.first, uiState.articles)
//            enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
//            enterValue.value = "1"
//        }
//        Spacer(Modifier.height(12.dp))
//        Row(Modifier.fillMaxWidth()) {
//            MyExposedDropdownMenuBox(/** Select group*/
//                listItems = uiState.group.map{ Pair(it.idGroup, it.nameGroup) },
//                label = "Group",
//                modifier = Modifier.weight(1f),
//                enterValue = enterGroup,
//                filtering = true)
//            Spacer(Modifier.width(4.dp))
//            MyOutlinedTextFieldWithoutIcon( /** Value*/
//                modifier = Modifier.background(Color.LightGray)
//                    .align(Alignment.CenterVertically)
//                    .width(200.dp)
//                    .padding(top = 4.dp),
//                enterValue = enterValue)
//            Spacer(Modifier.width(4.dp))
//            MyExposedDropdownMenuBox(/** Select unit*/
//                listItems = uiState.unitA.map{ Pair(it.idUnit, it.nameUnit) },
//                label = "Unit",
//                modifier = Modifier.width(120.dp),
//                enterValue = enterUnit,
//                filtering = false)
//        }
//        Spacer(Modifier.height(36.dp))
//        Row(Modifier.fillMaxWidth()) {
//            ButtonMy(Modifier.weight(1f),"Add") {
//                onAddProduct(
//                    ProductEntity(
//                        value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble(),
//                        article = ArticleEntity(
//                            idArticle = enterArticle.value.first,
//                            nameArticle = enterArticle.value.second,
//                            group = GroupEntity(
//                                idGroup = if (enterGroup.value.first != 0L) enterGroup.value.first else 1,
//                                nameGroup = enterGroup.value.second
//                            ),
//                            unitA = UnitEntity(
//                                idUnit = enterUnit.value.first,
//                                nameUnit = enterUnit.value.second
//                            )
//                        )
//                    )
//                )
//                enterArticle.value = Pair(0,"")
//            }
//            Spacer(Modifier.width(12.dp))
//            ButtonMy(Modifier.weight(1f), "Cancel"){
//                enterArticle.value = Pair(0,"")
//                bottomSheetHide() }
//        }
//        Spacer(Modifier.height(72.dp))
//    }
}