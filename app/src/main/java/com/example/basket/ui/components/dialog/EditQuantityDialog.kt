package com.example.basket.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Product
import com.example.basket.entity.TypeKeyboard
import com.example.basket.ui.components.MyOutlinedTextFieldWithoutIconClearing
import com.example.basket.ui.components.MyTextH2
import com.example.basket.ui.components.TextButtonOK
import com.example.basket.ui.theme.shapesApp

@Composable
fun EditQuantityDialog(
    product: Product,
    onConfirm: (Product) -> Unit,
    onDismiss: () -> Unit,
) {
    val enterUnit = remember {
        mutableStateOf(Pair(product.article.unitApp.idUnit, product.article.unitApp.nameUnit)) }
    val enterValue = remember { mutableStateOf(product.value.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = shapesApp.small,
        title = { MyTextH2(stringResource(R.string.change_quantity)) },
        text = { EditQuantityDialogLayout(enterValue) },
        confirmButton = {
            TextButtonOK(
                onConfirm = {
                    onConfirm(
                        ProductDB(
                            idProduct = product.idProduct,
                            basketId = product.basketId,
                            value = enterValue.value.toDouble(),
                            putInBasket = product.putInBasket,
                            position = product.position,
                            isSelected = product.isSelected,
                            articleId = product.article.idArticle,
                            article = ArticleDB(
                                idArticle = product.article.idArticle,
                                nameArticle = product.article.nameArticle,
                                unitApp = UnitDB( idUnit = enterUnit.value.first,nameUnit = enterUnit.value.second),
                                section = product.article.section as SectionDB,
                                isSelected = product.article.isSelected,
                                position = product.article.position,
                            )
                        )
                    )
                }
            )
        }
    )
}

@Composable
fun EditQuantityDialogLayout( enterValue: MutableState<String>
){
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        /** Value*/
        MyOutlinedTextFieldWithoutIconClearing(
            enterValue = enterValue,
            typeKeyboard = TypeKeyboard.TEXT,
            modifier = Modifier.align(Alignment.CenterVertically).width(120.dp).padding(top = 8.dp),
        )
        Spacer(Modifier.width(4.dp))
    }
}



