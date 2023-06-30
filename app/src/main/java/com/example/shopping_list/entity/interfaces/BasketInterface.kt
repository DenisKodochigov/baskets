package com.example.shopping_list.entity.interfaces

interface BasketInterface {
    var idBasket: Long
    var dateB: Long
    var nameBasket: String
    var fillBasket: Boolean
    var quantity: Int
    var isSelected:Boolean
    var position: Int
}