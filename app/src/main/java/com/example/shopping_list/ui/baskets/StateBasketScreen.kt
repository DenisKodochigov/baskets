package com.example.shopping_list.ui.baskets

import com.example.shopping_list.data.room.tables.BasketDB

data class StateBasketScreen(
    val baskets: List<BasketDB> = emptyList()
)
