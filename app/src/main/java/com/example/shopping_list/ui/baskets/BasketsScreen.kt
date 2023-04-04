package com.example.shopping_list.ui.baskets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopping_list.ui.theme.textBottomBar

@Composable
fun BasketsScreen(
    onClickSeeAllAccounts: (String) -> Unit = {},
    onClickSeeAllBills: (String) -> Unit = {},
    onAccountClick: (String) -> Unit = {},
) {

    Row(
        Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp).background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings",
                modifier = Modifier.background(Color.Gray))
            Text( "ICON 1111111111", color = Color.White, style = textBottomBar)
        }
        Column(
            modifier = Modifier.padding(14.dp).background(Color.Blue),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings",
                modifier = Modifier.background(Color.Blue))
            Text( "ICON 2222222222", color = Color.White, style = textBottomBar)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(14.dp).background(Color.Green),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings",
                modifier = Modifier.background(Color.Green))
            Text( "ICON 3333333333", color = Color.White, style = textBottomBar,)
        }
    }

}
@Preview
@Composable
fun BasketsScreenPreview(){
    BasketsScreen()
}
