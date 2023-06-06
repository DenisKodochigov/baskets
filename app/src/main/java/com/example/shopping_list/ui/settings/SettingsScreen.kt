package com.example.shopping_list.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    onSettingsClick: (String) -> Unit = {},
    bottomSheetContent: MutableState<@Composable (() -> Unit)?>
) {
    SettingsScreenLayout()
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun SettingsScreenLayout(modifier: Modifier = Modifier, ){

//        Column(modifier = Modifier
////            .fillMaxHeight()
//            .background(Color.LightGray)) {
//            Text("1 Text", modifier = Modifier.background(Color(0xffF44336)), color = Color.White)
//            Text("2 Text", modifier = Modifier.background(Color(0xff9C27B0)), color = Color.White)
//            Text("3 Text", modifier = Modifier.background(Color(0xff2196F3)), color = Color.White)
//            Column(Modifier.fillMaxHeight().weight(1f)) {
//                Spacer(modifier = Modifier.weight(1f))
//                LazyColumn(
//                ) {
//                    item { Text(text = "Header") }
//                    items(33) { index -> Text(text = "List items : $index") }
//                }
//            }
//
//            Button(onClick = { /*TODO*/ }) {
//                Text("Test")
//            }
//            Text("4 Text", modifier = Modifier.background(Color(0xff2196F3)), color = Color.White)
//        }

}