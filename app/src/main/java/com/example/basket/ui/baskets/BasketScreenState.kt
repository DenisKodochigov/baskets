package com.example.basket.ui.baskets

import com.example.basket.entity.Basket

data class BasketScreenState(
    val baskets: List<Basket> = emptyList(),
)
