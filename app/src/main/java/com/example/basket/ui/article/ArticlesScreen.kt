package com.example.basket.ui.article

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
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Article
import com.example.basket.entity.SizeElement
import com.example.basket.entity.SortingBy
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TypeText
import com.example.basket.navigation.ScreenDestination
import com.example.basket.ui.components.*
import com.example.basket.ui.components.dialog.EditArticleDialog
import com.example.basket.ui.components.dialog.SelectSectionDialog
import com.example.basket.ui.theme.SectionColor
import com.example.basket.ui.theme.getIdImage
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.DismissBackground
import com.example.basket.utils.bottomBarAnimatedScroll
import com.example.basket.utils.log
import com.example.basket.utils.selectSectionWithArticle
import com.example.basket.utils.selectUnitWithArticle
import kotlin.math.roundToInt

const val showLog = false
@Composable
fun ArticlesScreen(showBottomSheet: MutableState<Boolean>,
                   screen: ScreenDestination,) {
    val viewModel: ArticleViewModel = hiltViewModel()
    viewModel.getStateArticle()

    ArticleScreenInitDate(viewModel = viewModel, screen = screen,showBottomSheet = showBottomSheet)
}

@Composable
fun ArticleScreenInitDate(
    viewModel: ArticleViewModel,
    screen: ScreenDestination,
    showBottomSheet: MutableState<Boolean>,
) {
    log( showLog,"ArticleScreenInitDate")
    val uiState by viewModel.articleScreenState.collectAsState()
    if (showBottomSheet.value)
        AddEditArticleBottomSheet(
            uiState = uiState,
            onAddArticle = { article -> viewModel.addArticle(article, uiState.sorting) },
            onDismiss = { showBottomSheet.value = false})
    ArticleScreenLayout(
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.screen_padding_hor)),
        screen = screen,
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
fun ArticleScreenLayout(
    modifier: Modifier = Modifier,
    uiState: ArticleScreenState,
    screen: ScreenDestination,
    changeArticle: (Article) -> Unit,
    doChangeSectionSelected: (List<Article>, Long) -> Unit,
    doDeleteSelected: (List<Article>) -> Unit,
    doChangeSortingBy: (SortingBy) -> Unit,
    doSelected: (Long) -> Unit
) {
    log( showLog,"LayoutArticleScreen")
    val isSelectedId: MutableState<Long> = remember { mutableLongStateOf(0L) }
    val deleteSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val unSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    val changeSectionSelected: MutableState<Boolean> = remember { mutableStateOf(false) }
    var startScreen by remember { mutableStateOf(false) } // Индикатор первого запуска окна
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

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
                    .bottomBarAnimatedScroll(
                        height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                        offsetHeightPx = bottomBarOffsetHeightPx)
                    .weight(1f)) {
                ArticleLazyColumn(
                    uiState = uiState,
                    screen = screen,
                    changeArticle = changeArticle,
                    scrollOffset =-bottomBarOffsetHeightPx.floatValue.roundToInt(),
                    doSelected = { idItem -> isSelectedId.value = idItem },
                    doDelete = { items -> doDeleteSelected( items ) })
            }
            log(showLog,"LayoutArticleScreen.SwitcherButton")
            SwitcherButton(doChangeSortingBy)
        }
        startScreen = showFABs(
            startScreen = startScreen,
            isSelected =  uiState.article.flatten().find { it.isSelected } != null,
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
    screen: ScreenDestination,
    scrollOffset:Int,
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
    CollapsingToolbar(
        text = stringResource(screen.textHeader),
        idImage = getIdImage(screen),
        scrollOffset = scrollOffset)
    Spacer(modifier = Modifier.height(2.dp))
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    ) {
        items( items = uiState.article )
        { item ->
            Column( modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (uiState.article.size == 1 ) SectionColor
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
                        } else if (it == DismissValue.DismissedToEnd) { editArticle( article ) }
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
    val localDensity = LocalDensity.current
    var heightIs by remember { mutableStateOf(0.dp) }
//    val modifier = modifier.padding(vertical = dimensionResource(R.dimen.lazy_padding_ver))
    
    Box (modifier
        .padding(horizontal = 6.dp)
        .onGloballyPositioned { coordinates ->
            heightIs = with(localDensity) { coordinates.size.height.toDp() } })
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
                modifier = modifier.weight(1f).padding(start = 6.dp).padding(vertical = 6.dp),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SETTING)
            )
            Spacer(modifier = modifier.width(4.dp))
            TextApp (text = item.unitApp.nameUnit,
                textAlign = TextAlign.Left,
                modifier = modifier.width(40.dp),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SETTING)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditArticleBottomSheet(uiState: ArticleScreenState, onAddArticle: (Article) -> Unit, onDismiss:() -> Unit )
{
    log( showLog,"BottomSheetContentArticle")
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true },)
    val nameSection = stringResource(R.string.name_section)
    val stuff = stringResource(R.string.unit_st)
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember { mutableStateOf("1") }
    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, stuff)) }
    val listArticle = uiState.article.flatten()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .testTag(TagsTesting.BASKETBOTTOMSHEET)
            .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_padding_hor)),
        shape = MaterialTheme.shapes.small,
        containerColor = BottomSheetDefaults.ContainerColor,
        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
        tonalElevation = BottomSheetDefaults.Elevation,
        scrimColor = BottomSheetDefaults.ScrimColor,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        windowInsets = BottomSheetDefaults.windowInsets,
        sheetState = sheetState  ) {
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
                .padding(horizontal = dimensionResource(id = R.dimen.bottom_sheet_item_padding_hor),
                    vertical = dimensionResource(id = R.dimen.bottom_sheet_item_padding_ver))
                .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.85).dp)
        ) {
            HeaderScreen(text = stringResource(R.string.add_product))
//        Spacer(Modifier.height(24.dp))
            MyExposedDropdownMenuBox(
                /** Select article*/
                listItems = listArticle.map { Pair(it.idArticle, it.nameArticle) }.sortedBy { it.second },
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
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height)))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                MyExposedDropdownMenuBox(
                    /** Select section*/
                    listItems = uiState.sections.map { Pair(it.idSection, it.nameSection) }.sortedBy { it.second },
                    label = stringResource(R.string.section),
                    modifier = Modifier.weight(1f),
                    enterValue = enterSection,
                    filtering = false
                )
                Spacer(Modifier.width(4.dp))
                MyExposedDropdownMenuBox(
                    /** Select unit*/
                    listItems = uiState.unitApp.map { Pair(it.idUnit, it.nameUnit) }.sortedBy { it.second },
                    label = stringResource(R.string.units),
                    modifier = Modifier.width(dimensionResource(id = R.dimen.bottom_sheet_unit_width)),
                    enterValue = enterUnit,
                    filtering = false
                )
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_1)))

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
            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_sheet_spacer_height_2)))
        }
    }
}
@Preview
@Composable fun ElementColumPreview(){
    ElementColum(modifier = Modifier,
        item = ArticleDB(nameArticle = "Moloko", sectionId = 1L, unitId = 1L) as Article,
        doSelected = {})
}

