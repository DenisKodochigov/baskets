package com.example.shopping_list.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Article


@Composable
fun HeaderScreen(text: String){
    val typography = MaterialTheme.typography
    Row( Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text, style = typography.h1)
    }
}
@Composable
fun HeaderScreen1(text: String, onClick: () -> Unit){
    val typography = MaterialTheme.typography
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick }, horizontalArrangement = Arrangement.Center){
        Text(text, style = typography.h1)
    }
}
@Composable
fun BasketsRow(modifier: Modifier = Modifier, name: String,){

    Spacer(
        Modifier
            .width(360.dp)
            .size(360.dp, 4.dp)
//            .background(color = color)
            .padding(bottom = 12.dp))
    Row(
        modifier = modifier.height(68.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography

        Column(Modifier) {
            Text(text = name, style = typography.body1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = name, style = typography.subtitle1)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.width(16.dp))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            )
        }
    }
    com.example.shopping_list.archiv.RallyDivider()
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable fun MyExposedDropdownMenuBox(
    listItems: List<Pair<Long,String>>,
    label: String?,
    modifier: Modifier,
    onSelectItem: (Pair<Long,String>) -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = modifier,
        onExpandedChange = { expanded = !expanded }){
        OutlinedTextField(
            value = selectedText,
            singleLine = true,
            textStyle = TextStyle(fontSize =  20.sp),
            onValueChange = { selectedText = it },
            label = { if( label != null) Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSelectItem(Pair(0,selectedText))
                    keyboardController?.hide()
                    localFocusManager.clearFocus()
                }
            ) ,
        )
        val filteringOptions = listItems.filter { it.second.contains(selectedText, ignoreCase = true) }
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {}
//                onDismissRequest = { expanded = false }
            ) {
                filteringOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedText = item.second
                            expanded = false
                            onSelectItem(item)
                            keyboardController?.hide()
                            localFocusManager.clearFocus()
                        }
                    ) {
                        Text(text = item.second)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyExposedDropdownMenuBoxPreview(){
    MyExposedDropdownMenuBox(emptyList(),"label", Modifier.width(220.dp),{})
}


@Composable
fun ListArticle(itemList: List<Article>, onAddClick: (Long, Double) -> Unit){
    val listState = rememberLazyListState()
    LazyColumn (state = listState, modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)) {
        items(items = itemList){ item->
            Row ( modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 2.dp, end = 24.dp, bottom = 2.dp)
                .background(Color.White)
                .clickable {
                    //Показать диалог с выбором значения
                    onAddClick(item.idArticle, 0.0)
                }){
                Text(text = item.nameArticle,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditTextNewArticle(onNewArticle: (String) -> Unit){

    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    var nameNewArticle by remember { mutableStateOf("") }
    val pb = 0.dp
    val modifier = Modifier.padding(start = pb, top = pb, end = pb, bottom = pb).fillMaxWidth()
    OutlinedTextField(
        value = nameNewArticle,
        singleLine = true,
        textStyle = TextStyle(fontSize =  20.sp),
        label = { Text(text = "New article") },
        onValueChange = { nameNewArticle = it},
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onNewArticle(nameNewArticle)
                keyboardController?.hide()
                localFocusManager.clearFocus()
                nameNewArticle = ""
            }
        ) ,
        leadingIcon = { nameNewArticle = onAddIconEditText(onNewArticle,nameNewArticle) },
        trailingIcon = { nameNewArticle = onAddIconEditText(onNewArticle,nameNewArticle) }
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun onAddIconEditText(onNewArticle: (String) -> Unit, nameNewArticle: String): String {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    Icon(
        Icons.Filled.Add,
        contentDescription = "Add",
        Modifier.clickable(
            onClick = {
                keyboardController?.hide()
                onNewArticle(nameNewArticle)
                localFocusManager.clearFocus() }
        )
    )
    return ""
}
