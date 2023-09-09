package com.example.shopping_list.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.navigation.Baskets
import com.example.shopping_list.navigation.ScreenDestination
import com.example.shopping_list.navigation.appTabRowScreens
import com.example.shopping_list.ui.theme.TabFadeInAnimationDelay
import com.example.shopping_list.ui.theme.TabFadeInAnimationDuration
import com.example.shopping_list.ui.theme.TabFadeOutAnimationDuration
import com.example.shopping_list.ui.theme.TabHeight


@Composable
fun BottomBarApp(currentScreen: ScreenDestination, onTabSelection:(ScreenDestination) -> Unit) {
    BottomAppBar(
        tonalElevation = 6.dp,
        modifier = Modifier
            .background(color = Color.Transparent)  //MaterialTheme.colorScheme.surface)
//            .height(TabHeight)
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

    Row(modifier = Modifier.fillMaxSize().selectableGroup(),
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
    selected: Boolean) {

    val iconSize = 24.dp
    val colorSelectIcon = MaterialTheme.colorScheme.onPrimaryContainer
    val colorUnSelectIcon = MaterialTheme.colorScheme.primary
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(durationMillis = durationMillis,
            easing = LinearEasing, delayMillis = TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) colorSelectIcon else colorUnSelectIcon,
        animationSpec = animSpec, label = ""
    )
    IconButton( onClick = onSelected, modifier = Modifier.size(40.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(24.dp)
//                .padding(start = 12.dp, end = 12.dp)
//                .width(45.dp)
                .padding(start = 2.dp, end = 2.dp, top = 2.dp, bottom = 2.dp)
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