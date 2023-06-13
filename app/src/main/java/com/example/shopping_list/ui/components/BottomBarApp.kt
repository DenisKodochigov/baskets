package com.example.shopping_list.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shopping_list.navigation.ScreenDestination
import com.example.shopping_list.navigation.appTabRowScreens
import com.example.shopping_list.ui.theme.BackgroundBottomBar
import com.example.shopping_list.ui.theme.textBottomBar
import java.util.*
import com.example.shopping_list.R


@Composable
fun BottomBarApp(currentScreen: ScreenDestination,
                 onTabSelection:(ScreenDestination) -> Unit
) {
    BottomAppBar(
        backgroundColor = BackgroundBottomBar,
        elevation = 6.dp,
        modifier = Modifier.height(TabHeight).clip(shape = RoundedCornerShape(dimensionResource(R.dimen.corner_default)))
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
    Row(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()) {
        allScreens.forEachIndexed { index, screen ->
            if (index == allScreens.size - 1) Spacer(modifier = Modifier.weight(1f))
            BottomTab(
                text = screen.route,
                icon = screen.icon,
                onSelected = { onTabSelected(screen) },
                selected = currentScreen == screen)
        }
    }
}

@Composable
private fun BottomTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean) {

    val color = MaterialTheme.colors.onSurface
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(durationMillis = durationMillis,
            easing = LinearEasing, delayMillis = TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = InactiveTabOpacity),
        animationSpec = animSpec )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 6.dp, start = 14.dp, end = 14.dp)
            .width(35.dp)
            .animateContentSize()
//            .height(TabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false, radius = Dp.Unspecified, color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text }

    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tabTintColor,
            modifier = Modifier.size(IconSize))
        if (selected) {
            Text( text.uppercase(Locale.getDefault()), color = tabTintColor, style = textBottomBar)
        }
    }
}

private val TabHeight = 70.dp
private val IconSize = 50.dp
private const val InactiveTabOpacity = 0.60f
private const val TabFadeInAnimationDuration = 150
private const val TabFadeInAnimationDelay = 100
private const val TabFadeOutAnimationDuration = 100
