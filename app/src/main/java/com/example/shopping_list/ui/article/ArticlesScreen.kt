package com.example.shopping_list.ui.article

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveDone
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ArticleClass
import com.example.shopping_list.ui.components.*
import com.example.shopping_list.ui.components.dialog.EditArticleDialog
import com.example.shopping_list.ui.components.dialog.SelectSectionDialog
import com.example.shopping_list.ui.theme.BackgroundElementList
import com.example.shopping_list.ui.theme.SectionColor
import com.example.shopping_list.utils.createDoubleLisArticle

@Composable
fun ArticlesScreen(
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()

    ArticleScreenCreateView(
        viewModel = viewModel,
        bottomSheetVisible = bottomSheetVisible,
        bottomSheetContent = bottomSheetContent
    )
}

@Composable
fun ArticleScreenCreateView(
    viewModel: ArticleViewModel,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    val uiState by viewModel.articleScreenState.collectAsState()
    bottomSheetContent.value = {
        LayoutAddEditArticle(
            uiState = uiState,
            onAddArticle = { article -> viewModel.addArticle(article) }
        )
    }

    LayoutArticleScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = viewModel.articleScreenState.collectAsState().value,
        changeArticle = { article -> viewModel.changeArticle(article) },
        doChangeSectionSelected = { articles, idSection ->
            viewModel.changeSectionSelectedArticle(articles, idSection)
        },
        doDeleteSelected = { articles -> viewModel.deleteSelectedArticle(articles) },
        movePosition = { direction -> viewModel.movePositionArticle(direction) },
        bottomSheetVisible = bottomSheetVisible
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutArticleScreen(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    bottomSheetVisible: MutableState<Boolean>,
    changeArticle: (Article) -> Unit,
    doChangeSectionSelected: (List<Article>, Long) -> Unit,
    doDeleteSelected: (List<Article>) -> Unit,
    movePosition: (Int) -> Unit,
) {
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

    val itemList = uiState.article
    if (isSelectedId.value > 0L) {
        val item = itemList.find { it.idArticle == isSelectedId.value }
        if (item != null) item.isSelected = !item.isSelected
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

    Box( Modifier.fillMaxSize() ) {
        Column(modifier.fillMaxHeight()) {
            HeaderScreen(text = stringResource(R.string.product), modifier)
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                if (!bottomSheetVisible.value) Spacer(modifier = Modifier.weight(1f))
                LazyColumnArticle(
                    modifier = modifier,
                    uiState = uiState,
                    doDeleteSelected = doDeleteSelected,
                    changeArticle = changeArticle,
                    doSelected = { idItem -> isSelectedId.value = idItem })
            }
            ButtonMove(movePosition)
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
fun LazyColumnArticle(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    doDeleteSelected: (List<Article>) -> Unit,
    changeArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit
) {
    val listState = rememberLazyListState()
    val editArticle: MutableState<Article?> = remember { mutableStateOf(null) }
    val firstItem = remember { mutableStateOf(Pair<Int, Long>(0, 0)) }
    val coroutineScope = rememberCoroutineScope()

    if (editArticle.value != null) {
        EditArticleDialog(
            article = editArticle.value!!,
            listUnit = uiState.unitA,
            listSection = uiState.sections,
            onDismiss = { editArticle.value = null },
            onConfirm = {
                changeArticle(editArticle.value!!)
                editArticle.value = null
            },
        )
    }

//    if (uiState.article.isNotEmpty()) {
//        if (firstItem.value.first != uiState.article[0].position ||
//            firstItem.value.second != uiState.article[0].idArticle
//        ) {
//            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
//            firstItem.value = Pair(uiState.article[0].position, uiState.article[0].idArticle)
//        }
//    }
    val listSection = createDoubleLisArticle(uiState.article)

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items(items = listSection)
        { item ->
            Column(modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SectionColor)) {
                HeaderSection(text = item[0].section.nameSection, modifier = Modifier)
                LayoutColumArticles(
                    item,
                    doDeleteSelected,
                    { article -> editArticle.value = article },
                    doSelected)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LayoutColumArticles(
    articles: List<Article>,
    doDeleteSelected: (List<Article>) -> Unit,
    editArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit
) {
//    log("LayoutColumProducts")
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        for (article in articles){
            key(article.idArticle){
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            article.isSelected = true
//                            doDeleteSelected(mutableListOf(article))
                        } else if (it == DismissValue.DismissedToEnd) {
                            editArticle( article )
                        }
                        false
                    })
                SwipeToDismiss(state = dismissState,
                    modifier = Modifier.padding(vertical = 1.dp),
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
                            DismissDirection.StartToEnd -> Icons.Default.Edit
                            DismissDirection.EndToStart -> Icons.Default.Delete
                        }
                        val colorIcon = when (direction) {
                            DismissDirection.StartToEnd -> Color.Green
                            DismissDirection.EndToStart -> Color.Red
                        }
                        val scale by animateFloatAsState(
                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                        )
                        Box( Modifier.fillMaxSize().padding(horizontal = 12.dp),
                            contentAlignment = alignment) {
                            Icon(icon, null, modifier = Modifier.scale(scale), tint = colorIcon)
                        }
                    }
                ){
                    ElementColum(article, doSelected)
                }
            }
        }
    }
}

@Composable
fun ElementColum( item: Article, doSelected: (Long)->Unit ){
    Box (Modifier.padding(horizontal = 6.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(BackgroundElementList)
                .clickable { doSelected(item.idArticle) }
        ) {
            Spacer(
                modifier = Modifier.width(8.dp).height(32.dp)
                    .background(if (item.isSelected) Color.Red else Color.LightGray)
                    .align(Alignment.CenterVertically)
            )
            MyTextH2(
                text = item.nameArticle,
                modifier = Modifier.weight(1f)
                    .padding(
                        start = dimensionResource(R.dimen.lazy_padding_hor),
                        top = dimensionResource(R.dimen.lazy_padding_ver),
                        bottom = dimensionResource(R.dimen.lazy_padding_ver)
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyTextH2(
                text = item.unitA.nameUnit,
                modifier = Modifier.width(40.dp)
                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
            )
        }
    }
}

@Composable
fun LayoutAddEditArticle(
    uiState: ArticleScreenState,
    onAddArticle: (Article) -> Unit
) {
    Log.d("KDS", "BottomSheetContentArticle")
    val nameSection = stringResource(R.string.name_section)
    val stuff = stringResource(R.string.name_unit1)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, stuff)) }
    val focusRequesterSheet = remember { FocusRequester() }

    if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
        val id: Long = uiState.unitA.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
        enterUnit.value = Pair(id, enterUnit.value.second)
    }
    if (enterSection.value.first == 0L && enterSection.value.second != "") {
        val id: Long = uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L
        enterSection.value = Pair(id, enterSection.value.second)
    }
    if (enterArticle.value.first == 0L && enterArticle.value.second != "") {
        val id: Long =
            uiState.article.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
        enterArticle.value = Pair(id, enterArticle.value.second)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.85).dp)
    ) {
        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen( text = stringResource(R.string.add_product),
            Modifier.focusRequester(focusRequesterSheet) )
//        Spacer(Modifier.height(24.dp))
        MyExposedDropdownMenuBox(
            /** Select article*/
            listItems = uiState.article.map { Pair(it.idArticle, it.nameArticle) },
            label = stringResource(R.string.select_product),
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true
        )
        if (enterArticle.value.first > 0) {
            enterSection.value = selectSectionWithArticle(enterArticle.value.first, uiState.article)
            enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.article)
            enterValue.value = "1"
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            MyExposedDropdownMenuBox(
                /** Select section*/
                listItems = uiState.sections.map { Pair(it.idSection, it.nameSection) },
                label = stringResource(R.string.section),
                modifier = Modifier.weight(1f),
                enterValue = enterSection,
                filtering = false
            )
            Spacer(Modifier.width(4.dp))
            MyExposedDropdownMenuBox(
                /** Select unit*/
                listItems = uiState.unitA.map { Pair(it.idUnit, it.nameUnit) },
                label = stringResource(R.string.units),
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false
            )
        }
        Spacer(Modifier.height(36.dp))

        val article = ArticleClass(
            idArticle = enterArticle.value.first,
            nameArticle = enterArticle.value.second,
            section = SectionEntity(enterSection.value.first, enterSection.value.second),
            unitA = UnitEntity(enterUnit.value.first, enterUnit.value.second),
            isSelected = false,
            position = 0
        )
//        article.section = SectionEntity(enterSection.value.first, enterSection.value.second)
//        article.unitA = UnitEntity(enterUnit.value.first, enterUnit.value.second)

        TextButtonOK(
            enabled = enterArticle.value.second != "",
            onConfirm = {
            onAddArticle(article)
            enterArticle.value = Pair(0, "")
        })
        Spacer(Modifier.height(72.dp))
    }
}

