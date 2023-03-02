package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "films")
@TypeConverters(ConverterForFilmDB::class)
data class FilmDB(
    @PrimaryKey(autoGenerate = true) var idFilm: Int,
    val msg: String,
//    @Embedded var film: Film?
)
