package com.example.shopping_list.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Article
import com.example.shopping_list.ui.theme.BackgroundBottomBar

@Composable
fun HeaderScreen(text: String, modifier: Modifier){
    val typography = MaterialTheme.typography
    Row( modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text, style = typography.h1)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable fun MyExposedDropdownMenuBox(
    listItems: List<Pair<Long,String>>,
    label: String?,
    modifier: Modifier,
    filtering: Boolean,
    enterValue: MutableState<Pair<Long,String>>?,
    readOnly: Boolean = false,) {

    var focusItem by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }
    enteredText = if (enterValue != null && !focusItem) enterValue.value.second else ""
    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = modifier,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(1f).onFocusChanged { focusItem = it.isFocused},
            value = enteredText,
            singleLine = true,
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.h1,
            onValueChange = {
                enteredText = it
                focusItem = false
                enterValue!!.value = Pair(0,it)
                expanded = true
            },
            label = { if( label != null) Text(text = label, style = MaterialTheme.typography.h3) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    enterValue!!.value = Pair(0,enteredText)
                    expanded = false
                }
            ) ,
        )
        var filteringOptions = listItems
        if (filtering) filteringOptions = listItems.filter{ it.second.contains(enteredText, ignoreCase = true)}
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }
            ) {
                filteringOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            enteredText = item.second
                            expanded = false
                            enterValue!!.value = item
                            localFocusManager.clearFocus()
                        }
                    ) {
                        Text(text = item.second, style = MaterialTheme.typography.h1)
                    }
                }
            }
        } else expanded = false
    }
}

@Composable
fun MyTextH1(text: String, modifier: Modifier){
    Text(
        text = text,
        style = MaterialTheme.typography.h1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun MyTextH2(text: String, modifier: Modifier){
    Text(
        text = text,
        style = MaterialTheme.typography.h2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}
@Composable
fun MyTextH1End(text: String, modifier: Modifier){
    Text(
        text = text,
        style = MaterialTheme.typography.h1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        textAlign = TextAlign.End
    )
}

@Composable
fun TextButtonOK(onConfirm: ()->Unit, enabled: Boolean = true){
    Column( horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth() ) {
        TextButton(onClick = onConfirm, enabled = enabled) { MyTextH2(stringResource(R.string.ok), Modifier) }
    }
}

@Composable
fun MyOutlinedTextFieldWithoutIcon(
    modifier: Modifier, enterValue: MutableState<String>, typeKeyboard: String){

    val localFocusManager = LocalFocusManager.current
    var enterText by remember { mutableStateOf("") }
    enterText = enterValue.value
//    val keyboardController = LocalSoftwareKeyboardController.current
    var keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done)
    if (typeKeyboard == "text") keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

    OutlinedTextField(
        modifier = modifier,
        value = enterText,
        singleLine = true,
        textStyle = MaterialTheme.typography.h1,
        onValueChange = {
            enterText = it
            enterValue.value = it },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
                enterValue.value = enterText
//                keyboardController?.hide()
            }
        ) ,
//        leadingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) },
//        trailingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) }
    )
}

@Composable
fun MyOutlinedTextFieldWithoutIconClearing(modifier: Modifier, enterValue: MutableState<String>, typeKeyboard: String){

    val localFocusManager = LocalFocusManager.current
    var focusItem by remember { mutableStateOf(false) }
    var enterText by remember { mutableStateOf("") }
    enterText = if (!focusItem) enterValue.value else  ""
//    val keyboardController = LocalSoftwareKeyboardController.current
    var keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done)
    if (typeKeyboard == "text") keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

    OutlinedTextField(
        modifier = modifier.onFocusChanged { focusItem = it.isFocused},
        value = enterText,
        singleLine = true,
        textStyle = MaterialTheme.typography.h1,
        onValueChange = {
            focusItem = false
            enterText = it
            enterValue.value = it },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
                enterValue.value = enterText
//                keyboardController?.hide()
            }
        ) ,
//        leadingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) },
//        trailingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) }
    )
}
@Composable
fun ButtonSwipe( sortingList: (Int) -> Unit ) {
//    Log.d("KDS", "ButtonSwipeArticle")
    Row(Modifier.fillMaxWidth()) {
        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowDownward) { sortingList(1) }
//        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.lazy_padding_hor)))
//        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowUpward) { sortingList(-1) }
    }
}

@Composable
fun ButtonMove(modifier: Modifier, icon: ImageVector, onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier,   //.height(36.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = BackgroundBottomBar)) {
        Icon(icon, contentDescription = null)
    }
}

@Composable
fun ButtonMy(modifier: Modifier, nameButton: String, onClick: () -> Unit){
    OutlinedButton(
        modifier = modifier,
        elevation = ButtonDefaults.elevation(),
        onClick = { onClick() }){
        Text(text = nameButton, fontSize = 25.sp)
    }
}


@Composable
fun selectGroupWithArticle (id: Long, listArticle: List<Article>): Pair<Long, String>{
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) { Pair(article.group.idGroup, article.group.nameGroup) }
            else Pair(0L,"")
}

@Composable
fun selectUnitWithArticle (id: Long, listArticle: List<Article>): Pair<Long, String>{
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) Pair(article.unitA.idUnit, article.unitA.nameUnit)
        else Pair(0L,"")
}

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun onAddIconEditText( onNewArticle: (String) -> Unit, nameNewArticle: String): String {
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val localFocusManager = LocalFocusManager.current
//    Icon(
//        Icons.Filled.Add,
//        contentDescription = "Add",
//        Modifier.clickable(
//            onClick = {
//                keyboardController?.hide()
//                onNewArticle(nameNewArticle)
//                localFocusManager.clearFocus() }
//        )
//    )
//    return ""
//}