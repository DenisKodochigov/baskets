package com.example.shopping_list.entity

data class ProductClass(
    override var idProduct: Long,
    override var basketId: Long,
    override var article: Article,
    override var value: Double,
    override var putInBasket: Boolean,
    override var isSelected: Boolean,
    override var position: Int
): Product
