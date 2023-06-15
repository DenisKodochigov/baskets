package com.example.shopping_list.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.RemoveDone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shopping_list.ui.theme.BackgroundFab
import com.example.shopping_list.ui.theme.BorderBottomBar
import com.example.shopping_list.ui.theme.ContentFab

@Composable
fun FloatingActionButtonApp(onClick:() -> Unit){
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 70.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab
    ) {
        Icon(Icons.Filled.Add,null)
    }
}

@Composable
fun FabDeleteProducts(deleteSelected: MutableState<Boolean>){
//    val collapsed = true
//    var isAnimated by remember { mutableStateOf (false) }
//    isAnimated = !deleteSelected.value
//
//    val alpha by animateDpAsState( if (isAnimated) 0.dp else 20.dp,
//        animationSpec = tween(durationMillis = 2000 ))
//
//    val transition = updateTransition(targetState = isAnimated, label = "transition")
//    val rocketOffset by transition.animateOffset(
//        transitionSpec = {
//            if (this.targetState) { tween(durationMillis = 10000)} // launch duration }
//            else { tween(durationMillis = 15000) } },
//        label = "rocket offset")
//    { animated -> if (animated) Offset(0f, 0f) else Offset(200f, 200f) }


    FloatingActionButton(onClick =  { deleteSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
//            .offset( rocketOffset.x.dp, rocketOffset.y.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab) {
        Icon(Icons.Filled.Delete,"")
    }
}
@Composable
fun FabChangeGroupProducts(changeGroupSelected: MutableState<Boolean>){

    FloatingActionButton(
        onClick =  { changeGroupSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab)
    {
        Icon(Icons.Filled.Dns,"")
    }
}
@Composable
fun FabUnSelectProducts(unSelected: MutableState<Boolean>){

    FloatingActionButton( onClick = { unSelected.value = true },
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp)),
        backgroundColor = BackgroundFab,
        contentColor = ContentFab) {
        Icon(Icons.Filled.RemoveDone,"")
    }
}
