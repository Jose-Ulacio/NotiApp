package com.example.examplenotiapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.examplenotiapp.model.NewsEntity

@Database(
    entities = [NewsEntity::class], version = 3  //Cambiado de 1 a 2
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
