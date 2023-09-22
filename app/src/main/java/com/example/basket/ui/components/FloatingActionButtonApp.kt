package com.example.basket.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.basket.entity.TagsTesting.FAB_PLUS

@Composable
fun FloatingActionButtonApp(offset: Dp, top: Dp, icon: ImageVector, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(top = top).offset(0.dp, offset).size(40.dp).testTag(FAB_PLUS))
    {
        Icon(
            icon, null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .size(28.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )
    }
}

@Composable
fun FabAnimation(show: Boolean, offset: Dp, icon: ImageVector, onClick: () -> Unit) {

    var isAnimated by rememberSaveable { mutableStateOf(false) }

    val offsetFAB by animateDpAsState(
        targetValue = if (isAnimated) {
            if (show) offset else 200.dp
        } else {
            if (show) 200.dp else offset
        },
        animationSpec = tween(durationMillis = 600), label = ""
    )

    FloatingActionButtonApp(offsetFAB, 0.dp, icon, onClick)
    isAnimated = true
}