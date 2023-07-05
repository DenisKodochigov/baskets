package com.example.shopping_list.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.SortingBy
import com.example.shopping_list.ui.theme.ButtonColorsMy
import com.example.shopping_list.ui.theme.ScaffoldColor


@Composable
fun HeaderScreen(text: String, modifier: Modifier) {
    val typography = MaterialTheme.typography
    Spacer(modifier = Modifier.height(24.dp))
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text, style = typography.h1)
    }
}

@Composable
fun HeaderImScreen(text: String, idImage:Int ) {

    Box(Modifier.fillMaxWidth().height(240.dp) ){
        Image(
            painter = painterResource(id = idImage),
            contentDescription = "Photo",
            contentScale = FillBounds,
            modifier = Modifier.align(alignment = Alignment.Center))
        Box(modifier = Modifier.background(ScaffoldColor, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .align(alignment = Alignment.BottomCenter)) {
            Text( text = text, style = MaterialTheme.typography.h1 )
        }
    }
}
@Composable
fun HeaderSection(text: String, modifier: Modifier) {
    val typography = MaterialTheme.typography
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 12.dp), horizontalArrangement = Arrangement.Start) {
        Text(text, style = typography.h2)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyExposedDropdownMenuBox(
    listItems: List<Pair<Long, String>>,
    label: String?,
    modifier: Modifier,
    filtering: Boolean,
    enterValue: MutableState<Pair<Long, String>>?,
    readOnly: Boolean = false,
    enabled: Boolean = true
) {

    var focusItem by remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current
    var expandedLocal by remember { mutableStateOf(false) }
    var enteredText by remember { mutableStateOf("") }
    enteredText = if (enterValue != null && !focusItem) enterValue.value.second else ""
    ExposedDropdownMenuBox(
        expanded = expandedLocal && enabled,
        modifier = modifier,
        onExpandedChange = { expandedLocal = !expandedLocal }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .onFocusChanged { focusItem = it.isFocused },
            value = enteredText,
            singleLine = true,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = MaterialTheme.typography.h1,
            onValueChange = {
                enteredText = it
                focusItem = false
                enterValue!!.value = Pair(0, it)
                expandedLocal = true
            },
            label = { if (label != null) Text(text = label, style = MaterialTheme.typography.h3) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocal) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    enterValue!!.value = Pair(0, enteredText)
                    expandedLocal = false
                }
            ),
        )
        var filteringOptions = listItems
        if (filtering) filteringOptions =
            listItems.filter { it.second.contains(enteredText, ignoreCase = true) }
        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expandedLocal && enabled,
                onDismissRequest = { expandedLocal = false }) {
                filteringOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            enteredText = item.second
                            expandedLocal = false
                            enterValue!!.value = item
                            localFocusManager.clearFocus()
                        }
                    ) {
                        Text(text = item.second, style = MaterialTheme.typography.h1)
                    }
                }
            }
        } else expandedLocal = false
    }
}

@Composable
fun MyTextH1(text: String, modifier: Modifier, textAlign: TextAlign) {
    Text(
        text = text,
        style = MaterialTheme.typography.h1,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun MyTextH2(text: String, modifier: Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.h2,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun MyTextH1End(text: String, modifier: Modifier) {
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
fun TextButtonOK(onConfirm: () -> Unit, enabled: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onConfirm, enabled = enabled) {
            MyTextH2(
                stringResource(R.string.ok),
                Modifier
            )
        }
    }
}

@Composable
fun MyOutlinedTextFieldWithoutIcon(
    modifier: Modifier, enterValue: MutableState<String>, typeKeyboard: String
) {

    val localFocusManager = LocalFocusManager.current
    var enterText by remember { mutableStateOf(enterValue.value) }
//    enterText = enterValue.value
//    val keyboardController = LocalSoftwareKeyboardController.current
    var keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done)
    if (typeKeyboard == "text") keyboardOptions =
        KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

    OutlinedTextField(
        modifier = modifier,
        value = enterText,
        singleLine = true,
        textStyle = MaterialTheme.typography.h1,
        onValueChange = {
            enterText = it
            enterValue.value = it
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
                enterValue.value = enterText
//                keyboardController?.hide()
            }
        ),
//        leadingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) },
//        trailingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) }
    )
}

@Composable
fun MyOutlinedTextFieldWithoutIconClearing(
    modifier: Modifier,
    enterValue: MutableState<String>,
    typeKeyboard: String
) {

    val localFocusManager = LocalFocusManager.current
    var focusItem by remember { mutableStateOf(false) }
    var enterText by remember { mutableStateOf("") }
    enterText = if (!focusItem) enterValue.value else ""
//    val keyboardController = LocalSoftwareKeyboardController.current
    var keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done)
    if (typeKeyboard == "text") keyboardOptions =
        KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

    OutlinedTextField(
        modifier = modifier.onFocusChanged { focusItem = it.isFocused },
        value = enterText,
        singleLine = true,
        textStyle = MaterialTheme.typography.h1,
        onValueChange = {
            focusItem = false
            enterText = it
            enterValue.value = it
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                localFocusManager.clearFocus()
                enterValue.value = enterText
//                keyboardController?.hide()
            }
        ),
//        leadingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) },
//        trailingIcon = { enterText = onAddIconEditText(onNewArticle,enterText) }
    )
}

@Composable fun ButtonCircle( modifier: Modifier, iconButton: ImageVector, onClick: () -> Unit) {
    val radius = 25.dp
    IconButton (
        modifier = modifier
            .clip(RoundedCornerShape(radius, radius, radius, radius))
            .size(60.dp),
        onClick = { onClick() }) {
        Icon( imageVector = iconButton, null, tint = ButtonColorsMy , modifier = Modifier.size(60.dp))
    }
}


@Composable fun selectSectionWithArticle(id: Long, listArticle: List<Article>): Pair<Long, String> {
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) {
        Pair(article.section.idSection, article.section.nameSection)
    } else Pair(0L, "")
}

@Composable fun selectUnitWithArticle(id: Long, listArticle: List<Article>): Pair<Long, String> {
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) Pair(article.unitApp.idUnit, article.unitApp.nameUnit)
    else Pair(0L, "")
}

@Composable fun showFABs(startScreen: Boolean, isSelected: Boolean, modifier: Modifier, doDeleted: ()->Unit,
             doChangeSection: ()->Unit, doUnSelected:()->Unit):Boolean {
    var startScreenLocal = startScreen
    if (isSelected) {
        startScreenLocal = true
        ShowFABs_(show = true,
            modifier = modifier,
            doDeleted = doDeleted,
            doChangeSection = doChangeSection,
            doUnSelected = doUnSelected)
    } else if (startScreenLocal){
        ShowFABs_(show = false, doDeleted = {}, doChangeSection = {}, doUnSelected = {}, modifier = modifier)
    }
    return startScreenLocal
}

@Composable fun ShowFABs_(show: Boolean, modifier: Modifier, doDeleted: ()->Unit, doChangeSection: ()->Unit, doUnSelected:()->Unit){
    Box( modifier ) {
        FabAnimation(show = show, offset = 0.dp, icon = Icons.Filled.Delete, onClick = doDeleted)
        FabAnimation(show = show, offset = 64.dp, icon = Icons.Filled.Dns, onClick = doChangeSection)
        FabAnimation(show = show, offset = 128.dp, icon = Icons.Filled.RemoveDone, onClick = doUnSelected)
    }
}

@Composable fun SwitchSorting(doChangeSorting: (SortingBy) -> Unit){
    Row( Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically )
    {
        val checkedState = remember { mutableStateOf(false) }
        Text(text = stringResource(R.string.by_name), style = MaterialTheme.typography.h3 )
        Spacer(Modifier.width(30.dp))
        Switch(
            checked = checkedState.value,
            modifier = Modifier
                .height(12.dp)
                .width(30.dp),
            onCheckedChange = {
                checkedState.value = it
                if (checkedState.value) doChangeSorting(SortingBy.BY_SECTION)
                else doChangeSorting( SortingBy.BY_NAME )
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF5E5E5E),
                checkedTrackColor = Color(0xFF919191),
                uncheckedThumbColor = Color(0xFF5E5E5E),
                uncheckedTrackColor = Color(0xFF919191)
            ))
        Spacer(Modifier.width(30.dp))
        Text(text = stringResource(R.string.by_section), style = MaterialTheme.typography.h3 )
    }
}

