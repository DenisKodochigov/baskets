package com.example.basket.entity

interface Basket {
    var idBasket: Long
    var dateB: Long
    var nameBasket: String
    var fillBasket: Boolean
    var quantity: Int
    var isSelected:Boolean
    var position: Int
}