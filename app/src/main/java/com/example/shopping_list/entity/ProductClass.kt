package com.example.shopping_list.entity

data class ProductClass(
    override val idProduct: Long,
    override val basketId: Long,
    override val article: Article,
    override val value: Double,
    override val putInBasket: Boolean,
    override var isSelected: Boolean,
    override var position: Int
): Product
