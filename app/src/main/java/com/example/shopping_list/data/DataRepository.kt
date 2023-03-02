package com.example.shopping_list.data

import com.example.shopping_list.entity.Box
import com.example.shopping_list.entity.Plug
import com.example.shopping_list.entity.Product
import javax.inject.Inject

class DataRepository @Inject constructor() {

    fun getBoxes(): List<Box>{
        return Plug.listBox
    }
    fun getProducts(): List<Product>{
        return Plug.listProduct
    }
}