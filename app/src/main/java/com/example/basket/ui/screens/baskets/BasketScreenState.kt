package com.example.basket.ui.screens.baskets

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Basket

data class BasketScreenState(
    val baskets: List<Basket> = emptyList(),
    var refresh: Boolean = true,
    var changeNameBasket: (Basket) -> Unit = {},
    var deleteBasket: (Long) -> Unit = {},
    var onAddClick: (String) -> Unit = {},
    var onDismiss: () -> Unit = {},
    var triggerRunOnClickFAB: MutableState<Boolean> = mutableStateOf(false),
    var idImage: Int = 0,
    var screenTextHeader: String = "",
)
