package com.example.basket.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.SizeElement
import com.example.basket.entity.SortingBy
import com.example.basket.entity.TagsTesting.BUTTON_OK
import com.example.basket.entity.TypeText
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.log

@Composable
fun HeaderScreen(text: String, refreshScreen: MutableState<Boolean> = mutableStateOf(false) ) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        val plug = refreshScreen.value
        TextApp(text = text, style = styleApp(nameStyle = TypeText.NAME_SCREEN))
    }
}

@Composable
fun HeaderSection(text: String, modifier: Modifier, refreshScreen: MutableState<Boolean> = mutableStateOf(false)) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 12.dp), horizontalArrangement = Arrangement.Start) {
        val plug = refreshScreen.value
        TextApp(text, style = styleApp(nameStyle = TypeText.NAME_SECTION))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
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
    val heightDropMenu: Dp = with(LocalDensity.current) {
        (styleApp(nameStyle = TypeText.EDIT_TEXT).fontSize * 1.2).toDp()}
    log(true, "heightDropMenu = $heightDropMenu   fontSize = ${styleApp(nameStyle = TypeText.EDIT_TEXT).fontSize.value}")
    enteredText = if (enterValue != null && !focusItem) enterValue.value.second else ""

    ExposedDropdownMenuBox(
        expanded = expandedLocal && enabled,
        modifier = modifier,
        onExpandedChange = { expandedLocal = !expandedLocal }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(1f)
                .background(color = MaterialTheme.colorScheme.surface)
                .onFocusChanged { focusItem = it.isFocused },
            value = enteredText,
            singleLine = true,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = styleApp(nameStyle = TypeText.EDIT_TEXT),
            onValueChange = {
                enteredText = it
                focusItem = false
                enterValue!!.value = Pair(0, it)
                expandedLocal = true
            },
            label = { if (label != null)
                TextApp(text = label, style = styleApp(nameStyle = TypeText.EDIT_TEXT_TITLE)) },
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
                modifier = Modifier
                    .height(heightDropMenu * 8)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                expanded = expandedLocal && enabled,
                onDismissRequest = { expandedLocal = false }) {
                    filteringOptions.forEach { item ->
                        DropdownMenuItem(
                            onClick = {
                                enteredText = item.second
                                expandedLocal = false
                                enterValue!!.value = item
                                localFocusManager.clearFocus() },
                            text = {
                                TextApp(text = item.second,
                                    style = styleApp(nameStyle = TypeText.EDIT_TEXT))}
                        )
                    }
            }
        } else expandedLocal = false
    }
}
@Composable fun TextApp(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    style: TextStyle,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        style = style,
        maxLines = 1,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface
    )
}
@Composable
fun MyTextH1(text: String, modifier: Modifier, textAlign: TextAlign) {
    TextApp (text = text,
        modifier = modifier,
        style = styleApp(nameStyle = TypeText.TEXT_IN_LIST),
        textAlign = textAlign
    )
}

@Composable
fun MyTextH2(text: String, modifier: Modifier = Modifier) {
    TextApp (text = text,
        modifier = modifier,
        style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SETTING)
    )
}

@Composable
fun TextButtonOK(onConfirm: () -> Unit, enabled: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onConfirm, enabled = enabled) {
            TextApp(
                text = stringResource(R.string.ok),
                modifier = Modifier.testTag(BUTTON_OK),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SETTING)
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
    var keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Decimal).copy(imeAction = ImeAction.Done)
    if (typeKeyboard == "text") keyboardOptions =
        KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

    OutlinedTextField(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface),
        value = enterText,
        singleLine = true,
        textStyle = styleApp(nameStyle = TypeText.EDIT_TEXT),
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
        modifier = modifier
            .onFocusChanged { focusItem = it.isFocused }
            .background(color = MaterialTheme.colorScheme.surface),
        value = enterText,
        singleLine = true,
        textStyle = styleApp(nameStyle = TypeText.EDIT_TEXT),
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
        Icon( imageVector = iconButton, null, tint = MaterialTheme.colorScheme.primary , modifier = Modifier.size(60.dp))
    }
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
    val offset = 8.dp + sizeApp(sizeElement = SizeElement.SIZE_FAB)
    Box( modifier = modifier.height(sizeApp(SizeElement.HEIGHT_FAB_BOX))) {
        FabAnimation(show = show, offset = 0.dp, icon = Icons.Filled.Delete, onClick = doDeleted)
        FabAnimation(show = show, offset = offset, icon = Icons.Filled.Dns, onClick = doChangeSection)
        FabAnimation(show = show, offset = offset * 2, icon = Icons.Filled.RemoveDone, onClick = doUnSelected)
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SwitcherButton(doChangeSorting: (SortingBy) -> Unit) {

    val cornerDp = 4.dp

    var sortingPosition by remember { mutableStateOf(true) }

    Row( verticalAlignment = Alignment.CenterVertically,  horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(cornerDp))
            .clickable {
                if (sortingPosition) doChangeSorting(SortingBy.BY_SECTION) else doChangeSorting(
                    SortingBy.BY_NAME
                )
                sortingPosition = !sortingPosition
            }
    ) {
        TextApp(
            text = stringResource(id = R.string.by_name),
            fontWeight = if (sortingPosition) FontWeight.Bold else FontWeight.Normal,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)
        )
        Spacer(modifier = Modifier.width(24.dp))
        TextApp(
            text = stringResource(id = R.string.by_section),
            fontWeight = if (!sortingPosition) FontWeight.Bold else FontWeight.Normal,
            style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL)
        )
    }
}

