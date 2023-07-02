package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product

@Entity(tableName = "tb_product")
data class ProductDB(
    @PrimaryKey(autoGenerate = true) override var idProduct: Long,
    override var value: Double,
    override var basketId: Long,
    override var putInBasket: Boolean,
    override var position: Int,
    var articleId: Long,
    @Ignore override var article: Article,
    @Ignore override var isSelected: Boolean
) : Product {
    constructor(value: Double, basketId: Long, putInBasket: Boolean, position: Int, articleId: Long)
            : this(
        0, 0.0, 0, false, 0, 0,
        ArticleDB(
            0, "", 0, 0, 0,
            SectionDB(), UnitDB(0, "", false),
            false
        ), false
    )

    @Ignore
    constructor(value: Double, basketId: Long, position: Int, articleId: Long) :
    this( 0, 0.0, 0, false, 0, 0,
        ArticleDB(0, "", 0, 0, 0, SectionDB(), UnitDB(0, "", false), false), false)
    @Ignore
    constructor(articleId: Long, value: Double, basketId: Long) :
            this( 0, 0.0, 0, false, 0, 0,
                ArticleDB(0, "", 0, 0, 0, SectionDB(), UnitDB(0, "", false), false), false)
//    @Ignore
//    constructor(value: Double, putInBasket: Boolean, articleId: Long)
//            : this(
//        0, 0.0, 0, false, 0, 0,
//        ArticleEntity(
//            0, "", 0, 0, 0,
//            SectionEntity(), UnitEntity(0, "", false),
//            false
//        ), false
//    )

    @Ignore
    constructor(articleId: Long, value: Double, putInBasket: Boolean, article: ArticleDB)
            : this(0, 0.0, 0, false, 0, 0,
                ArticleDB( 0, "", 0, 0, 0, SectionDB(), UnitDB(0, "", false), false),
                false
    )

    @Ignore
    constructor(
        idProduct: Long,
        basketId: Long,
        article: ArticleDB,
        value: Double,
        putInBasket: Boolean,
        isSelected: Boolean,
        position: Int
    ) : this(0, 0.0, 0, false, 0, 0,
        ArticleDB( 0, "", 0, 0, 0, SectionDB(), UnitDB(0, "", false), false), false
    )
}
