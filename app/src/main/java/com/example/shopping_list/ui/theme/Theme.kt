package com.example.shopping_list.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat
import com.example.shopping_list.App
import com.example.shopping_list.entity.TypeText

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                useDarkTheme
        }
        MaterialTheme(
            colorScheme = if (!useDarkTheme) LightColorScheme else DarkColorScheme,
            content = content,
            shapes = Shapes,
        )
    }
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

//#####################################################################################################



