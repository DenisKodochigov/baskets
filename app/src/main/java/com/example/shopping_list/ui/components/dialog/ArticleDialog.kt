package com.example.shopping_list.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ArticleClass
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditArticleDialog(
    article: Article,
    listUnit: List<UnitA>,
    listSection: List<Section>,
    onConfirm: (Article) -> Unit,
    onDismiss: () -> Unit,)
{
    val articleLocal = remember{ mutableStateOf(article) }
    AlertDialog(
        onDismissRequest = onDismiss ,
        title = {  MyTextH2(stringResource(R.string.change_article), Modifier) },
        text = { EditArticleDialogLayout(articleLocal, listUnit, listSection) },
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(articleLocal.value) } ) }
    )
}

@Composable
fun EditArticleDialogLayout(
    article: MutableState<Article>, listUnit: List<UnitA>, listSection: List<Section>,){
    Column {
        Text(text = "")
        LayoutAddEditArticle(article = article, listUnit = listUnit, listSection = listSection)
    }
}

@Composable
fun LayoutAddEditArticle(
    article: MutableState<Article>,
    listUnit: List<UnitA>,
    listSection: List<Section>)
{
//    Log.d("KDS", "LayoutAddEditArticle")
    val enterNameArticle = remember{ mutableStateOf( article.value.nameArticle )}
    val enterSection = remember{
        mutableStateOf( Pair(article.value.section.idSection,article.value.section.nameSection)) }
    val enterUnit = remember{
        mutableStateOf( Pair(article.value.unitA.idUnit,article.value.unitA.nameUnit)) }

    article.value = ArticleClass(
        idArticle = article.value.idArticle,
        nameArticle = enterNameArticle.value,
        unitA = if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
            UnitEntity(nameUnit = enterUnit.value.second, idUnit = 0L)
        } else {
            if (listUnit.isNotEmpty()) {
                listUnit.find { it.idUnit == enterUnit.value.first } ?: listUnit[0]}
            else UnitEntity(nameUnit = "")
        },
        section = if (enterSection.value.first == 0L && enterSection.value.second != "") {
            SectionEntity(nameSection = enterSection.value.second, idSection = 0L)
        } else {
            if (listSection.isNotEmpty()) {
                listSection.find { it.idSection == enterSection.value.first } ?: listSection[0]}
            else SectionEntity(nameSection = "")
        },
        isSelected = article.value.isSelected,
        position = article.value.position,
    )


    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = enterNameArticle, "text")
        Spacer(Modifier.height(12.dp))
        /** Select section*/
        MyExposedDropdownMenuBox(
            listItems = listSection.map{ Pair(it.idSection, it.nameSection) },
            label = stringResource(R.string.section),
            modifier = Modifier.fillMaxWidth(), //.weight(1f),
            enterValue = enterSection,
            filtering = true)
//        Spacer(Modifier.width(4.dp))
        Spacer(Modifier.height(12.dp))
        /** Select unit*/
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            MyExposedDropdownMenuBox(
                listItems = listUnit.map{ Pair(it.idUnit, it.nameUnit) },
                label = stringResource(R.string.units),
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false,
                readOnly = true)
        }
    }
}
