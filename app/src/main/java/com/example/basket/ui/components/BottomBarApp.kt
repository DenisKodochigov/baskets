package com.example.basket.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.entity.SizeElement
import com.example.basket.entity.TagsTesting.BOTTOM_APP_BAR
import com.example.basket.navigation.Baskets
import com.example.basket.navigation.ScreenDestination
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.ui.theme.TabFadeInAnimationDelay
import com.example.basket.ui.theme.TabFadeInAnimationDuration
import com.example.basket.ui.theme.TabFadeOutAnimationDuration
import com.example.basket.ui.theme.sizeApp


@Composable
fun BottomBarApp(currentScreen: ScreenDestination, onTabSelection: (ScreenDestination) -> Unit) {

    BottomAppBar(
        contentPadding = PaddingValues(1.dp),
        tonalElevation = 6.dp,
        modifier = Modifier
            .background(color = Color.Transparent)  //MaterialTheme.colorScheme.surface)
            .height(sizeApp(SizeElement.SIZE_ICON) + 12.dp)
            .testTag(BOTTOM_APP_BAR)
            .clip(shape = MaterialTheme.shapes.small)
    ) {
        appTabRowScreens.forEachIndexed { index, screen ->
            if (index == appTabRowScreens.size - 1) Spacer(modifier = Modifier.weight(1f))
            BottomTab(
                text = screen.route,
                icon = screen.icon,
                onSelected = { onTabSelection(screen) },
                selected = currentScreen == screen
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
private fun BottomTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean,
) {
    val animationSpec = remember {
        tween<Color>(
            easing = LinearEasing, delayMillis = TabFadeInAnimationDelay,
            durationMillis = if (selected) TabFadeInAnimationDuration
            else TabFadeOutAnimationDuration
        )
    }

    val colorUnselected = Color(
        MaterialTheme.colorScheme.primary.red,
        MaterialTheme.colorScheme.primary.green,
        MaterialTheme.colorScheme.primary.blue,
        MaterialTheme.colorScheme.primary.alpha * 0.6f
    )

    val iconColor by animateColorAsState(
        label = "", animationSpec = animationSpec,
        targetValue = if (selected) MaterialTheme.colorScheme.primary else colorUnselected,
    )
    Spacer(modifier = Modifier.width(12.dp))
    IconButton(
        onClick = onSelected,
        modifier = Modifier
            .size(sizeApp(SizeElement.SIZE_ICON))
            .testTag(text)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize()
                .clearAndSetSemantics { contentDescription = text }
        )
    }
}

@Preview
@Composable
fun BottomBarAppPreview() {
    BottomBarApp(currentScreen = Baskets, {})
}