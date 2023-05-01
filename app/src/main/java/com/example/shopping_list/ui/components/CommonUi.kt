package com.example.shopping_list.ui.components

import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Article

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
    enterValue: MutableState<Pair<Long,String>>?) {

    val localFocusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }

    if (enterValue != null) {
        enteredText = enterValue.value.second
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        modifier = modifier,
        onExpandedChange = {
            expanded = !expanded
        }){
        OutlinedTextField(
            value = enteredText,
            modifier = Modifier.fillMaxWidth(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.h1,
            onValueChange = {
                enteredText = it
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
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
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

@Preview(showBackground = true)
@Composable
fun MyExposedDropdownMenuBoxPreview(){
    MyExposedDropdownMenuBox(emptyList(),"label", Modifier, true, null)
}

@Composable
fun MyOutlinedTextFieldWithoutIcon(modifier: Modifier, enterValue: MutableState<Double>){

    val localFocusManager = LocalFocusManager.current
    var enterText by remember { mutableStateOf("") }
    enterText = enterValue.value.toString()

    OutlinedTextField(
        modifier = modifier,
        value = enterText,
        singleLine = true,
        textStyle = MaterialTheme.typography.h1,
        onValueChange = {
            enterText = it;
            if (it.isNotEmpty()) enterValue.value = it.toDouble()},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
                if (enterText.isNotEmpty()) enterValue.value = enterText.toDouble()
            }
        ) ,
    )
}

@Composable
fun ButtonMy(modifier: Modifier, nameButton: String, onClick: () -> Unit){
    OutlinedButton(modifier = modifier, elevation = ButtonDefaults.elevation(), onClick = {
        onClick()
        Log.d("KDS", "Button.click")
    }){
        Text(text = nameButton, fontSize = 25.sp)
    }
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
fun MyOutlinedTextField(onNewArticle: (String) -> Unit){

    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    var nameNewArticle by remember { mutableStateOf("") }
    val pb = 0.dp
    val modifier = Modifier
        .padding(start = pb, top = pb, end = pb, bottom = pb)
        .fillMaxWidth()
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
