package com.example.shopping_list.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HeaderScreen(text: String){
    val typography = MaterialTheme.typography
    Row( Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Text(text, style = typography.h1)
    }
}
@Composable
fun BasketsRow(modifier: Modifier = Modifier, name: String,){

    Spacer(
        Modifier
            .width(360.dp)
            .size(360.dp, 4.dp)
//            .background(color = color)
            .padding(bottom = 12.dp))
    Row(
        modifier = modifier.height(68.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography

        Column(Modifier) {
            Text(text = name, style = typography.body1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = name, style = typography.subtitle1)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.width(16.dp))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            )
        }
    }
    RallyDivider()
}