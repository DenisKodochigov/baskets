package com.example.shopping_list.data.room

import androidx.room.*
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj

@Dao
interface DataDao {
    @Update
    fun update(basket: BasketDB)

    @Insert
    fun newBasket(basket: BasketDB): Long

    @Query("SELECT idBasket FROM tb_basket WHERE nameBasket = :basketName")
    fun checkBasketFromName(basketName: String): Long?

    @Query("DELETE FROM tb_basket WHERE idBasket = :id")
    fun deleteByIdBasket(id: Long)
    @Query("DELETE FROM tb_product WHERE basketId = :id")
    fun deleteByIdBasketProduct(id: Long)
    @Query("SELECT * FROM tb_basket ORDER BY position ASC")
    fun getListBasket(): List<BasketDB>
    @Query("UPDATE tb_basket SET nameBasket = :newName WHERE idBasket =:basketId")
    fun changeNameBasket(basketId: Long, newName: String)
    @Query("SELECT nameBasket FROM tb_basket WHERE idBasket = :basketId")
    fun getNameBasket(basketId: Long): String
    @Query("UPDATE tb_basket SET position = :position WHERE idBasket=:basketId ")
    fun setPositionBasket(basketId: Long, position: Int)

    /** Product entity*/
    @Insert
    fun addProduct(product: ProductDB): Long

    @Query("SELECT idProduct FROM tb_product JOIN tb_article ON tb_article.nameArticle = :name " +
                "WHERE tb_product.articleId = tb_article.idArticle")
    fun checkProductFromName(name: String): Long?

    @Query("SELECT idProduct FROM tb_product WHERE articleId = :id AND basketId = :basketId")
    fun checkArticleInBasket(basketId: Long, id: Long): Long?

    @Transaction
    @Query("SELECT * FROM tb_product WHERE basketId = :basketId")
    fun getListProduct(basketId: Long): List<ProductObj>

    @Query("SELECT COUNT(idProduct) FROM tb_product WHERE basketId = :basketId")
    fun countProductInBasket(basketId: Long): Int

    @Transaction
    @Query("SELECT * FROM tb_product")
    fun getListProductAll(): List<ProductObj>

    @Query("UPDATE tb_product SET value = :value WHERE  idProduct=:productId AND basketId =:basketId ")
    fun setValueProduct(productId: Long, basketId: Long, value: Double)

    @Query("UPDATE tb_product SET putInBasket = NOT putInBasket " +
                "WHERE idProduct=:productId AND basketId =:basketId ")
    fun putProductInBasket(productId: Long, basketId: Long)

    @Query("UPDATE tb_product SET position = :position " +
                "WHERE idProduct=:productId AND basketId =:basketId ")
    fun setPositionProductInBasket(productId: Long, basketId: Long, position: Int)

    @Query("DELETE FROM tb_product WHERE idProduct=:productId AND basketId=:basketId")
    fun deleteProduct(productId: Long, basketId: Long)

    @Query("DELETE FROM tb_product WHERE basketId=:basketId AND idProduct IN (:listId)")
    fun deleteProducts(listId: List<Long>, basketId: Long)

    @Delete
    fun removeProduct(product: ProductDB)

    /** Article entity*/
    @Query("DELETE FROM tb_article WHERE idArticle =:articleId")
    fun delArticle(articleId: Long)

    @Insert
    fun addArticle(article: ArticleDB): Long

    @Update
    fun updateArticle(article: ArticleDB)

    @Transaction
    @Query("SELECT * FROM tb_article ORDER BY position ASC")
    fun getListArticle(): List<ArticleObj>
    @Transaction
    @Query("SELECT * FROM tb_article ORDER BY nameArticle ASC")
    fun getListArticleSortName(): List<ArticleObj>

    @Query("UPDATE tb_article SET unitId = :unitId WHERE idArticle =:articleId")
    fun setUnitInArticle(articleId: Long, unitId: Long)

    @Query("UPDATE tb_article SET sectionId = :id WHERE idArticle IN (:articles)")
    fun changeSectionArticle( articles: List<Long>, id: Long)

    @Query("SELECT unitId FROM tb_article WHERE idArticle =:articleId")
    fun getIdUnitFromArticle(articleId: Long): Long

    @Update
    fun changeArticle(article: ArticleDB)

    @Query("SELECT idProduct FROM tb_product WHERE articleId =:articleId")
    fun checkArticleWithProduct(articleId: Long): List<Long>

    @Query("UPDATE tb_article SET position = :position WHERE idArticle=:articleId ")
    fun setPositionArticle(articleId: Long, position: Int)

    /** Section entity*/

    @Query("SELECT * FROM tb_section")
    fun getSections(): List<SectionDB>

    @Insert
    fun addSection(section: SectionDB): Long
    @Query("SELECT * FROM tb_section WHERE idSection = :id")
    fun getSection(id: Long): SectionDB
    @Query("SELECT * FROM tb_section WHERE nameSection = :name")
    fun getSection(name: String): SectionDB?
    @Update
    fun changeSection(section: SectionDB)
    @Query("UPDATE tb_section SET colorSection = :colorLong WHERE idSection = :sectionId ")
    fun changeSectionColor(sectionId: Long, colorLong: Long)


    /** Unit entity*/

    @Insert
    fun addUnit(unit: UnitDB): Long
    @Update
    fun changeUnit(unitA: UnitDB)
    @Query("SELECT * FROM tb_unit")
    fun getUnits(): List<UnitDB>

    @Query("SELECT * FROM tb_unit WHERE idUnit = :id")
    fun getUnit(id: Long): UnitDB
    @Query("SELECT * FROM tb_unit WHERE nameUnit = :name")
    fun getUnit(name: String): UnitDB?
//    @Query("SELECT * FROM tb_unit")
//    fun getUnitsFlow(): Flow<List<UnitEntity>>
    @Query("DELETE FROM tb_unit WHERE idUnit IN (:listId)")
    fun deleteUnits(listId: List<Long>)
    @Query("DELETE FROM tb_unit WHERE idUnit = :id")
    fun deleteUnit(id: Long)
    @Query("SELECT idArticle FROM tb_article WHERE unitId = :id")
    fun checkUnit(id: Long): Long?
}
