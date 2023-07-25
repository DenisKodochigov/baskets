package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product

@Entity(tableName = "tb_product")
data class ProductDB(
    @PrimaryKey(autoGenerate = true) override var idProduct: Long,
    override var basketId: Long,
    override var value: Double,
    override var putInBasket: Boolean,
    override var position: Int,
    var articleId: Long,
    @Ignore override var isSelected: Boolean,
    @Ignore override var article: Article,
) : Product {
    @Ignore constructor(
        basketId: Long,
        value: Double,
        position: Int,
        articleId: Long,
    ) : this(
        idProduct = 0,
        value = 0.0,
        basketId = 0,
        putInBasket = false,
        position = 0,
        articleId = 0,
        isSelected = false,
        article = ArticleDB(
            idArticle = 0,
            nameArticle = "",
            position = 0,
            sectionId = 0,
            unitId = 0,
            section = SectionDB(),
            unitApp =  UnitDB(),
            isSelected = false,
        )
    ) {
        this.value = value
        this.basketId = basketId
        this.position = position
        this.articleId = articleId
    }
    @Ignore constructor(
        value: Double,
        putInBasket: Boolean,
        articleId: Long,
        article: ArticleDB
    ) : this(
        idProduct = 0,
        value = 0.0,
        basketId = 0,
        putInBasket = false,
        position = 0,
        articleId = 0,
        isSelected = false,
        article = ArticleDB(
            idArticle = 0,
            nameArticle = "",
            position = 0,
            sectionId = 0,
            unitId = 0,
            section = SectionDB(),
            unitApp =  UnitDB(),
            isSelected = false,
        )
    ) {
        this.value = value
        this.putInBasket = putInBasket
        this.articleId = articleId
        this.article = article
    }

    constructor(
        value: Double,
        basketId: Long,
        articleId: Long,
    ) : this(
        idProduct = 0,
        value = 0.0,
        basketId = 0,
        putInBasket = false,
        position = 0,
        articleId = 0,
        isSelected = false,
        article = ArticleDB(
            idArticle = 0,
            nameArticle = "",
            position = 0,
            sectionId = 0,
            unitId = 0,
            section = SectionDB(),
            unitApp =  UnitDB(),
            isSelected = false,
        )
    ) {
        this.value = value
        this.basketId = basketId
        this.articleId = articleId
    }

    @Ignore     constructor() : this(
        idProduct = 0,
        value = 0.0,
        basketId = 0,
        putInBasket = false,
        position = 0,
        articleId = 0,
        isSelected = false,
        article = ArticleDB(
            idArticle = 0,
            nameArticle = "",
            position = 0,
            sectionId = 0,
            unitId = 0,
            section = SectionDB(),
            unitApp =  UnitDB(),
            isSelected = false,
        )
    )
}
