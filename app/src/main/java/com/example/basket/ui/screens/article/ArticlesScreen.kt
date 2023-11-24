package com.example.basket.ui.screens.article

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.bottomsheets.articleAdd.BottomSheetArticleGeneral
import com.example.basket.ui.bottomsheets.articleAdd.BottomSheetArticleState
import com.example.basket.ui.bottomsheets.articleEdit.BottomSheetArticleEdit
import com.example.basket.ui.bottomsheets.sectionSelect.BottomSheetSectionSelect
import com.example.basket.ui.components.*
import com.example.basket.ui.theme.Dimen
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.ItemSwipe
import com.example.basket.utils.animatedScroll
import com.example.basket.utils.log
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
){
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

    screen.textFAB = stringResource(id = R.string.product_text_fab)
    screen.onClickFAB = { uiState.triggerRunOnClickFAB.value = true}
    log(true, "ArticleScreenInitDate")
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
    val offsetHeightPx = remember { mutableFloatStateOf(0f) }

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
            sections = mutableStateOf(uiState.sections),
        ) as BottomSheetInterface)
    }
    if (deleteSelected.value) {
        uiState.doDeleteSelected( uiState.articles.flatten() )
        deleteSelected.value = false
    }

    Box(Modifier.fillMaxSize() ) {
        Column(Modifier.fillMaxHeight()) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .animatedScroll(
                        height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                        offsetHeightPx = offsetHeightPx
                    )
                    .weight(1f)) {
                ArticleLazyColumn(
                    uiState = uiState,
                    scrollOffset =-offsetHeightPx.floatValue.roundToInt(),
                    doSelected = { idItem -> isSelectedId.value = idItem },)
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
) {
    val listState = rememberLazyListState()

    if (uiState.editArticle.value != null) BottomSheetArticleEdit(uiState)
    val listItems:List<List<Article>> = uiState.articles
    CollapsingToolbar(
        text = uiState.screenTextHeader,
        idImage = uiState.idImage,
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
    ){
        items( items = listItems ) {item ->
        Column( modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .animateItemPlacement()
            .background(
                if (uiState.articles.size == 1) colorApp.tertiaryContainer
                else {
                    if (item[0].section.colorSection != 0L) Color(item[0].section.colorSection)
                    else colorApp.tertiaryContainer
                }
            )
        ){//SectionColor
            HeaderSection(
                modifier = Modifier.padding(top = Dimen.lazyPaddingVer),
                text = if ( uiState.sorting == SortingBy.BY_SECTION) item[0].section.nameSection
                        else stringResource(id = R.string.all))
            ArticleLayoutColum(
                modifier = Modifier,
                articles = item,
                doEditArticle = { article -> uiState.editArticle.value = article },
                doSelected = doSelected,
                doDelete = { items -> uiState.doDeleteSelected( items ) })
            }
        }
    }
}
@Composable
fun ArticleLayoutColum(
    modifier: Modifier,
    articles: List<Article>,
    doEditArticle: (Article) -> Unit,
    doSelected: (Long) -> Unit,
    doDelete: (List<Article>) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(vertical = Dimen.lazyPaddingVer)
    ) {
        for (article in articles){
            key(article.idArticle){
                ItemSwipe(
                    frontFon = { ElementColum( modifier, article, doSelected ) },
                    actionDragLeft = {
                        article.isSelected = true
                        doDelete(mutableListOf(article))},
                    actionDragRight = { doEditArticle( article ) },
                )
            }
        }
    }
}
@Composable
fun ElementColum(modifier: Modifier, item: Article, doSelected: (Long)->Unit)
{
    val localDensity = LocalDensity.current
    var heightIs by remember { mutableStateOf(0.dp) }

    Box (
        modifier.padding(horizontal = Dimen.lazyPaddingHor)
            .onGloballyPositioned { coordinates ->
                heightIs = with(localDensity) { coordinates.size.height.toDp() } })
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(color = colorApp.surface)
                .clickable { doSelected(item.idArticle) }
        ) {
            Spacer( modifier = modifier
                .width(Dimen.widthIndicatorSelect)
                .height(heightIs)
                .background(if (item.isSelected) Color.Red else Color.LightGray)
                .align(Alignment.CenterVertically)
            )
            TextApp (text = item.nameArticle,
                textAlign = TextAlign.Left,
                modifier = modifier
                    .weight(1f)
                    .padding(start = Dimen.lazyPaddingHor)
                    .padding(vertical = Dimen.lazyPaddingVer),
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

