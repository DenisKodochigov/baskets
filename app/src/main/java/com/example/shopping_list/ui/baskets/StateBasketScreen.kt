package com.example.shopping_list.ui.baskets

import com.example.shopping_list.entity.Basket

data class StateBasketScreen(
    val baskets: MutableList<Basket> = mutableListOf(),
)
