package com.example.basket.ui.screens.products

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

data class ProductsScreenState(
    val products: List<List<Product>> = emptyList(),
    val articles: List<Article> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitApp: List<UnitApp> = emptyList(),
    val nameBasket: String = "",
    var refresh: Boolean = true,
    var putProductInBasket: (Product) -> Unit = {},
    var changeProduct: (Product) -> Unit = {},
    var doChangeSection: (List<Product>, Long) -> Unit = { _,_->  },
    var doDeleteSelected: (List<Product>) -> Unit = {},
    var doSelected: (Long) -> Unit = {},
    var onAddProduct: (Product) -> Unit = {},
    var onDismiss:() -> Unit = {},
    var triggerRunOnClickFAB: MutableState<Boolean> = mutableStateOf(false),
)
