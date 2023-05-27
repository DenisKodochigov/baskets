package com.example.shopping_list.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import com.example.shopping_list.ui.products.StateProductsScreen
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
fun ButtonSwipeProduct(itemList:List<Product>,
                       sortingList: (List<Product>, Int) -> Unit )
{
//    Log.d("KDS", "ButtonSwipeProduct")
    Row(Modifier.fillMaxWidth()) {
        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowDownward) { sortingList(itemList,1) }
//        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.lazy_padding_hor)))
//        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowUpward) { sortingList(itemList,-1) }
    }
}

@Composable
fun ButtonSwipeBasket(itemList:MutableList<Basket>,
                      sortingList: (MutableList<Basket>, Int) -> Unit )
{
//    Log.d("KDS", "ButtonSwipeBasket")
    Row(Modifier.fillMaxWidth()) {
        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowDownward) { sortingList(itemList,1) }
//        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.lazy_padding_hor)))
//        ButtonMove(Modifier.weight(1f), Icons.Default.ArrowUpward) { sortingList(itemList,-1) }
    }
}

@Composable
fun ButtonSwipeArticle( sortingList: (Int) -> Unit )
{
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
fun LayoutAddEditProduct(
    uiState: StateProductsScreen,
    bottomSheetHide: () -> Unit,
    onAddProduct: (Product) -> Unit)
{
//    Log.d("KDS", "BottomSheetContentProduct")
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val enterValue = remember{ mutableStateOf("1")}
    val enterArticle = remember{ mutableStateOf(Pair<Long,String>(0,""))}
    val enterGroup = remember{ mutableStateOf(Pair<Long,String>(1,"All"))}
    val enterUnit = remember{ mutableStateOf(Pair<Long,String>(1,"шт"))}
    val focusRequesterSheet = remember { FocusRequester() }


    val idUnit = uiState.unitA.find { it.nameUnit == enterUnit.value.second }?.idUnit ?: 0L
    enterUnit.value = Pair(idUnit, enterUnit.value.second )
    val idGroup = uiState.group.find { it.nameGroup == enterGroup.value.second }?.idGroup ?: 0L
    enterGroup.value = Pair(idGroup, enterGroup.value.second )
    val idArticle = uiState.articles.find { it.nameArticle == enterArticle.value.second }?.idArticle ?: 0L
    enterArticle.value = Pair(idArticle, enterArticle.value.second )

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .heightIn((screenHeight * 0.3).dp, (screenHeight * 0.75).dp)) {
//        Log.d("KDS", "BottomSheetContentProduct.Column")
        HeaderScreen(text = "Add product", Modifier.focusRequester(focusRequesterSheet))
        Spacer(Modifier.height(24.dp))
        MyExposedDropdownMenuBox(/** Select article*/
            listItems = uiState.articles.map{ Pair(it.idArticle, it.nameArticle) },
            label = "Select product",
            modifier = Modifier.fillMaxWidth(),
            enterValue = enterArticle,
            filtering = true )
        if (enterArticle.value.first > 0) {
            enterGroup.value = selectGroupWithArticle(enterArticle.value.first, uiState.articles)
            enterUnit.value = selectUnitWithArticle(enterArticle.value.first, uiState.articles)
            enterValue.value = "1"
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            MyExposedDropdownMenuBox(/** Select group*/
                listItems = uiState.group.map{ Pair(it.idGroup, it.nameGroup) },
                label = "Group",
                modifier = Modifier.weight(1f),
                enterValue = enterGroup,
                filtering = true)
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextFieldWithoutIcon( /** Value*/ typeKeyboard = "digit",
                modifier = Modifier.width(90.dp),
                enterValue = enterValue)
            Spacer(Modifier.width(4.dp))
            MyExposedDropdownMenuBox(/** Select unit*/
                listItems = uiState.unitA.map{ Pair(it.idUnit, it.nameUnit) },
                label = "Unit",
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false)
        }
        Spacer(Modifier.height(36.dp))
        Row(Modifier.fillMaxWidth()) {
            ButtonMy(Modifier.weight(1f),"Add") {
                val article = ArticleEntity( idArticle = enterArticle.value.first,
                    nameArticle = enterArticle.value.second )
                article.group = GroupEntity( idGroup = enterGroup.value.first,
                    nameGroup = enterGroup.value.second )
                article.groupId = enterGroup.value.first
                article.unitA = UnitEntity( idUnit = enterUnit.value.first,
                    nameUnit = enterUnit.value.second )
                article.unitId = enterUnit.value.first
                val product = ProductEntity(
                    value = if (enterValue.value.isEmpty()) 1.0 else enterValue.value.toDouble())
                product.article = article
                onAddProduct( product )
                enterArticle.value = Pair(0,"")
            }
            Spacer(Modifier.width(12.dp))
            ButtonMy(Modifier.weight(1f), "Cancel"){
                enterArticle.value = Pair(0,"")
                bottomSheetHide() }
        }
        Spacer(Modifier.height(72.dp))
    }
}

@Composable
fun LayoutAddEditArticle(
    article: MutableState<Article>,
    listUnit: List<UnitA>,
    listGroup: List<GroupArticle>)
{
    Log.d("KDS", "LayoutAddEditArticle")
    val enterNameArticle = remember{ mutableStateOf( article.value.nameArticle )}
    val enterGroup = remember{ mutableStateOf(Pair<Long,String>(1,"All"))}
    val enterUnit = remember{ mutableStateOf(Pair<Long,String>(1,"шт"))}

    article.value.unitA = if (enterUnit.value.first == 0L && enterUnit.value.second != "") {
        UnitEntity(nameUnit = enterUnit.value.second, idUnit = 0L)
    } else listUnit.find { it.idUnit == enterUnit.value.first }!!

    article.value.group = if (enterGroup.value.first == 0L && enterGroup.value.second != "") {
        GroupEntity(nameGroup = enterGroup.value.second, idGroup = 0L)
    } else listGroup.find { it.idGroup == enterGroup.value.first }!!

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        MyOutlinedTextFieldWithoutIcon(modifier = Modifier.fillMaxWidth(), enterValue = enterNameArticle, "text")
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            MyExposedDropdownMenuBox(/** Select group*/
                listItems = listGroup.map{ Pair(it.idGroup, it.nameGroup) },
                label = "Group",
                modifier = Modifier.weight(1f),
                enterValue = enterGroup,
                filtering = true)
            Spacer(Modifier.width(4.dp))
            MyExposedDropdownMenuBox(/** Select unit*/
                listItems = listUnit.map{ Pair(it.idUnit, it.nameUnit) },
                label = "Unit",
                modifier = Modifier.width(120.dp),
                enterValue = enterUnit,
                filtering = false,
                readOnly = true)
        }
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