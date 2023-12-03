package com.example.basket.ui.screens.products

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

data class ProductsScreenState(
    val products: MutableState<List<List<Product>>> = mutableStateOf(emptyList()),
    val articles: MutableState<List<Article>> = mutableStateOf(emptyList()),
    val sections: MutableState<List<Section>> = mutableStateOf(emptyList()),
    val unitApp: MutableState<List<UnitApp>> = mutableStateOf(emptyList()),
    var triggerRunOnClickFAB: MutableState<Boolean> = mutableStateOf(false),
    val editProduct: MutableState<Product?> = mutableStateOf(null),
    var onAddProduct: (Product) -> Unit = {},
    var changeProduct: (Product) -> Unit = {},
    var doChangeSectionSelected: (List<Product>, Long) -> Unit = { _, _->  },
    var doDeleteSelected: (List<Product>) -> Unit = {},
    var doSelected: (Long) -> Unit = {},
    var doUnSelected: () -> Unit = {},
    var putProductInBasket: (Product) -> Unit = {},

    var idImage: Int = 0,
    var screenTextHeader: String = "",
    val nameBasket: String = "",
)

