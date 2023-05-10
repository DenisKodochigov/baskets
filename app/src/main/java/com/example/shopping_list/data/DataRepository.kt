package com.example.shopping_list.data

import android.util.Log
import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.BasketEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    /** Basket entity*/
    fun getListBasket(): List<Basket>{
        return dataSourceDB.getListBasketCount()
    }

    fun newBasket(basketName: String): List<Basket>{
        val currentTime = Date().time
        return dataSourceDB.newBasket(BasketEntity(nameBasket = basketName, dateB = currentTime))
    }

    fun changeNameBasket(basket: Basket): List<Basket>{
        return dataSourceDB.changeNameBasket(basket)
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        return dataSourceDB.deleteBasket(basketId)
    }

    /** Product entity*/
    fun getListProducts(basketId: Long): List<Product> = dataSourceDB.getListProducts(basketId)

    fun addProduct(product: Product, basketId: Long): List<Product>{
//        Log.d("KDS", " ${product}")
        return  if (product.article.nameArticle != "") {
            product.basketId = basketId
            product.position = dataSourceDB.getCountProductInBasket(basketId) + 1
            if (product.article.unitA!!.idUnit == 0L && product.article.unitA!!.nameUnit != "") {
                product.article.unitA!!.idUnit =
                    dataSourceDB.addUnit(product.article.unitA as UnitEntity)
            }
            if (product.article.group!!.idGroup == 0L && product.article.group!!.nameGroup != "") {
                product.article.group!!.idGroup =
                    dataSourceDB.addGroup(product.article.group as GroupEntity)
            }
            if (product.article.idArticle == 0L && product.article.nameArticle != "") {
                product.article.idArticle = dataSourceDB.addArticle(
                    ArticleEntity(
                        nameArticle = product.article.nameArticle,
                        groupId = product.article.group!!.idGroup,
                        unitId = product.article.unitA!!.idUnit
                    )
                )
            }
            dataSourceDB.addProduct(product)
        } else dataSourceDB.getListProducts(0L)
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        return dataSourceDB.putProductInBasket(product, basketId)
    }

    fun setPositionBasket(baskets: List<Basket>, direction: Int): List<Basket>{
        return dataSourceDB.setPositionBasket(baskets,direction)
    }

    fun setPositionProductInBasket(products: List<Product>, direction: Int): List<Product>{
        return dataSourceDB.setPositionProductInBasket(products,direction)
    }

    fun changeProductInBasket(product: Product, basketId: Long): List<Product>{
        return dataSourceDB.changeProductInBasket(product, basketId)
    }

    fun deleteSelectedProduct(productList: MutableList<Product>): List<Product>{
        return dataSourceDB.deleteSelectedProduct(productList)
    }

    /** Article entity*/
    fun getListArticle(): List<Article> =  dataSourceDB.getListArticle()

    fun newArticle(name: String): List<Article> {
        return dataSourceDB.newArticleS(name)
    }

    fun getGroups(): List<GroupArticle> = dataSourceDB.getGroups()

    fun getUnits(): List<UnitA> = dataSourceDB.getUnits()

    fun changeGroupSelected(productList: MutableList<Product>, idGroup: Long){

    }
//    fun getBasketProducts(basket:BasketDB): List<ProductDB>{
//        var listProduct = emptyList<ProductDB>()
//        dataSourceDB.getBasketProducts(basket).forEach { item->
//            listProduct = item.listProductDB
//        }
//        return listProduct
//    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return dataSourceDB.getGroupsWithProduct()
//    }
}