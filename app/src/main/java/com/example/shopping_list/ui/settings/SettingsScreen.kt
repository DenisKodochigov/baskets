package com.example.shopping_list.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.products.StateProductsScreen

@Composable
fun SettingsScreen(
    onSettingsClick: (String) -> Unit = {},
    viewModel: AppViewModel,
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    SettingsScreenLayout()
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun SettingsScreenLayout(modifier: Modifier = Modifier, ){
    Column(modifier = Modifier.fillMaxHeight().background(Color.LightGray)) {
        Text("First Text", modifier = Modifier.background(Color(0xffF44336)), color = Color.White)
        Text("Second Text", modifier = Modifier.background(Color(0xff9C27B0)), color = Color.White)
        Spacer(modifier = Modifier.weight(1f))
        Text("Third Text", modifier = Modifier.background(Color(0xff2196F3)), color = Color.White)
        LazyColumn {
            item { Text(text = "Header") }

            items(3) { index -> Text(text = "First List items : $index") }
            items(2) { index -> Text(text = "Second List Items: $index") }
            item { Text(text = "Footer") }
        }
    }
}