package com.example.shopping_list.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontVariation.width
import com.example.shopping_list.App
import com.example.shopping_list.entity.TypeText

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (!useDarkTheme) LightColorScheme else DarkColorScheme ,
        content = content,
        shapes = Shapes,
    )
}

 @Composable fun styleApp(nameStyle: TypeText): TextStyle{

     val nameScreen = listOf(typography.headlineLarge, typography.headlineSmall, typography.titleMedium)
     val nameSection = listOf(typography.headlineMedium, typography.titleLarge, typography.titleSmall)
     val textInList = listOf(typography.headlineSmall, typography.titleLarge, typography.titleSmall)
     val textInListSmall = listOf(typography.bodyMedium, typography.labelLarge, typography.labelMedium)
     val editText = listOf(typography.headlineSmall, typography.titleLarge, typography.titleSmall)
     val editTextLabel = listOf(typography.bodyMedium, typography.labelLarge, typography.labelMedium)
     val textInListSettings = listOf(typography.headlineSmall, typography.titleLarge, typography.titleSmall)
     val nameSlider = listOf(typography.bodyMedium, typography.labelLarge, typography.labelMedium)


    return when(nameStyle){
        TypeText.NAME_SCREEN -> nameScreen[App.scale]
        TypeText.NAME_SECTION -> nameSection[App.scale]
        TypeText.TEXT_IN_LIST -> textInList[App.scale]
        TypeText.TEXT_IN_LIST_SMALL -> textInListSmall[App.scale]
        TypeText.EDIT_TEXT -> editText[App.scale]
        TypeText.EDIT_TEXT_TITLE -> editTextLabel[App.scale]
        TypeText.TEXT_IN_LIST_SETTING -> textInListSettings[App.scale]
        TypeText.NAME_SLIDER -> nameSlider[App.scale]
    }
}

//@Composable
//fun AppTheme( darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme)  DarkColorPalette else LightColorPalette
//    MaterialTheme(
//        colorScheme = colors,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppComposable() {
//    // M3 composables
//    MaterialTheme(
//        colorScheme = …,
//    typography = …,
//    shapes = …) {
//
//    }
//}
