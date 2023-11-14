package com.example.basket.ui.screens.article

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.entity.Article
import com.example.basket.entity.BottomSheetInterface
import com.example.basket.entity.SizeElement
import com.example.basket.entity.SortingBy
import com.example.basket.entity.TypeText
import com.example.basket.entity.UPDOWN
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.bottomsheets.bottomSheetArticle.BottomSheetArticleGeneral
import com.example.basket.ui.bottomsheets.bottomSheetArticle.BottomSheetArticleState
import com.example.basket.ui.bottomsheets.bottomSheetSectionSelect.BottomSheetSectionSelect
import com.example.basket.ui.components.*
import com.example.basket.ui.components.dialog.articuleDialog.EditArticleDialog
import com.example.basket.ui.components.dialog.SelectSectionDialog
import com.example.basket.ui.theme.SectionColor
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.DismissBackground
import com.example.basket.utils.bottomBarAnimatedScroll
import kotlin.math.roundToInt

@Composable
fun ArticlesScreen( screen: ScreenDestination)
{
    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()
    ArticleScreenInitDate(viewModel = viewModel, screen = screen)
}

@Composable
fun ArticleScreenInitDate(viewModel: ArticleViewModel, screen: ScreenDestination,
) {
    val uiState by viewModel.articleScreenState.collectAsState()
    uiState.onAddArticle = remember {{ article -> viewModel.addArticle(article, uiState.sorting) }}
    uiState.changeArticle = remember {{ article -> viewModel.changeArticle(article, uiState.sorting) }}
    uiState.doDeleteSelected = remember {{ articles -> viewModel.deleteSelected(articles, uiState.sorting) }}
    uiState.doChangeSortingBy = remember {{ sortingBy -> viewModel.doChangeSortingBy(sortingBy) }}
    uiState.doChangeSectionSelected = remember {{ articles, idSection ->
        viewModel.changeSectionSelected(articles, idSection, uiState.sorting) }}
    uiState.doSelected = remember {{ articleId -> viewModel.changeSelected(articleId) }}
    uiState.idImage = getIdImage(screen)
    uiState.screenTextHeader = stringResource(screen.textHeader)

    screen.textFAB = stringResource(id = R.string.products)
    screen.onClickFAB = { uiState.triggerRunOnClickFAB.value = true}

    if (uiState.triggerRunOnClickFAB.value) BottomSheetArticleGeneral(uiStateA = uiState)

    ArticleScreenLayout(uiState = uiState)
}
@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun ArticleScreenLayout(uiState: ArticleScreenState)
{
    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    if (isSelectedId.value > 0L) {
        uiState.doSelected(isSelectedId.value)
        isSelectedId.value = 0
    }
    if (unSelected.value) {
        uiState.articles.forEach { articles -> articles.forEach { it.isSelected = false } }
        unSelected.value = false
    }
    if (changeSectionSelected.value) {
        BottomSheetSectionSelect(
            uiState = BottomSheetArticleState(
            onConfirmationSelectSection = {
                if (it.selectedSection.value?.idSection != 0L) {
                    it.selectedSection.value?.idSection?.let { it1 ->
                        uiState.doChangeSectionSelected (uiState.articles.flatten(), it1) }
                }
                changeSectionSelected.value = false
            },
            onDismissSelectSection = { changeSectionSelected.value = false },
            buttonDialogSelectSection = changeSectionSelected,
            sections = uiState.sections,
        ) as BottomSheetInterface)
    }
    if (deleteSelected.value) {
        uiState.doDeleteSelected( uiState.articles.flatten() )
        deleteSelected.value = false
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = dimensionResource(R.dimen.screen_padding_hor)) ) {
        Column(Modifier.fillMaxHeight()) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .bottomBarAnimatedScroll(
                        height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                        offsetHeightPx = bottomBarOffsetHeightPx
                    )
                    .weight(1f)) {
                ArticleLazyColumn(
                    uiState = uiState,
                    scrollOffset =-bottomBarOffsetHeightPx.floatValue.roundToInt(),
                    doSelected = { idItem -> isSelectedId.value = idItem },
                    doDelete = { items -> uiState.doDeleteSelected( items ) })
            }
            SwitcherButton(uiState.doChangeSortingBy)
        }
        startScreen = showFABs(
            startScreen = startScreen,
            isSelected =  uiState.articles.flatten().find { it.isSelected } != null,
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            doDeleted = { deleteSelected.value = true },
            doChangeSection = { changeSectionSelected.value = true },
            doUnSelected = { unSelected.value = true }
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ArticleLazyColumn(
    uiState: ArticleScreenState,
    scrollOffset:Int,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    val listState = rememberLazyListState()
    val editArticle: MutableState<Article?> = remember { mutableStateOf(null) }

    val showArrowUp = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 }}.value
    val showArrowDown = remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
            listState.layoutInfo.totalItemsCount - 1 } }.value

    if (editArticle.value != null) {
        EditArticleDialog(
            article = editArticle.value!!,
            listUnit = uiState.unitApp,
            listSection = uiState.sections,
            onDismiss = { editArticle.value = null },
            onConfirm = {
                uiState.changeArticle(it)
                editArticle.value = null
            }
        )
    }
    CollapsingToolbar(
        text = uiState.screenTextHeader,
        idImage = uiState.idImage,
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    ShowArrowVer(direction = UPDOWN.UP, enable = showArrowUp && uiState.articles.isNotEmpty(), drawLine = false)
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        itemsIndexed( items = uiState.articles ) { _,item ->
            Column( modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .animateItemPlacement()
                .background(
                    if (uiState.articles.size == 1) SectionColor
                    else {
                        if (item[0].section.colorSection != 0L) Color(item[0].section.colorSection)
                        else SectionColor
                    }
                )
                .animateItemPlacement()) {//SectionColor
                HeaderSection(
                    text = if ( uiState.sorting == SortingBy.BY_SECTION) item[0].section.nameSection
                    else stringResource(id = R.string.all), modifier = Modifier)
                ArticleLayoutColum(
                    modifier = Modifier,
                    articles = item,
                    editArticle = { article -> editArticle.value = article },
                    doSelected = doSelected,
                    doDelete = doDelete)
            }
        }
    }
    ShowArrowVer(direction = UPDOWN.DOWN, enable = showArrowDown && uiState.articles.isNotEmpty(), drawLine = false)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleLayoutColum(
    modifier: Modifier,
    articles: List<Article>,
    editArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
) {
    var show by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        for (article in articles){
            key(article.idArticle){
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            show = false
                            article.isSelected = true
                            doDelete(mutableListOf(article))
                        } else if (it == DismissValue.DismissedToEnd) { editArticle( article ) }
                        false
                    }, positionalThreshold = { 250.dp.toPx() }
                )
                AnimatedVisibility( visible = show, exit = fadeOut(spring())) {
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
fun ElementColum(modifier: Modifier, item: Article, doSelected: (Long)->Unit)
{
    val localDensity = LocalDensity.current
    var heightIs by remember { mutableStateOf(0.dp) }
//    val modifier = modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    
    Box (
        modifier
            .padding(horizontal = 6.dp)
            .onGloballyPositioned { coordinates ->
                heightIs = with(localDensity) { coordinates.size.height.toDp() }
            })
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
                .clickable { doSelected(item.idArticle) }
        ) {
            Spacer( modifier = modifier
                .width(8.dp)
                .height(heightIs)
                .background(if (item.isSelected) Color.Red else Color.LightGray)
                .align(Alignment.CenterVertically)
            )
            TextApp (text = item.nameArticle,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .weight(1f)
                    .padding(start = 6.dp)
                    .padding(vertical = 6.dp),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST)
            )
            Spacer(modifier = modifier.width(4.dp))
            TextApp (text = item.unitApp.nameUnit,
                textAlign = TextAlign.Left,
                modifier = modifier.width(40.dp),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST)
            )
        }
    }
}
@Preview
@Composable fun ElementColumPreview(){
    ElementColum(modifier = Modifier,
        item = ArticleDB(nameArticle = "Moloko", sectionId = 1L, unitId = 1L) as Article,
        doSelected = {})
}

