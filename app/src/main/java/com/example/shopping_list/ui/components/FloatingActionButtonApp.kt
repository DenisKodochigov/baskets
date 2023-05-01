package com.example.shopping_list.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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