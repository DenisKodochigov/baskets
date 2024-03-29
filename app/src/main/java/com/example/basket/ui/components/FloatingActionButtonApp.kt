package com.example.basket.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.basket.entity.SizeElement
import com.example.basket.entity.TagsTesting.FAB_PLUS
import com.example.basket.entity.TypeText
import com.example.basket.ui.theme.colorApp
import com.example.basket.ui.theme.sizeApp
import com.example.basket.ui.theme.styleApp

@Composable fun ExtendedFAB(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add,
    onClick: () -> Unit,
    refreshScreen:Boolean = true,
    text: String
){
    val plug = refreshScreen
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        icon = {
            Icon(
                icon, null,
                tint = colorApp.onPrimaryContainer,
                modifier = Modifier
                    .padding(sizeApp(SizeElement.PADDING_FAB))
                    .background(color = colorApp.primaryContainer)
            )
        },
        text = { TextApp(text = text, style = styleApp(nameStyle = TypeText.EDIT_TEXT)) },
    )
}

@Composable
fun FloatingActionButtonApp( offset: Dp,
                             modifier: Modifier = Modifier,
                             icon: ImageVector,
                             onClick: () -> Unit
){
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .offset(0.dp, offset)
            .size(sizeApp(SizeElement.SIZE_FAB))
            .testTag(FAB_PLUS))
    {
        Icon(
            icon, null,
            tint = colorApp.onPrimaryContainer,
            modifier = Modifier
                .padding(sizeApp(SizeElement.PADDING_FAB))
                .fillMaxSize()
                .background(color = colorApp.primaryContainer)
        )
    }
}
@Composable fun FabAnimation(show: Boolean, offset: Dp, icon: ImageVector, onClick: () -> Unit
){
    var isAnimated by rememberSaveable { mutableStateOf(false) }
    val offsetFAB by animateDpAsState(
        targetValue = if (isAnimated) {
            if (show) offset else sizeApp(SizeElement.HEIGHT_FAB_BOX)
        } else {
            if (show) sizeApp(SizeElement.HEIGHT_FAB_BOX) else offset
        },
        animationSpec = tween(durationMillis = 600), label = ""
    )

    FloatingActionButtonApp(offset = offsetFAB,icon = icon, onClick = onClick)
    isAnimated = true
}