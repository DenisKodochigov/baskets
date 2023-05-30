package com.example.shopping_list.ui.baskets

import com.example.shopping_list.entity.Basket

data class BasketScreenState(
    val baskets: List<Basket> = emptyList(),
)
