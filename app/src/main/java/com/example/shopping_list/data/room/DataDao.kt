package com.example.shopping_list.data.room

import androidx.room.*
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj

@Dao
interface DataDao {

/** Basket entity*/
    @Update
    fun update(basket: BasketEntity)

    @Insert
    fun newBasket(basket: BasketEntity): Long

    @Query("SELECT idBasket FROM tb_basket WHERE nameBasket = :basketName")
    fun checkBasketFromName(basketName: String): Long?

    @Query("DELETE FROM tb_basket WHERE idBasket = :id")
    fun deleteByIdBasket(id:Int)

    @Query("SELECT * FROM tb_basket")
    fun getListBasket(): List<BasketEntity>

/** Product entity*/

    @Insert
    fun addProduct(product: ProductEntity): Long

    @Query("SELECT idProduct FROM tb_product " +
            "JOIN tb_article ON tb_article.nameArticle = :name " +
            "WHERE tb_product.articleId = tb_article.idArticle")
    fun checkProductFromName(name: String): Long?

    @Query("SELECT idProduct FROM tb_product " +
            "WHERE idProduct = :productId AND basketId = :basketId")
    fun checkProductInBasket(basketId:Long, productId: Long): Long?

    @Transaction
    @Query("SELECT * FROM tb_product WHERE basketId = :basketId")
    fun getListProduct(basketId: Long): List<ProductObj>

    @Transaction
    @Query("SELECT * FROM tb_product")
    fun getListProductAll(): List<ProductObj>

    @Query("DELETE FROM tb_product WHERE idProduct=:productId AND basketId =:basketId")
    fun deleteSelectedProduct(productId:Long,basketId: Long)

    @Query("UPDATE tb_product SET value = :value WHERE  idProduct=:productId AND basketId =:basketId ")
    fun setValueProduct(productId:Long,basketId: Long, value: Double)

/** Article entity*/
    @Insert
    fun addArticle(article: ArticleEntity): Long

    @Transaction
    @Query("SELECT * FROM tb_article ")
    fun getListArticle(): List<ArticleObj>

    @Query("UPDATE tb_article SET unitId = :unitId WHERE idArticle =:articleId")
    fun setUnitInArticle(articleId:Long,unitId: Long)

    @Query("SELECT unitId FROM tb_article WHERE idArticle =:articleId")
    fun getIdUnitFromArticle(articleId:Long): Long

/** Group entity*/

    @Query("SELECT * FROM tb_group")
    fun getGroups(): List<GroupEntity>

    @Insert
    fun addGroup(group: GroupEntity): Long

/** Unit entity*/
    @Insert
    fun addUnit(unit: UnitEntity): Long

    @Query("SELECT * FROM tb_unit")
    fun getUnits(): List<UnitEntity>

    @Query("UPDATE tb_product SET putInBasket = NOT putInBasket WHERE idProduct=:productId AND basketId =:basketId " )
    fun putProductInBasket(productId:Long,basketId: Long)

//    @Query("SELECT * FROM basket JOIN products ON basket.productId = products.idProduct" +
//            "JOIN article ON products.articleId = article.idArticle " +
//            "JOIN tb_unit ON article.unitId = tb_uit.idUnit WHERE basketId=:basketId")
//    fun getListProducts(basketId: Int): List<ProductTable>
//    @Transaction
//    @Query("SELECT * FROM basket WHERE idBasket = :basket_id")
//    fun getBasketWithProducts(basket_id: Int): List<BasketWithProduct>

////    @Transaction
////    @Query("SELECT * FROM Basket WHERE idBasket = :basket_id")
////    fun getBasketWithProducts(basket_id: Int): List<BasketWithProduct>

//    @Transaction
//    @Query("SELECT * FROM mygroup")
//    fun getGroupsWithProducts(): List<GroupWithProducts>
//
//    @Transaction
//    @Query("SELECT * FROM mygroup WHERE idGroup = :id")
//    fun getGroupWithProducts(id:Int): GroupWithProducts
//
//    @Transaction
//    @Query("SELECT * FROM myunit WHERE idUnit = :id")
//    fun getUnitWithProducts(id:Int): List<UnitWithProducts>


//    @Query("SELECT * FROM baskettoproduct WHERE basket_id = :idBasket")
//    fun getBasketProducts(idBasket: Int): List<ProductDB>
    //Insert new record to the table films
//    @Insert(entity = BasketDB::class)
//    fun insert(vararg data: BasketDB)
//    //Updating a record in the table films
//    @Update
//    fun update(film: BasketDB)
//    //Clearing the table films
//    @Query("DELETE FROM films")
//    fun nukeTable()
//    //Select all entries and sort by id
//    @Query("SELECT * FROM films ORDER BY idFilm DESC")
//    fun getAll(): BasketDB
//    //Select filmDB bi id
//    @Query("SELECT * FROM films WHERE idFilm = :id")
//    fun getFilm(id: Int): BasketDB?
//    //Select all entries from films
//    @Query("SELECT * FROM films")
//    fun getFilms(): List<BasketDB>?
//    //Get the VIEWED parameter for recording with id = id
//    @Query("SELECT viewed FROM films WHERE idFilm = :id")
//    fun getViewed(id: Int): Boolean
//    //Select list value filmId from the table films where viewed = :viewed
//    @Query("SELECT filmId FROM films WHERE viewed = :viewed")
//    fun getViewedFilms(viewed: Boolean): List<Int>
//    //Setting the viewed parameter in all records
//    @Query("UPDATE films SET viewed = :value ")
//    fun setAllViewed(value: Boolean)
//    //Creating a stream to a movie in viewed state
//    @Query("SELECT viewed FROM films WHERE idFilm = :id")
//    fun setViewedFlow(id: Int): Flow<Boolean>
//    //Delete record in the table films where id=id
//    @Query("DELETE FROM films WHERE idFilm = :id")
//    fun deleteByIdFilmDB(id: Int)
//    //Selection of records from the FILES table where shs belong to the list
//    @Query("SELECT * FROM films WHERE idFilm = :listId")
//    fun getFilmInList(listId: List<Int>): List<BasketDB>
//
////Table COLLECTIONS
//    //Insert new record to the table collections
//    @Insert(entity = ProductDB::class)
//    fun insert(vararg data: ProductDB)
//    //Delete record in the table collections where id=id
//    @Query("DELETE FROM collections WHERE idCollection = :id")
//    fun deleteByIdCollection(id:Int)
//    //Select all records from the table collections
//    @Query("SELECT * FROM collections ")
//    fun getCollections(): List<ProductDB>
//    //Selecting a record from a table collections where name =
//    @Query("SELECT * FROM collections WHERE name = :collectionName ")
//    fun getCollectionRecord(collectionName: String): ProductDB?
//
////Table COLLECTIONS WITH FILM
//    //Insert new record to the table CrossFC
//    @Insert(entity = BasketToProduct::class, onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vararg data: BasketToProduct)
//    //Updating a record in the table CrossFC
//    @Update
//    fun update(data: BasketToProduct)
//    // FAVORITE
//    //Creating a stream to include a movie in the favorites collection
//    @Query("SELECT value FROM crossFC WHERE collection_id = '1' AND film_id = :id")
//    fun setFavoriteFlow(id: Int): Flow<Boolean>
//    //Request for the film to belong to the favorites collection
//    @Query("SELECT value FROM crossFC WHERE collection_id = '1' AND film_id = :id")
//    fun getFavorite(id: Int): Boolean
//    //BOOKMARK
//    //Creating a stream to include a movie in the bookmark collection
//    @Query("SELECT value FROM crossFC WHERE collection_id = '2' AND film_id = :id")
//    fun setBookmarkFlow(id: Int): Flow<Boolean>
//    //Request for the film to belong to the bookmark collection
//    @Query("SELECT value FROM crossFC WHERE collection_id = '2' AND film_id = :id")
//    fun getBookmark(id: Int): Boolean
//    //Selecting a count of movies added to the collection
//    @Query("SELECT * FROM crossFC WHERE collection_id = :id")
//    fun getCountFilmCollection(id: Int): List<BasketToProduct>
//    //Request an entry from the table crossFC for the movie belonging to the collection
//    @Query("SELECT id FROM crossFC WHERE collection_id = :collectionId AND film_id = :filmId")
//    fun getFilmInCollection( filmId: Int,collectionId: Int): Int?
//    //Request for the film to belong to the collection
//    @Query("SELECT 1 FROM crossFC WHERE film_id = :filmId")
//    fun existFilmInCollections( filmId: Int): Boolean
//    //Deleting record in table crossFC by id
//    @Query("DELETE FROM crossFC WHERE id = :id")
//    fun deleteByIdCrossFC(id:Int)
//    //Clear all link collection to film for collection
//    @Query("DELETE FROM crossFC WHERE collection_id = :id")
//    fun clearByCollectionIdCrossFC(id:Int)
//    //Selecting a list of movies-id added to the collection
//    @Query("SELECT film_id FROM crossFC WHERE collection_id = :collectionId")
//    fun getListFilmsInCollection(collectionId: Int): List<Int>
}
