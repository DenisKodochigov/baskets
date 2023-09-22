package com.example.basket.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.entity.TagsTesting.BOTTOM_APP_BAR
import com.example.basket.navigation.Baskets
import com.example.basket.navigation.ScreenDestination
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.ui.theme.TabFadeInAnimationDelay
import com.example.basket.ui.theme.TabFadeInAnimationDuration
import com.example.basket.ui.theme.TabFadeOutAnimationDuration


@Composable
fun BottomBarApp(currentScreen: ScreenDestination, onTabSelection: (ScreenDestination) -> Unit) {
    BottomAppBar(
        tonalElevation = 6.dp,
        modifier = Modifier
            .background(color = Color.Transparent)  //MaterialTheme.colorScheme.surface)
            .height(52.dp)
            .testTag(BOTTOM_APP_BAR)
            .clip(shape = MaterialTheme.shapes.small)
    ) {
//        BottomTabRow(
//            allScreens = appTabRowScreens,
//            currentScreen = currentScreen,
//            onTabSelected = onTabSelection
//        )
        appTabRowScreens.forEachIndexed { index, screen ->
            if (index == appTabRowScreens.size - 1) Spacer(modifier = Modifier.weight(1f))
            BottomTab(
                text = screen.route,
                icon = screen.icon,
                onSelected = { onTabSelection(screen) },
                selected = currentScreen == screen
            )
        }
    }
}

@Composable
fun BottomTabRow(
    allScreens: List<ScreenDestination>,
    onTabSelected: (ScreenDestination) -> Unit,
    currentScreen: ScreenDestination
) {

//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color =Color.Gray)
//            .selectableGroup()
//            .padding(bottom = 0.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        allScreens.forEachIndexed { index, screen ->
//            if (index == allScreens.size - 1) Spacer(modifier = Modifier.weight(1f))
//            BottomTab(
//                text = screen.route,
//                icon = screen.icon,
//                onSelected = { onTabSelected(screen) },
//                selected = currentScreen == screen
//            )
//        }
//    }
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

    IconButton(
        onClick = onSelected,
        modifier = Modifier
            .padding(top= 4.dp)
            .testTag(text)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier
                .padding(start = 2.dp, end = 2.dp)
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