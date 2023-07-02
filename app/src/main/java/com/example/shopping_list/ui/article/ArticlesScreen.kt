package com.example.shopping_list.ui.article

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.ArticleDB
import com.example.shopping_list.data.room.tables.SectionDB
import com.example.shopping_list.data.room.tables.UnitDB
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.SortingBy
import com.example.shopping_list.ui.components.*
import com.example.shopping_list.ui.components.dialog.EditArticleDialog
import com.example.shopping_list.ui.components.dialog.SelectSectionDialog
import com.example.shopping_list.ui.theme.BackgroundElementList
import com.example.shopping_list.ui.theme.SectionColor
import com.example.shopping_list.utils.createDoubleLisArticle
import com.example.shopping_list.utils.log

@Composable
fun ArticlesScreen(
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()

    ArticleScreenInitDate(
        viewModel = viewModel,
        bottomSheetVisible = bottomSheetVisible,
        bottomSheetContent = bottomSheetContent
    )
}

@Composable
fun ArticleScreenInitDate(
    viewModel: ArticleViewModel,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    val uiState by viewModel.articleScreenState.collectAsState()
    bottomSheetContent.value = {
        LayoutAddEditArticle(uiState = uiState,
            onAddArticle = { article -> viewModel.addArticle(article) })
    }

    LayoutArticleScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = viewModel.articleScreenState.collectAsState().value,
        changeArticle = { article -> viewModel.changeArticle(article) },
        doChangeSectionSelected = { articles, idSection ->
            viewModel.changeSectionSelected(articles, idSection) },
        doDeleteSelected = { articles -> viewModel.deleteSelected(articles) },
        movePosition = { direction -> viewModel.movePosition(direction) },
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
    log("LayoutArticleScreen")
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val sortingBy: MutableState<SortingBy> = remember { mutableStateOf(SortingBy.BY_NAME) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

    val itemList = uiState.article
    if (isSelectedId.value > 0L) {
        itemList.find { it.idArticle == isSelectedId.value }?.let { it1-> it1.isSelected = !it1.isSelected }
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
            Column( Modifier.fillMaxHeight().weight(1f)) {
                Spacer(modifier = Modifier.weight(1f, !bottomSheetVisible.value))
                LazyColumnArticle(
                    uiState = uiState,
                    sortingBy = sortingBy,
                    changeArticle = changeArticle,
                    doSelected = { idItem -> isSelectedId.value = idItem },
                    doDelete = { items -> doDeleteSelected( items ) })
            }
            SwitchSorting { item -> sortingBy.value = item }
            ButtonMove(movePosition)
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LazyColumnArticle(
    uiState: ArticleScreenState,
    sortingBy: MutableState<SortingBy>,
    changeArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    log("LazyColumnArticle")
    val listState = rememberLazyListState()
    val listSection: MutableState<List<List<Article>>> = remember { mutableStateOf(emptyList()) }
    val editArticle: MutableState<Article?> = remember { mutableStateOf(null) }

    if (editArticle.value != null) {
        EditArticleDialog(
            article = editArticle.value!!,
            listUnit = uiState.unitApp,
            listSection = uiState.sections,
            onDismiss = { editArticle.value = null },
            onConfirm = {
                changeArticle(editArticle.value!!)
                editArticle.value = null
            },
        )
    }

    listSection.value = createDoubleLisArticle(uiState.article, sortingBy.value)

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items(items = listSection.value)
        { item ->
            Column( modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SectionColor)) {
                HeaderSection(text = if (sortingBy.value == SortingBy.BY_SECTION) item[0].section.nameSection
                    else stringResource(id = R.string.all), modifier = Modifier)
                LayoutColumArticles(
                    uiState = uiState,
                    articles = item,
                    editArticle = { article -> editArticle.value = article },
                    doSelected = doSelected,
                    doDelete = doDelete)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LayoutColumArticles(
    uiState: ArticleScreenState,
    articles: List<Article>,
    editArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    log("LayoutColumArticles")

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
                            doDelete(mutableListOf(article))
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
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            contentAlignment = alignment) {
                            if (dismissState.progress.fraction != 1.0f)
                                Icon(icon, null, tint = colorIcon)
                        }
                    }
                ){
                    ElementColum( article, doSelected, uiState)
                }
            }
        }
    }
}

@Composable
fun ElementColum( item: Article, doSelected: (Long)->Unit, uiState: ArticleScreenState,){
    log("ElementColum Articles")
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
                modifier = Modifier
                    .width(8.dp)
                    .height(32.dp)
                    .background(if (item.isSelected) Color.Red else Color.LightGray)
                    .align(Alignment.CenterVertically)
            )
            MyTextH2(
                text = item.nameArticle,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = dimensionResource(R.dimen.lazy_padding_hor),
                        top = dimensionResource(R.dimen.lazy_padding_ver),
                        bottom = dimensionResource(R.dimen.lazy_padding_ver)
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyTextH2(
                text = item.unitApp.nameUnit,
                modifier = Modifier
                    .width(40.dp)
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
//    Log.d("KDS", "BottomSheetContentArticle")
    val nameSection = stringResource(R.string.name_section)
    val stuff = stringResource(R.string.name_unit1)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, stuff)) }
    val focusRequesterSheet = remember { FocusRequester() }

    if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
        val id: Long = uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
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
                listItems = uiState.unitApp.map { Pair(it.idUnit, it.nameUnit) },
                label = stringResource(R.string.units),
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false
            )
        }
        Spacer(Modifier.height(36.dp))

        val article = ArticleDB(
            idArticle = enterArticle.value.first,
            nameArticle = enterArticle.value.second,
            section = SectionDB(enterSection.value.first, enterSection.value.second),
            unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
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

