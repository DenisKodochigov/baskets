package com.example.shopping_list.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopping_list.entity.Article
import com.example.shopping_list.ui.theme.BackgroundFab
import com.example.shopping_list.ui.theme.BorderBottomBar
import com.example.shopping_list.ui.theme.ContentFab

@Composable
fun FloatingActionButtonApp(onClick:() -> Unit){
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 48.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab
    ) {
        Icon(Icons.Filled.Add,null)
    }
}

@Composable
fun FabDeleteProducts(deleteSelected: MutableState<Boolean>){

    FloatingActionButton(onClick =  { deleteSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab) {
        Icon(Icons.Filled.Delete,"")
    }
}
@Composable
fun FabChangeGroupProducts(changeGroupSelected: MutableState<Boolean>){

    FloatingActionButton(onClick =  { changeGroupSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab) {
        Icon(Icons.Filled.Dns,"")
    }
}
@Composable
fun FabUnSelectProducts(unSelected: MutableState<Boolean>){

    FloatingActionButton(onClick = { unSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab) {
        Icon(Icons.Filled.RemoveDone,"")
    }
}
