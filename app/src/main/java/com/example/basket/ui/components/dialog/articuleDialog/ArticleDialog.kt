package com.example.basket.ui.components.dialog.articuleDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Article
import com.example.basket.entity.Section
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.UnitApp
import com.example.basket.ui.components.ChipsSections
import com.example.basket.ui.components.ChipsUnit
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.theme.Dimen

@Composable
fun EditArticleDialog(
    article: Article,
    listUnit: List<UnitApp>,
    listSection: List<Section>,
    onConfirm: (Article) -> Unit,
    onDismiss: () -> Unit,)
{
    val articleLocal = remember{ mutableStateOf(article) }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.small,
        confirmButton = { TextButtonOK( onConfirm = { onConfirm( articleLocal.value) } ) },
        text = { EditArticleDialogLayout( articleLocal, listUnit, listSection ) },
        title = {  MyTextH2(stringResource(R.string.change_article)) },
    )
}

@Composable
fun EditArticleDialogLayout(
    article: MutableState<Article>, listUnit: List<UnitApp>, listSection: List<Section>,){
    Column {
        Text(text = "")
        LayoutAddEditArticle(article = article, listUnit = listUnit, listSection = listSection)
    }
}

@Composable
fun LayoutAddEditArticle(
    article: MutableState<Article>,
    listUnit: List<UnitApp>,
    listSection: List<Section>)
{
    val enterNameArticle = remember{ mutableStateOf( article.value.nameArticle )}
    val enterSection = remember{
        mutableStateOf( Pair(article.value.section.idSection,article.value.section.nameSection)) }
    val enterUnit = remember{
        mutableStateOf( Pair(article.value.unitApp.idUnit,article.value.unitApp.nameUnit)) }

    article.value = ArticleDB(
        idArticle = article.value.idArticle,
        nameArticle = enterNameArticle.value,
        position = article.value.position,
        isSelected = article.value.isSelected,
        section =
            if (enterSection.value.first == 0L && enterSection.value.second != "") {
                SectionDB(nameSection = enterSection.value.second, idSection = 0L)
            } else {
                if (listSection.isNotEmpty()) {
                    (listSection.find { it.idSection == enterSection.value.first }
                        ?: listSection[0]) as SectionDB
                } else SectionDB(nameSection = "")
            },
        unitApp =
        if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
            UnitDB(nameUnit = enterUnit.value.second, idUnit = 0L)
        } else {
            if (listUnit.isNotEmpty()) {
                (listUnit.find { it.idUnit == enterUnit.value.first } ?: listUnit[0]) as UnitDB
            } else UnitDB(nameUnit = "")
        },
    )


    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(
            modifier = Modifier.fillMaxWidth(), enterValue = enterNameArticle, TypeKeyboard.TEXT)
        Spacer(Modifier.height(Dimen.bsSpacerHeight))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            /** Select unit*/
            ChipsUnit(
                listUnit = listUnit,
                edit = true,
                unitArticle = article.value.unitApp,
                onClick = { enterUnit.value = Pair(it.idUnit,it.nameUnit)})
            Spacer(Modifier.height(Dimen.bsSpacerHeight))
            /** Select section*/
            ChipsSections(
                edit = true,
                listSection = listSection,
                sectionArticle = article.value.section,
                onClick = { enterSection.value = Pair(it.idSection,it.nameSection)})
        }
    }
}
