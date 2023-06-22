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
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.MyExposedDropdownMenuBox
import com.example.shopping_list.ui.components.MyOutlinedTextFieldWithoutIcon
import com.example.shopping_list.ui.components.MyTextH2
import com.example.shopping_list.ui.components.TextButtonOK

@Composable
fun EditArticleDialog(
    article: Article,
    listUnit: List<UnitA>,
    listGroup: List<GroupArticle>,
    onConfirm: (Article) -> Unit,
    onDismiss: () -> Unit,)
{
    val articleLocal = remember{ mutableStateOf(article) }
    AlertDialog(
        onDismissRequest = onDismiss ,
        title = {  MyTextH2(stringResource(R.string.change_article), Modifier) },
        text = { EditArticleDialogLayout(articleLocal, listUnit, listGroup) },
        confirmButton = { TextButtonOK( onConfirm = { onConfirm(articleLocal.value) } ) }
    )
}

@Composable
fun EditArticleDialogLayout(
    article: MutableState<Article>, listUnit: List<UnitA>, listGroup: List<GroupArticle>,){
    Column {
        Text(text = "")
        LayoutAddEditArticle(article = article, listUnit = listUnit, listGroup = listGroup)
    }
}

@Composable
fun LayoutAddEditArticle(
    article: MutableState<Article>,
    listUnit: List<UnitA>,
    listGroup: List<GroupArticle>)
{
//    Log.d("KDS", "LayoutAddEditArticle")
    val enterNameArticle = remember{ mutableStateOf( article.value.nameArticle )}
    val enterGroup = remember{
        mutableStateOf( Pair(article.value.group.idGroup,article.value.group.nameGroup)) }
    val enterUnit = remember{
        mutableStateOf( Pair(article.value.unitA.idUnit,article.value.unitA.nameUnit)) }

    article.value.unitA = if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
        UnitEntity(nameUnit = enterUnit.value.second, idUnit = 0L)
    } else {
        if (listUnit.isNotEmpty()) {
            listUnit.find { it.idUnit == enterUnit.value.first } ?: listUnit[0]}
        else UnitEntity(nameUnit = "")
    }

    article.value.group = if (enterGroup.value.first == 0L && enterGroup.value.second != "") {
        GroupEntity(nameGroup = enterGroup.value.second, idGroup = 0L)
    } else {
        if (listGroup.isNotEmpty()) {
            listGroup.find { it.idGroup == enterGroup.value.first } ?: listGroup[0]}
        else GroupEntity(nameGroup = "")
    }
    article.value.nameArticle = enterNameArticle.value

    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = enterNameArticle, "text")
        Spacer(Modifier.height(12.dp))
        /** Select group*/
        MyExposedDropdownMenuBox(
            listItems = listGroup.map{ Pair(it.idGroup, it.nameGroup) },
            label = stringResource(R.string.group),
            modifier = Modifier.fillMaxWidth(), //.weight(1f),
            enterValue = enterGroup,
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
