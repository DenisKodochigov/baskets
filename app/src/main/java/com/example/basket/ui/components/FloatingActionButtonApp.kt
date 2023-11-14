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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.basket.entity.SizeElement
import com.example.basket.entity.TagsTesting.FAB_PLUS
import com.example.basket.ui.theme.sizeApp
import com.example.basket.utils.log


@Composable fun ExtendedFAB(
    icon: ImageVector = Icons.Filled.Add,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(sizeApp(SizeElement.PADDING_FAB))
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        text = { Text(text = text) },
    )
}

@Composable
fun FloatingActionButtonApp( offset: Dp,
                             refreshScreen: MutableState<Boolean>,
                             modifier: Modifier = Modifier,
                             icon: ImageVector,
                             onClick: () -> Unit) {

    val plug = refreshScreen.value

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .offset(0.dp, offset)
            .size(sizeApp(SizeElement.SIZE_FAB))
            .testTag(FAB_PLUS))
    {
        Icon(
            icon, null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .padding(sizeApp(SizeElement.PADDING_FAB))
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )
    }
}
@Composable
fun FabAnimation(show: Boolean, offset: Dp, icon: ImageVector, onClick: () -> Unit) {

    var isAnimated by rememberSaveable { mutableStateOf(false) }
    val refreshScreen = remember{ mutableStateOf(true)}
    val offsetFAB by animateDpAsState(
        targetValue = if (isAnimated) {
            if (show) offset else sizeApp(SizeElement.HEIGHT_FAB_BOX)
        } else {
            if (show) sizeApp(SizeElement.HEIGHT_FAB_BOX) else offset
        },
        animationSpec = tween(durationMillis = 600), label = ""
    )

    FloatingActionButtonApp(offset = offsetFAB,icon = icon, onClick = onClick, refreshScreen = refreshScreen)
    isAnimated = true
}