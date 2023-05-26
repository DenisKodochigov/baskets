package com.example.shopping_list.ui.components.dialog

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shopping_list.R
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.components.LayoutAddEditArticle
import com.example.shopping_list.ui.components.MyTextH2

@Composable
fun EditArticleDialog(
    article: Article,
    listUnit: List<UnitA>,
    listGroup: List<GroupArticle>,
    onConfirm: (Article) -> Unit,
    onDismiss: () -> Unit,)
{
    val articleLocal = remember{ mutableStateOf(article) }
    Log.d("KDS", "EditArticleDialog")
    AlertDialog(
        onDismissRequest = onDismiss ,
        title = {  MyTextH2(stringResource(R.string.change_article), Modifier) },
        text = { EditArticleDialogLayout(articleLocal, listUnit, listGroup) },
        dismissButton = {
            TextButton(onClick = onDismiss) { MyTextH2(stringResource(R.string.exit), Modifier) } },
        confirmButton = {
            TextButton( onClick = {
                onConfirm(articleLocal.value)
            }
            ) { MyTextH2(stringResource(R.string.ok), Modifier) }
        }
    )
}

@Composable
fun EditArticleDialogLayout( article: MutableState<Article>,
                             listUnit: List<UnitA>,
                             listGroup: List<GroupArticle>,){

    Column() {
        Text(text = "")
        LayoutAddEditArticle(article = article, listUnit = listUnit, listGroup = listGroup)
    }
}
