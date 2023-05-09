package com.example.shopping_list.data.room.tables.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.entity.Article

data class ArticleObj(
    @Embedded val article: ArticleEntity,
    @Relation(
        parentColumn = "unitId",
        entityColumn = "idUnit"
    ) val unitA: UnitEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "idGroup"
    ) val group: GroupEntity,
)
data class ProductObj (
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "idArticle",
        entity = ArticleEntity::class
    ) val article: ArticleObj
)

data class BasketCountObj(
    @Embedded var basket: BasketEntity,
    @ColumnInfo var count: Int
)
//data class ProductForBasket (
//    @Embedded val basket: BasketTable,
//    @Relation(
//        parentColumn = "productId",
//        entityColumn = "idProduct"
//    ) val product: ProductTable
//)
//
//data class BasketObj(
//    var basket: Basket,
//    var products: List<ProductObj>
//)
//
//data class ProductObj(
//    var product: Product,
//    var article: ArticleObj,
//)


//data class ArticleRec(
//    @Embedded val article: ArticleTable,
//    @Embedded val unit: UnitTable
//)