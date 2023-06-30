package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.interfaces.ProductInterface

@Entity(tableName = "tb_product")
data class ProductTable (
    @PrimaryKey(autoGenerate = true) override var idProduct: Long = 0,
    override var value: Double = 0.0,
    override var basketId: Long = 0,
    override var putInBasket:Boolean = false,
    override var position : Int = 0,
    var articleId: Long = 0,
): ProductInterface {
    @Ignore override var article: Article = Article()
    @Ignore override var isSelected: Boolean = false
}