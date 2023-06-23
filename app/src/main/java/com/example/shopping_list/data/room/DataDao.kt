package com.example.shopping_list.data.room

import androidx.room.*
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {
    @Update
    fun update(basket: BasketEntity)

    @Insert
    fun newBasket(basket: BasketEntity): Long

    @Query("SELECT idBasket FROM tb_basket WHERE nameBasket = :basketName")
    fun checkBasketFromName(basketName: String): Long?

    @Query("DELETE FROM tb_basket WHERE idBasket = :id")
    fun deleteByIdBasket(id: Long)
    @Query("DELETE FROM tb_product WHERE basketId = :id")
    fun deleteByIdBasketProduct(id: Long)
    @Query("SELECT * FROM tb_basket ORDER BY position ASC")
    fun getListBasket(): List<BasketEntity>
    @Query("UPDATE tb_basket SET nameBasket = :newName WHERE idBasket =:basketId")
    fun changeNameBasket(basketId: Long, newName: String)
    @Query("SELECT nameBasket FROM tb_basket WHERE idBasket = :basketId")
    fun getNameBasket(basketId: Long): String
    @Query("UPDATE tb_basket SET position = :position WHERE idBasket=:basketId ")
    fun setPositionBasket(basketId: Long, position: Int)

    /** Product entity*/
    @Insert
    fun addProduct(product: ProductEntity): Long

    @Query("SELECT idProduct FROM tb_product JOIN tb_article ON tb_article.nameArticle = :name " +
                "WHERE tb_product.articleId = tb_article.idArticle")
    fun checkProductFromName(name: String): Long?

    @Query("SELECT idProduct FROM tb_product WHERE idProduct = :productId AND basketId = :basketId")
    fun checkProductInBasket(basketId: Long, productId: Long): Long?

    @Transaction
    @Query("SELECT * FROM tb_product WHERE basketId = :basketId " +
                "ORDER BY putInBasket DESC, position ASC")
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
    fun removeProduct(product: ProductEntity)

    /** Article entity*/
    @Query("DELETE FROM tb_article WHERE idArticle =:articleId")
    fun delArticle(articleId: Long)

    @Insert
    fun addArticle(article: ArticleEntity): Long

    @Transaction
    @Query("SELECT * FROM tb_article ORDER BY position ASC, nameArticle ASC")
    fun getListArticle(): List<ArticleObj>
    @Transaction
    @Query("SELECT * FROM tb_article ORDER BY nameArticle ASC")
    fun getListArticleSortName(): List<ArticleObj>
    @Query("UPDATE tb_article SET unitId = :unitId WHERE idArticle =:articleId")
    fun setUnitInArticle(articleId: Long, unitId: Long)

    @Query("UPDATE tb_article SET sectionId = :id WHERE idArticle IN (:articles)")
    fun changeSectionArticle(id: Long, articles: List<Long>)

    @Query("SELECT unitId FROM tb_article WHERE idArticle =:articleId")
    fun getIdUnitFromArticle(articleId: Long): Long

    @Update
    fun changeArticle(article: ArticleEntity)

    @Query("SELECT idProduct FROM tb_product WHERE articleId =:articleId")
    fun checkArticleWithProduct(articleId: Long): List<Long>

    @Query("UPDATE tb_article SET position = :position WHERE idArticle=:articleId ")
    fun setPositionArticle(articleId: Long, position: Int)

    /** Section entity*/

    @Query("SELECT * FROM tb_section")
    fun getSections(): List<SectionEntity>
    @Query("SELECT * FROM tb_section")
    fun getSectionFlow(): Flow<List<SectionEntity>>
    @Insert
    fun addSection(section: SectionEntity): Long
    @Query("SELECT * FROM tb_section WHERE idSection = :id")
    fun getSection(id: Long): SectionEntity


    /** Unit entity*/

    @Insert
    fun addUnit(unit: UnitEntity): Long
    @Update
    fun changeUnit(unitA: UnitEntity)
    @Query("SELECT * FROM tb_unit")
    fun getUnits(): List<UnitEntity>

    @Query("SELECT * FROM tb_unit WHERE idUnit = :id")
    fun getUnit(id: Long): UnitEntity
    @Query("SELECT * FROM tb_unit")
    fun getUnitsFlow(): Flow<List<UnitEntity>>
    @Query("DELETE FROM tb_unit WHERE idUnit IN (:listId)")
    fun deleteUnits(listId: List<Long>)
    @Query("DELETE FROM tb_unit WHERE idUnit = :id")
    fun deleteUnit(id: Long)
    @Query("SELECT idArticle FROM tb_article WHERE unitId = :id")
    fun checkUnit(id: Long): Long?
}
