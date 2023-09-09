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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.alpha
import com.example.basket.navigation.Baskets
import com.example.basket.navigation.ScreenDestination
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.ui.theme.TabFadeInAnimationDelay
import com.example.basket.ui.theme.TabFadeInAnimationDuration
import com.example.basket.ui.theme.TabFadeOutAnimationDuration


@Composable
fun BottomBarApp(currentScreen: ScreenDestination, onTabSelection:(ScreenDestination) -> Unit) {
    BottomAppBar(
        tonalElevation = 1.dp,
        modifier = Modifier
            .background(color = Color.Transparent)  //MaterialTheme.colorScheme.surface)
            .height(48.dp)
            .clip(shape = MaterialTheme.shapes.small)
    ){
        BottomTabRow(
            allScreens = appTabRowScreens,
            currentScreen = currentScreen,
            onTabSelected = onTabSelection)
    }
}

@Composable
fun BottomTabRow(
    allScreens: List<ScreenDestination>,
    onTabSelected: (ScreenDestination) -> Unit,
    currentScreen: ScreenDestination
) {

    Row(modifier = Modifier.fillMaxSize().selectableGroup().padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        allScreens.forEachIndexed { index, screen ->
            if (index == allScreens.size - 1) Spacer(modifier = Modifier.weight(1f))
            BottomTab(
                text = screen.route,
                icon = screen.icon,
                onSelected = { onTabSelected(screen) },
                selected = currentScreen == screen )
        }
    }
}

@Composable
private fun BottomTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean)
{
    val animationSpec = remember {
        tween<Color>(easing = LinearEasing, delayMillis = TabFadeInAnimationDelay,
            durationMillis = if (selected) TabFadeInAnimationDuration
            else TabFadeOutAnimationDuration)
    }

    val colorUnselected = Color(
        MaterialTheme.colorScheme.primary.red,
        MaterialTheme.colorScheme.primary.green,
        MaterialTheme.colorScheme.primary.blue,
        MaterialTheme.colorScheme.primary.alpha * 0.7f)

    val iconColor by animateColorAsState(
        label = "", animationSpec = animationSpec,
        targetValue = if (selected) MaterialTheme.colorScheme.primary else colorUnselected,
    )

    IconButton( onClick = onSelected, modifier = Modifier.size(46.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier
                .size(28.dp)
                .padding(start = 2.dp, end = 2.dp)
                .animateContentSize()
                .clearAndSetSemantics { contentDescription = text }
        )
    }
//    Icon(
//        imageVector = icon,
//        contentDescription = text,
//        tint = tabTintColor,
//        modifier = Modifier
//                .size(iconSize)
////                .padding(start = 12.dp, end = 12.dp)
////                .width(45.dp)
//            .padding(start = 2.dp, end = 2.dp, top = 2.dp, bottom = 2.dp)
//            .animateContentSize()
//            .clearAndSetSemantics { contentDescription = text }
//            .background(color = Color.Green)
////            .selectable(
////                selected = selected,
////                onClick = onSelected,
////                role = Role.Tab,
////                interactionSource = remember { MutableInteractionSource() },
////                indication = rememberRipple(
////                    bounded = false,
////                    radius = 0.dp,
////                    color = Color.Unspecified
////                )
////            )
//
//    )
}

@Preview
@Composable
fun BottomBarAppPreview(){
    BottomBarApp(currentScreen = Baskets, {})
}