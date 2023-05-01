package com.example.shopping_list.data.room.tables.relation

import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Product

data class BasketWithProducts(
    var basket: Basket,
    var products: List<Product>
)
