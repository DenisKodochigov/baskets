package com.example.shopping_list.data.room;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DataDao _dataDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tb_basket` (`idBasket` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameBasket` TEXT NOT NULL, `fillBasket` INTEGER NOT NULL, `selected` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tb_basket_nameBasket` ON `tb_basket` (`nameBasket`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tb_product` (`idProduct` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `value` REAL NOT NULL, `basketId` INTEGER, `putInBasket` INTEGER NOT NULL, `articleId` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tb_article` (`idArticle` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameArticle` TEXT NOT NULL, `groupId` INTEGER, `unitId` INTEGER)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tb_article_nameArticle` ON `tb_article` (`nameArticle`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tb_group` (`idGroup` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameGroup` TEXT NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tb_group_nameGroup` ON `tb_group` (`nameGroup`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `tb_unit` (`idUnit` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nameUnit` TEXT NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tb_unit_nameUnit` ON `tb_unit` (`nameUnit`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ce7db884aa196fc77c6af37e53e13ca8')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `tb_basket`");
        _db.execSQL("DROP TABLE IF EXISTS `tb_product`");
        _db.execSQL("DROP TABLE IF EXISTS `tb_article`");
        _db.execSQL("DROP TABLE IF EXISTS `tb_group`");
        _db.execSQL("DROP TABLE IF EXISTS `tb_unit`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTbBasket = new HashMap<String, TableInfo.Column>(4);
        _columnsTbBasket.put("idBasket", new TableInfo.Column("idBasket", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbBasket.put("nameBasket", new TableInfo.Column("nameBasket", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbBasket.put("fillBasket", new TableInfo.Column("fillBasket", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbBasket.put("selected", new TableInfo.Column("selected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTbBasket = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTbBasket = new HashSet<TableInfo.Index>(1);
        _indicesTbBasket.add(new TableInfo.Index("index_tb_basket_nameBasket", true, Arrays.asList("nameBasket"), Arrays.asList("ASC")));
        final TableInfo _infoTbBasket = new TableInfo("tb_basket", _columnsTbBasket, _foreignKeysTbBasket, _indicesTbBasket);
        final TableInfo _existingTbBasket = TableInfo.read(_db, "tb_basket");
        if (! _infoTbBasket.equals(_existingTbBasket)) {
          return new RoomOpenHelper.ValidationResult(false, "tb_basket(com.example.shopping_list.data.room.tables.BasketEntity).\n"
                  + " Expected:\n" + _infoTbBasket + "\n"
                  + " Found:\n" + _existingTbBasket);
        }
        final HashMap<String, TableInfo.Column> _columnsTbProduct = new HashMap<String, TableInfo.Column>(5);
        _columnsTbProduct.put("idProduct", new TableInfo.Column("idProduct", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbProduct.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbProduct.put("basketId", new TableInfo.Column("basketId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbProduct.put("putInBasket", new TableInfo.Column("putInBasket", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbProduct.put("articleId", new TableInfo.Column("articleId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTbProduct = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTbProduct = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTbProduct = new TableInfo("tb_product", _columnsTbProduct, _foreignKeysTbProduct, _indicesTbProduct);
        final TableInfo _existingTbProduct = TableInfo.read(_db, "tb_product");
        if (! _infoTbProduct.equals(_existingTbProduct)) {
          return new RoomOpenHelper.ValidationResult(false, "tb_product(com.example.shopping_list.data.room.tables.ProductEntity).\n"
                  + " Expected:\n" + _infoTbProduct + "\n"
                  + " Found:\n" + _existingTbProduct);
        }
        final HashMap<String, TableInfo.Column> _columnsTbArticle = new HashMap<String, TableInfo.Column>(4);
        _columnsTbArticle.put("idArticle", new TableInfo.Column("idArticle", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbArticle.put("nameArticle", new TableInfo.Column("nameArticle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbArticle.put("groupId", new TableInfo.Column("groupId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbArticle.put("unitId", new TableInfo.Column("unitId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTbArticle = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTbArticle = new HashSet<TableInfo.Index>(1);
        _indicesTbArticle.add(new TableInfo.Index("index_tb_article_nameArticle", true, Arrays.asList("nameArticle"), Arrays.asList("ASC")));
        final TableInfo _infoTbArticle = new TableInfo("tb_article", _columnsTbArticle, _foreignKeysTbArticle, _indicesTbArticle);
        final TableInfo _existingTbArticle = TableInfo.read(_db, "tb_article");
        if (! _infoTbArticle.equals(_existingTbArticle)) {
          return new RoomOpenHelper.ValidationResult(false, "tb_article(com.example.shopping_list.data.room.tables.ArticleEntity).\n"
                  + " Expected:\n" + _infoTbArticle + "\n"
                  + " Found:\n" + _existingTbArticle);
        }
        final HashMap<String, TableInfo.Column> _columnsTbGroup = new HashMap<String, TableInfo.Column>(2);
        _columnsTbGroup.put("idGroup", new TableInfo.Column("idGroup", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbGroup.put("nameGroup", new TableInfo.Column("nameGroup", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTbGroup = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTbGroup = new HashSet<TableInfo.Index>(1);
        _indicesTbGroup.add(new TableInfo.Index("index_tb_group_nameGroup", true, Arrays.asList("nameGroup"), Arrays.asList("ASC")));
        final TableInfo _infoTbGroup = new TableInfo("tb_group", _columnsTbGroup, _foreignKeysTbGroup, _indicesTbGroup);
        final TableInfo _existingTbGroup = TableInfo.read(_db, "tb_group");
        if (! _infoTbGroup.equals(_existingTbGroup)) {
          return new RoomOpenHelper.ValidationResult(false, "tb_group(com.example.shopping_list.data.room.tables.GroupEntity).\n"
                  + " Expected:\n" + _infoTbGroup + "\n"
                  + " Found:\n" + _existingTbGroup);
        }
        final HashMap<String, TableInfo.Column> _columnsTbUnit = new HashMap<String, TableInfo.Column>(2);
        _columnsTbUnit.put("idUnit", new TableInfo.Column("idUnit", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTbUnit.put("nameUnit", new TableInfo.Column("nameUnit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTbUnit = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTbUnit = new HashSet<TableInfo.Index>(1);
        _indicesTbUnit.add(new TableInfo.Index("index_tb_unit_nameUnit", true, Arrays.asList("nameUnit"), Arrays.asList("ASC")));
        final TableInfo _infoTbUnit = new TableInfo("tb_unit", _columnsTbUnit, _foreignKeysTbUnit, _indicesTbUnit);
        final TableInfo _existingTbUnit = TableInfo.read(_db, "tb_unit");
        if (! _infoTbUnit.equals(_existingTbUnit)) {
          return new RoomOpenHelper.ValidationResult(false, "tb_unit(com.example.shopping_list.data.room.tables.UnitEntity).\n"
                  + " Expected:\n" + _infoTbUnit + "\n"
                  + " Found:\n" + _existingTbUnit);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ce7db884aa196fc77c6af37e53e13ca8", "aa4f541ba6f726e72418ca3f22b31842");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "tb_basket","tb_product","tb_article","tb_group","tb_unit");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `tb_basket`");
      _db.execSQL("DELETE FROM `tb_product`");
      _db.execSQL("DELETE FROM `tb_article`");
      _db.execSQL("DELETE FROM `tb_group`");
      _db.execSQL("DELETE FROM `tb_unit`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DataDao.class, DataDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public DataDao dataDao() {
    if (_dataDao != null) {
      return _dataDao;
    } else {
      synchronized(this) {
        if(_dataDao == null) {
          _dataDao = new DataDao_Impl(this);
        }
        return _dataDao;
      }
    }
  }
}
