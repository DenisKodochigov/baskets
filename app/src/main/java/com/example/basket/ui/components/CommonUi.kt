package com.example.basket.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.basket.R
import com.example.basket.entity.CustomTriangleShape
import com.example.basket.entity.Direcions
import com.example.basket.entity.SortingBy
import com.example.basket.entity.TypeText
import com.example.basket.ui.theme.styleApp

@Composable
fun HeaderScreen(text: String) {
    Spacer(modifier = Modifier.height(24.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextApp(text = text, style = styleApp(nameStyle = TypeText.NAME_SCREEN))
    }
}

@Composable
fun HeaderImScreen(text: String, idImage:Int ) {
    Column( Modifier.fillMaxWidth() ){
        Image(
            painter = painterResource(id = idImage),
            contentDescription = "Photo",
            contentScale = Crop,
            modifier = Modifier.height(340.dp)
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)) {
            TextApp(
                text = text,
                style = styleApp(nameStyle = TypeText.NAME_SCREEN),
                modifier = Modifier.align(alignment = Alignment.BottomCenter) )
        }
    }
}
@Composable
fun HeaderSection(text: String, modifier: Modifier) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 12.dp), horizontalArrangement = Arrangement.Start) {
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
                            TextApp(text = item.second, style = styleApp(nameStyle = TypeText.EDIT_TEXT))}
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
            MyTextH2( stringResource(R.string.ok))
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
    val offset = 48.dp
    Box( modifier ) {
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
            .clickable{
                if (sortingPosition) doChangeSorting(SortingBy.BY_SECTION) else doChangeSorting(SortingBy.BY_NAME)
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

@Composable fun SliderApp(
    modifier: Modifier = Modifier,
    position: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Int,
    direction: Direcions,
    onSelected: (Float)->Unit )
{
    SliderApp1(
        modifier = modifier,
        position = position,
        valueRange = valueRange,
        step = step,
        direction = direction,
        onSelected = onSelected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun SliderApp1(
    modifier: Modifier = Modifier,
    position: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Int,
    direction: Direcions,
    onSelected: (Float)->Unit )
{
    val interactionSource = remember { MutableInteractionSource() }
    val colors =  SliderDefaults.colors(
                    thumbColor = Color(0xFF575757),
                    activeTrackColor = Color(0xFFA2A2A2),
                    activeTickColor = Color(0xFFA2A2A2),
                    inactiveTrackColor = Color(0xFFA2A2A2),
                    inactiveTickColor = Color(0xFFA2A2A2),
                    disabledThumbColor = Color.Transparent,
                    disabledActiveTrackColor = Color.Transparent,
                    disabledActiveTickColor = Color.Transparent,
                    disabledInactiveTrackColor = Color.Transparent,
                    disabledInactiveTickColor = Color.Transparent,
                )
    Slider(
        modifier = modifier,
        value = position,
        valueRange = valueRange,
        steps = step,
        onValueChange = { onSelected(it) },
        thumb = {
            SliderDefaults.Thumb(
                modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CustomTriangleShape(direction))
                    .drawWithContent {  },
                interactionSource = interactionSource,
                thumbSize = DpSize(34.dp,34.dp),
                colors = colors
            )
        },
    )
}
