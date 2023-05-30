package com.example.shopping_list.ui.article

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.ui.components.*
import com.example.shopping_list.ui.components.dialog.EditArticleDialog
import com.example.shopping_list.ui.components.dialog.SelectGroupDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ArticlesScreen( bottomSheetContent: MutableState<@Composable (() -> Unit)?>){

    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()
    val uiState by viewModel.articleScreenState.collectAsState()

    if (uiState.article.isNotEmpty()) Log.d("KDS", "ArticlesScreen ${uiState.article[0].group.nameGroup}")
    bottomSheetContent.value = {
        LayoutAddEditArticle(
            uiState = uiState,
            onAddArticle = { article-> viewModel.addArticle( article )}
        )
    }
    LayoutArticleScreen(
        modifier = Modifier.padding(bottom =  dimensionResource(R.dimen.screen_padding_hor)),
        uiState = uiState,
        changeArticle = { article-> viewModel.changeArticle( article )},
        doChangeGroupSelected = { articles, idGroup ->
            viewModel.changeGroupSelectedArticle( articles, idGroup )},
        doDeleteSelected = { articles -> viewModel.deleteSelectedArticle( articles )},
        movePosition = { direction -> viewModel.movePositionArticle(direction) }
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutArticleScreen(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    changeArticle: (Article) -> Unit,
    doChangeGroupSelected: (List<Article>, Long) -> Unit,
    doDeleteSelected: (List<Article>) -> Unit,
    movePosition: (Int) -> Unit,
){
    var isSelectedId: MutableState<Long> = remember {  mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
    val changeGroupSelected: MutableState<Boolean> = remember {  mutableStateOf(false) }
//
    if (uiState.article.isNotEmpty()) Log.d("KDS", "ScreenLayoutArticle ${uiState.article[0].group.nameGroup}")

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
            HeaderScreen(text = "Products", modifier)
            Column(Modifier.fillMaxHeight().weight(1f)) {
                Spacer(modifier = Modifier.weight(1f))
                LazyColumnArticle(
                    modifier = modifier,
                    uiState = uiState,
                    doDeleteSelected = doDeleteSelected,
                    changeArticle = changeArticle,
                    doSelected = { idItem -> isSelectedId.value = idItem})
            }
            ButtonSwipe( movePosition )
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
fun LazyColumnArticle(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    doDeleteSelected: (List<Article>) -> Unit,
    changeArticle: (Article) -> Unit,
    doSelected: (Long)->Unit )
{
    if (uiState.article.isNotEmpty()) Log.d(
        "KDS", "LazyColumnArticle ${uiState.article[0].group.nameGroup}")

    val listState = rememberLazyListState()
    val showDialog = remember { mutableStateOf(false) }
    val firstItem = remember { mutableStateOf(Pair<Int, Long>(0, 0)) }
    val editArticle: MutableState<Article?> = remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    if (editArticle.value != null && showDialog.value) {
        EditArticleDialog(
            article = editArticle.value!!,
            listUnit = uiState.unitA,
            listGroup = uiState.group,
            onDismiss = { showDialog.value = false },
            onConfirm = {
                changeArticle(editArticle.value!!)
                showDialog.value = false
            },
        )
    }

    if (uiState.article.isNotEmpty()) {
        if (firstItem.value.first != uiState.article[0].position ||
            firstItem.value.second != uiState.article[0].idArticle) {
            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
            firstItem.value = Pair(uiState.article[0].position, uiState.article[0].idArticle)
        }
    }
    LazyColumn (
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items(items = uiState.article, key = {it.idArticle})
        { item ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if ( it == DismissValue.DismissedToStart) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({
                            item.isSelected = true
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
                            .clickable { doSelected(item.idArticle) }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .background( if (item.isSelected) Color.Red else Color.LightGray)
                                    .width(8.dp)
                                    .height(32.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            MyTextH1( text = item.nameArticle, modifier = Modifier
                                .weight(1f)
                                .padding(
                                    start = dimensionResource(R.dimen.lazy_padding_hor),
                                    top = dimensionResource(R.dimen.lazy_padding_ver),
                                    bottom = dimensionResource(R.dimen.lazy_padding_ver))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            MyTextH1(text = item.group.nameGroup, modifier = Modifier
                                .width(100.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            MyTextH1(text = item.unitA.nameUnit, modifier = Modifier
                                .width(40.dp)
                                .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
                            )
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun LayoutAddEditArticle(
    uiState: ArticleScreenState,
    onAddArticle: (Article) -> Unit)
{
    Log.d("KDS", "BottomSheetContentArticle")
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
        val id:Long = uiState.article.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
        enterArticle.value = Pair(id, enterArticle.value.second )
    }
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.75).dp)) {
        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen(text = "Add product", Modifier.focusRequester(focusRequesterSheet))
        Spacer(Modifier.height(24.dp))
        MyExposedDropdownMenuBox(/** Select article*/
            listItems = uiState.article.map{ Pair(it.idArticle, it.nameArticle) },
            label = "Select product",
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true )
        if (enterArticle.value.first > 0) {
            enterGroup.value = selectGroupWithArticle(enterArticle.value.first, uiState.article)
            enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.article)
            enterValue.value = "1"
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            MyExposedDropdownMenuBox(/** Select group*/
                listItems = uiState.group.map{ Pair(it.idGroup, it.nameGroup) },
                label = "Group",
                modifier = Modifier.weight(1f),
                enterValue = enterGroup,
                filtering = false)
            Spacer(Modifier.width(4.dp))
            MyExposedDropdownMenuBox(/** Select unit*/
                listItems = uiState.unitA.map{ Pair(it.idUnit, it.nameUnit) },
                label = "Unit",
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false)
        }
        Spacer(Modifier.height(36.dp))
        val article = ArticleEntity(
            idArticle = enterArticle.value.first,
            nameArticle = enterArticle.value.second,
            groupId = enterGroup.value.first,
            unitId = enterUnit.value.first)
        article.group = GroupEntity( enterGroup.value.first, enterGroup.value.second )
        article.unitA = UnitEntity( enterUnit.value.first, enterUnit.value.second )

        TextButtonOK( onConfirm = {
            onAddArticle(article)
            enterArticle.value = Pair(0,"")})
        Spacer(Modifier.height(72.dp))
    }
}