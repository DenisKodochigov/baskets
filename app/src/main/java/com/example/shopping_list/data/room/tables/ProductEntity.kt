package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product

@Entity(tableName = "tb_product")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true) override var idProduct: Long ,
    override var value: Double,
    override var basketId: Long ,
    override var putInBasket:Boolean ,
    override var position: Int ,
    var articleId: Long ,
    @Ignore override var article: Article ,
    @Ignore override var isSelected: Boolean
): Product {
    constructor( value: Double, basketId: Long, putInBasket:Boolean, position: Int, articleId: Long)
            :this(0,0.0, 0, false, 0, 0,
                    ArticleEntity(0,"",0,0,0,
                        SectionEntity(),UnitEntity(0,"", false),
                        false), false)
    @Ignore constructor( value: Double, basketId: Long, position: Int, articleId: Long)
            :this(0,0.0, 0, false, 0, 0,
                    ArticleEntity(0,"",0,0,0,
                        SectionEntity(),UnitEntity(0,"", false),
                        false), false)
    @Ignore constructor( value: Double, putInBasket:Boolean, articleId: Long)
            :this(0,0.0, 0, false, 0, 0,
                    ArticleEntity(0,"",0,0,0,
                        SectionEntity(),UnitEntity(0,"", false),
                        false), false)


}
//
//data class ProductEntity (
//    @PrimaryKey(autoGenerate = true) override var idProduct: Long = 0,
//    override var value: Double = 0.0,
//    override var basketId: Long = 0,
//    override var putInBasket:Boolean = false,
//    override var position: Int = 0,
//    var articleId: Long = 0,
//    @Ignore override var article: Article = ArticleEntity(),
//    @Ignore override var isSelected: Boolean = false
//): Product {
//    //    @Ignore override var article: Article = ArticleEntity()
////    @Ignore override var isSelected: Boolean = false
////    constructor( idProduct: Long, value: Double, basketId: Long, putInBasket:Boolean, position : Int, articleId: Long, isSelected: Boolean)
////    :this(0, 0.0, 0, false, 0, 0, ArticleEntity(),false)
//    constructor( idProduct: Long, value: Double, basketId: Long, putInBasket:Boolean, position: Int, articleId: Long, article: ArticleEntity, isSelected: Boolean)
//            :this(0, 0.0, 0, false, 0, 0, ArticleEntity(),false)
//}