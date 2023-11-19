package com.example.basket.ui.bottomsheets.bottomSheetsOld
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.BottomAppBarDefaults
//import androidx.compose.material3.BottomSheetDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.contentColorFor
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.res.stringResource
//import com.example.basket.R
//import com.example.basket.data.room.tables.ArticleDB
//import com.example.basket.data.room.tables.SectionDB
//import com.example.basket.data.room.tables.UnitDB
//import com.example.basket.entity.TagsTesting
//import com.example.basket.ui.components.ChipsSections
//import com.example.basket.ui.components.ChipsUnit
//import com.example.basket.ui.components.HeaderScreen
//import com.example.basket.ui.components.MyExposedDropdownMenuBox
//import com.example.basket.ui.components.TextButtonOK
//import com.example.basket.ui.screens.article.ArticleScreenState
//import com.example.basket.ui.theme.Dimen
//import com.example.basket.utils.selectSectionWithArticle
//import com.example.basket.utils.selectUnitWithArticle
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BottomSheetArticleAdd(uiState: ArticleScreenState)
//{
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true,
//        confirmValueChange = { true },)
//    val nameSection = stringResource(R.string.name_section)
//    val stuff = stringResource(R.string.unit_st)
//    val enterValue = remember { mutableStateOf("1") }
//    val enterArticle = remember { mutableStateOf(Pair<Long, String>(0, "")) }
//    val enterSection = remember { mutableStateOf(Pair<Long, String>(1, nameSection)) }
//    val enterUnit = remember { mutableStateOf(Pair<Long, String>(1, stuff)) }
//    val listArticle = uiState.articles.flatten()
//
//    ModalBottomSheet(
//        onDismissRequest = {uiState.triggerRunOnClickFAB.value = false},
//        modifier = Modifier
//            .testTag(TagsTesting.BASKETBOTTOMSHEET)
//            .padding(horizontal = Dimen.bsPaddingHor),
//        shape = shapesApp.small,
//        containerColor = BottomSheetDefaults.ContainerColor,
//        contentColor = contentColorFor(BottomAppBarDefaults.containerColor),
//        tonalElevation = BottomSheetDefaults.Elevation,
//        scrimColor = BottomSheetDefaults.ScrimColor,
//        dragHandle = { BottomSheetDefaults.DragHandle() },
//        windowInsets = BottomSheetDefaults.windowInsets,
//        sheetState = sheetState  ) {
//        if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
//            val id: Long =
//                uiState.unitApp.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
//            enterUnit.value = Pair(id, enterUnit.value.second)
//        }
//        if (enterSection.value.first == 0L && enterSection.value.second != "") {
//            val id: Long =
//                uiState.sections.find { it.nameSection == enterSection.value.second }?.idSection ?: 0L
//            enterSection.value = Pair(id, enterSection.value.second)
//        }
//        if (enterArticle.value.first == 0L && enterArticle.value.second != "") {
//            val id: Long =
//                listArticle.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
//            enterArticle.value = Pair(id, enterArticle.value.second)
//        }
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .padding(horizontal = Dimen.bsItemPaddingHor)
////                .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.85).dp)
//        ) {
//            HeaderScreen(text = stringResource(R.string.add_product))
//            Spacer(Modifier.height(Dimen.bsSpacerHeight))
//            /** Select article*/
//            /** Select article*/
//            /** Select article*/
//            /** Select article*/
//            MyExposedDropdownMenuBox(
//                listItems = emptyList(), //listArticle.map { Pair(it.idArticle, it.nameArticle) }.sortedBy { it.second },
//                label = stringResource(R.string.select_product),
//                modifier = Modifier.fillMaxWidth(),
//                enterValue = enterArticle,
//                filtering = true
//            )
//            if (enterArticle.value.first > 0) {
//                enterSection.value = selectSectionWithArticle(enterArticle.value.first, listArticle)
//                enterUnit.value = selectUnitWithArticle(enterArticle.value.first, listArticle)
//                enterValue.value = "1"
//            }
//            Spacer(Modifier.height(Dimen.bsSpacerHeight))
//            ChipsUnit(
//                listUnit = uiState.unitApp,
//                edit = true,
//                unitArticle = listArticle.find { it.idArticle == enterArticle.value.first }?.unitApp,
//                onClick = { enterUnit.value = Pair(it.idUnit,it.nameUnit)})
//            Spacer(Modifier.height(Dimen.bsSpacerHeight))
//            /** Select section*/
//            /** Select section*/
//            /** Select section*/
//            /** Select section*/
//            ChipsSections(
//                edit = true,
//                listSection = uiState.sections,
//                sectionArticle = listArticle.find { it.idArticle == enterArticle.value.first }?.section,
//                onClick = { enterSection.value = Pair(it.idSection,it.nameSection)})
//
//            TextButtonOK(
//                enabled = enterArticle.value.second != "",
//                onConfirm = {
//                    uiState.onAddArticle(
//                        ArticleDB(
//                            idArticle = enterArticle.value.first,
//                            nameArticle = enterArticle.value.second,
//                            section = SectionDB(
//                                idSection = enterSection.value.first,
//                                nameSection = enterSection.value.second),
//                            unitApp = UnitDB(enterUnit.value.first, enterUnit.value.second),
//                            isSelected = false,
//                            position = 0
//                        )
//                    )
//                    enterArticle.value = Pair(0, "")
//                }
//            )
//            Spacer(Modifier.height(Dimen.bsSpacerHeight2))
//        }
//    }
//}