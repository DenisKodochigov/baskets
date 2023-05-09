package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product

@Entity(tableName = "tb_product")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true) override var idProduct: Long = 0,
    override var value: Double = 0.0,
    override var basketId: Long? = null,
    override var putInBasket:Boolean = false,
    override var position : Int = 0,
    var articleId: Long? = null,
    @Ignore override var article: Article = ArticleEntity(),
    @Ignore override var isSelected: Boolean = false,
): Product