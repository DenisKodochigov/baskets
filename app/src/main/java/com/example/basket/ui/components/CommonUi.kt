package com.example.basket.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basket.R
import com.example.basket.entity.SizeElement
import com.example.basket.entity.SortingBy
import com.example.basket.entity.TagsTesting.BUTTON_OK
import com.example.basket.entity.TypeKeyboard
import com.example.basket.entity.TypeText
import com.example.basket.entity.UPDOWN
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.shapesApp
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp
import com.example.basket.utils.keyBoardOpt
import com.example.basket.utils.log

@Composable
fun HeaderScreen(text: String, refreshScreen: MutableState<Boolean> = mutableStateOf(false) ) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        val plug = refreshScreen.value
        TextApp(text = text, style = styleApp(nameStyle = TypeText.NAME_SCREEN))
    }
}

@Composable
fun HeaderSection(text: String, modifier: Modifier, refreshScreen: MutableState<Boolean> = mutableStateOf(false)) {
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

    enteredText = if (enterValue != null && !focusItem) enterValue.value.second else ""

    log(true, "$label: enteredText = ${enteredText}: enterValue = $enterValue: focusItem: $focusItem")

    ExposedDropdownMenuBox(
        expanded = expandedLocal && enabled,
        modifier = modifier,
        onExpandedChange = { expandedLocal = !expandedLocal }
    ) {
        OutlinedTextField (
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(1f)
                .background(color = colorApp.surface)
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
                log(true, "$label: ExposedDropdownMenuBox.onValueChange")
            },
            label = { if (label != null)
                TextApp(text = label, style = styleApp(nameStyle = TypeText.EDIT_TEXT_TITLE)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocal) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text).copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.moveFocus(FocusDirection.Next)
                    enterValue!!.value = Pair(0, enteredText)
                    expandedLocal = false
                },
            ),
        )
        val filteringOptions = if (filtering) listItems.filter {
                it.second.contains(enteredText, ignoreCase = true) }
            else listItems
        if (filteringOptions.isNotEmpty()) {

            ExposedDropdownMenu(
                modifier = Modifier
                    .height(heightDropMenu * 8)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                expanded = expandedLocal && enabled,
                onDismissRequest = { expandedLocal = false })
            {
                filteringOptions.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            enteredText = item.second
                            expandedLocal = false
                            enterValue!!.value = item
                            localFocusManager.moveFocus(FocusDirection.Next) },
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
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = colorApp.onSurface
) {
    Text(
        text = text,
        style = style,
        maxLines = 1,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign,
        modifier = modifier,
        color = color
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
        style = styleApp(nameStyle = TypeText.TEXT_IN_LIST)
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyOutlinedTextFieldWithoutIcon(
    modifier: Modifier,
    enterValue: MutableState<String>,
    typeKeyboard: TypeKeyboard,
    label:  @Composable (() -> Unit)? = null,
    keyboardActionsOnDone: (() -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    var enterText by remember { mutableStateOf(enterValue.value) }

    OutlinedTextField(
        modifier = modifier.background(color = colorApp.surface),
        value = enterText,
        singleLine = true,
        textStyle = styleApp(nameStyle = TypeText.EDIT_TEXT),
        label = label,
        onValueChange = {
            enterText = it
            enterValue.value = it
        },
        keyboardOptions = keyBoardOpt(typeKeyboard),
        keyboardActions = KeyboardActions(
            onDone =
            {
                localFocusManager.clearFocus()
                enterValue.value = enterText
                enterText = ""
                if (keyboardActionsOnDone != null) {
                    keyboardActionsOnDone.invoke()
                }
                keyboardController?.hide()
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
    typeKeyboard: TypeKeyboard,
    title: String = ""
) {
    val localFocusManager = LocalFocusManager.current
    var focusItem by remember { mutableStateOf(false) }
    var enterText by remember { mutableStateOf("") }
    enterText = if (!focusItem) enterValue.value else ""
//    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        TextApp(
            text = title,
            style = styleApp(TypeText.NAME_SLIDER),
            modifier = Modifier.padding(start = 12.dp)
        )
        OutlinedTextField(
            modifier = modifier
                .onFocusChanged { focusItem = it.isFocused }
                .background(color = colorApp.surface),
            value = enterText,
            singleLine = true,
            textStyle = styleApp(nameStyle = TypeText.EDIT_TEXT),
            onValueChange = {
                focusItem = false
                enterText = it
                enterValue.value = it
            },
            keyboardOptions = keyBoardOpt(typeKeyboard),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.moveFocus(FocusDirection.Next)
                    enterValue.value = enterText
//                keyboardController?.hide()
                }
            ),
        )
    }
}

@Composable fun showFABs(
    startScreen: Boolean,
    isSelected: Boolean,
    modifier: Modifier,
    doDeleted: ()->Unit,
    doChangeSection: ()->Unit,
    doUnSelected:()->Unit):Boolean
{
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

@Composable fun ShowFABs_(
    show: Boolean,
    modifier: Modifier,
    doDeleted: ()->Unit,
    doChangeSection: ()->Unit,
    doUnSelected:()->Unit)
{
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

@Composable fun ShowArrowVer(enable:Boolean, direction: UPDOWN, drawLine: Boolean)
{
    Column(modifier = Modifier.fillMaxWidth()){
        if (direction == UPDOWN.UP && drawLine) Divider(color = colorApp.primary, thickness = 1.dp)

        Row(modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.Center) {
            if( enable ) {
                if (direction == UPDOWN.UP) ArrowUp() else ArrowDown()
            }
            else ArrowNoneVer()
        }
        if (direction == UPDOWN.DOWN && drawLine) Divider(color = colorApp.primary, thickness = 1.dp)
    }
}
@Composable fun ShowArrowHor(enable:Boolean, direction: UPDOWN, drawLine: Boolean)
{
    Row(modifier = Modifier.fillMaxHeight(),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
    {
        if (direction == UPDOWN.START && drawLine){
            Divider(color = colorApp.primary, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp))}

        if( enable ) { if (direction == UPDOWN.START) ArrowLeft() else ArrowRight()}
        else ArrowNoneHor()
        if (direction == UPDOWN.END && drawLine){
            Divider(color = colorApp.primary, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp))}
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable fun TextFieldApp(modifier: Modifier = Modifier, textAlign: TextAlign,
                             enterValue: MutableState<String>, typeKeyboard: TypeKeyboard)
{
    val keyboardController = LocalSoftwareKeyboardController.current
    var focusItem by remember { mutableStateOf(false) }
    enterValue.value = if (!focusItem) enterValue.value else ""

    BasicTextField(
        value = enterValue.value,
        onValueChange = {
            focusItem = false
            enterValue.value = it },
        singleLine = true,
        maxLines = 1,
        modifier = modifier.onFocusChanged { focusItem = it.isFocused }
            .border(width = 1.dp, color = colorApp.onPrimaryContainer, shape = shapesApp.extraSmall),
        textStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            textAlign = textAlign),
        decorationBox = {
            Row( verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)){ it() }
        },
        keyboardOptions = keyBoardOpt(typeKeyboard),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}
@Composable fun ButtonApp(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier =Modifier,
    enabled: Boolean = true,
){
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors (
            containerColor= colorApp.tertiaryContainer,
            contentColor = colorApp.onTertiaryContainer ,
            disabledContainerColor = colorApp.surface,
            disabledContentColor = colorApp.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 0.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 6.dp,
            disabledElevation= 6.dp
        ),
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable fun ButtonCircle( modifier: Modifier, iconButton: ImageVector, onClick: () -> Unit) {
    val radius = 25.dp
    IconButton (
        modifier = modifier
            .clip(RoundedCornerShape(radius, radius, radius, radius))
            .size(60.dp),
        onClick = { onClick() }) {
        Icon(
            imageVector = iconButton, null,
            tint = colorApp.primary ,
            modifier = Modifier.size(60.dp))
    }
}
@Composable
fun TextButtonOK(onConfirm: () -> Unit, enabled: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        log(true, "TextButtonOK")
        TextButton(onClick = onConfirm, enabled = enabled) {
            TextApp(
                text = stringResource(R.string.ok),
                modifier = Modifier.testTag(BUTTON_OK),
                style = styleApp(nameStyle = TypeText.TEXT_IN_LIST)
            )
        }
    }
}