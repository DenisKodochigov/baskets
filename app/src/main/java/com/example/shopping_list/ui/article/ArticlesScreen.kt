package com.example.shopping_list.ui.article

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.shopping_list.utils.DismissBackground
import com.example.shopping_list.utils.log
import com.example.shopping_list.utils.selectSectionWithArticle
import com.example.shopping_list.utils.selectUnitWithArticle

const val showLog = false
@Composable
fun ArticlesScreen( showBottomSheet: MutableState<Boolean>) {
    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()

    ArticleScreenInitDate(viewModel = viewModel, showBottomSheet = showBottomSheet)
}

@Composable
fun ArticleScreenInitDate(
    viewModel: ArticleViewModel,
    showBottomSheet: MutableState<Boolean>,
) {
    log( showLog,"ArticleScreenInitDate")
    val uiState by viewModel.articleScreenState.collectAsState()
    if (showBottomSheet.value)
        LayoutAddEditArticle(
            uiState = uiState,
            onAddArticle = { article -> viewModel.addArticle(article, uiState.sorting) },
            onDismiss = { showBottomSheet.value = false})
    LayoutArticleScreen(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        uiState = viewModel.articleScreenState.collectAsState().value,
        changeArticle = { article -> viewModel.changeArticle(article, uiState.sorting) },
        doDeleteSelected = { articles -> viewModel.deleteSelected(articles, uiState.sorting) },
        doChangeSortingBy = { sortingBy -> viewModel.doChangeSortingBy(sortingBy) },
        doChangeSectionSelected = { articles, idSection ->
            viewModel.changeSectionSelected(articles, idSection, uiState.sorting) },
        doSelected = { articleId -> viewModel.changeSelected(articleId) },
    )
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun LayoutArticleScreen(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    changeArticle: (Article) -> Unit,
    doChangeSectionSelected: (List<Article>, Long) -> Unit,
    doDeleteSelected: (List<Article>) -> Unit,
    doChangeSortingBy: (SortingBy) -> Unit,
    doSelected: (Long) -> Unit
) {
    log( showLog,"LayoutArticleScreen")
    val isSelectedId: MutableState<Long> = remember { mutableStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна

    if (isSelectedId.value > 0L) {
        log( showLog,"LayoutArticleScreen if (isSelectedId.value > 0L) ${isSelectedId.value}")
        doSelected(isSelectedId.value)
        isSelectedId.value = 0
    }
    if (unSelected.value) {
        uiState.article.forEach { articles -> articles.forEach { it.isSelected = false } }
        unSelected.value = false
    }
    if (changeSectionSelected.value) {
        SelectSectionDialog(
            listSection = uiState.sections,
            onDismiss = { changeSectionSelected.value = false },
            onConfirm = {
                if (it != 0L) doChangeSectionSelected (uiState.article.flatten(), it)
                changeSectionSelected.value = false
            },
        )
    }
    if (deleteSelected.value) {
        doDeleteSelected( uiState.article.flatten() )
        deleteSelected.value = false
    }

    Box( Modifier.fillMaxSize() ) {
        Column(modifier.fillMaxHeight()) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .weight(1f)) {
                LazyColumnArticle(
                    uiState = uiState,
                    changeArticle = changeArticle,
                    doSelected = { idItem -> isSelectedId.value = idItem },
                    doDelete = { items -> doDeleteSelected( items ) })
            }
            log(showLog,"LayoutArticleScreen.SwitcherButton")
            SwitcherButton(doChangeSortingBy)
        }
        startScreen = showFABs(
            startScreen = startScreen,
            isSelected =  uiState.article.flatten().find { it.isSelected } != null,
            modifier = Modifier
                .height(200.dp)
                .align(alignment = Alignment.BottomCenter),
            doDeleted = { deleteSelected.value = true },
            doChangeSection = { changeSectionSelected.value = true },
            doUnSelected = { unSelected.value = true }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LazyColumnArticle(
    uiState: ArticleScreenState,
    changeArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    log( showLog,"LazyColumnArticle")
    val listState = rememberLazyListState()
    val editArticle: MutableState<Article?> = remember { mutableStateOf(null) }

    if (editArticle.value != null) {
        EditArticleDialog(
            article = editArticle.value!!,
            listUnit = uiState.unitApp,
            listSection = uiState.sections,
            onDismiss = { editArticle.value = null },
            onConfirm = {
                changeArticle(it)
                editArticle.value = null
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
            HeaderImScreen(text = stringResource(R.string.product), R.drawable.fon5_1) }
        items( items = uiState.article )
        { item ->
            Column( modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (item[0].section.colorSection > 0) Color(item[0].section.colorSection) else SectionColor)
                .animateItemPlacement()) {//SectionColor
                HeaderSection(
                    text = if ( uiState.sorting == SortingBy.BY_SECTION) item[0].section.nameSection
                    else stringResource(id = R.string.all), modifier = Modifier)
                LayoutColumArticles(
                    modifier = Modifier,
                    articles = item,
                    editArticle = { article -> editArticle.value = article },
                    doSelected = doSelected,
                    doDelete = doDelete)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutColumArticles(
    modifier: Modifier,
    articles: List<Article>,
    editArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    log( showLog,"LayoutColumArticles")

    val show = remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        for (article in articles){
            key(article.idArticle){
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            show.value = false
                            article.isSelected = true
                            doDelete(mutableListOf(article))
                        } else if (it == DismissValue.DismissedToEnd) {
                            editArticle( article )
                        }
                        false
                    }
                )
                AnimatedVisibility( visible = show.value, exit = fadeOut(spring())) {
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = modifier.padding(vertical = 1.dp),
                        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                        background = { DismissBackground(dismissState) },
                        dismissContent = { ElementColum(modifier, article, doSelected ) }
                    )
                }
            }
        }
    }
}

@Composable
fun ElementColum(modifier: Modifier, item: Article, doSelected: (Long)->Unit){
    log( showLog,"ElementColum Articles")
    Box (modifier.padding(horizontal = 6.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(BackgroundElementList)
                .clickable { doSelected(item.idArticle) }
        ) {
            Spacer( modifier = modifier
                .width(8.dp)
                .height(32.dp)
                .background(if (item.isSelected) Color.Red else Color.LightGray)
                .align(Alignment.CenterVertically)
            )
            MyTextH2(
                text = item.nameArticle,
                modifier = modifier
                    .weight(1f)
                    .padding(
                        start = dimensionResource(R.dimen.lazy_padding_hor),
                        top = dimensionResource(R.dimen.lazy_padding_ver),
                        bottom = dimensionResource(R.dimen.lazy_padding_ver)
                    )
            )
            Spacer(modifier = modifier.width(4.dp))
            MyTextH2(
                text = item.unitApp.nameUnit,
                modifier = modifier
                    .width(40.dp)
                    .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutAddEditArticle(
    uiState: ArticleScreenState, onAddArticle: (Article) -> Unit, onDismiss:() -> Unit )
{
    log( showLog,"BottomSheetContentArticle")
    val sheetState = rememberModalBottomSheetState()
    val nameSection = stringResource(R.string.name_section)
    val stuff = stringResource(R.string.unit_st)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, stuff)) }
    val listArticle = uiState.article.flatten()

    ModalBottomSheet( onDismissRequest = onDismiss, sheetState = sheetState ) {
        if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
            val id: Long =
                uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
            enterUnit.value = Pair(id, enterUnit.value.second)
        }
        if (enterSection.value.first == 0L && enterSection.value.second != "") {
            val id: Long =
                uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L
            enterSection.value = Pair(id, enterSection.value.second)
        }
        if (enterArticle.value.first == 0L && enterArticle.value.second != "") {
            val id: Long =
                listArticle.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
            enterArticle.value = Pair(id, enterArticle.value.second)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.85).dp)
        ) {
            HeaderScreen(text = stringResource(R.string.add_product))
//        Spacer(Modifier.height(24.dp))
            MyExposedDropdownMenuBox(
                /** Select article*/
                listItems = listArticle.map { Pair(it.idArticle, it.nameArticle) },
                label = stringResource(R.string.select_product),
                modifier = Modifier.fillMaxWidth(),
                enterValue = enterArticle,
                filtering = true
            )
            if (enterArticle.value.first > 0) {
                enterSection.value = selectSectionWithArticle(enterArticle.value.first, listArticle)
                enterUnit.value = selectUnitWithArticle(enterArticle.value.first, listArticle)
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
                section = SectionDB(
                    idSection = enterSection.value.first,
                    nameSection = enterSection.value.second
                ),
                unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
                isSelected = false,
                position = 0
            )

            TextButtonOK(
                enabled = enterArticle.value.second != "",
                onConfirm = {
                    onAddArticle(article)
                    enterArticle.value = Pair(0, "")
                }
            )
            Spacer(Modifier.height(72.dp))
        }
    }
}

