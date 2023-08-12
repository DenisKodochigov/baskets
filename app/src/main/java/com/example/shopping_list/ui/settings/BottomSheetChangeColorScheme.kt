package com.example.shopping_list.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.entity.Component
import com.example.shopping_list.ui.components.ThemeActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BottomSheetChangeColorScheme() {
    Column(Modifier.background(MaterialTheme.colorScheme.background)) {
        TopAppBar(title = { Text(text = "Jetpack theme picker", color = Color.Black) },)
        LazyColumn(state = rememberLazyListState()) {
            items(items = getComponents(), key = null) {
                ButtonComponent(it.componentName, it.className)
            }
        }
    }

}
@Composable
fun ButtonComponent(buttonText: String, className: Class<*>) {
    val context = LocalContext.current
    OutlinedButton(
        onClick = {
//            context.startActivity( Intent(applicationContext, className))
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(0.04f)),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(0.5f)),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = buttonText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

private fun getComponents() = listOf(Component("Theme", ThemeActivity::class.java))