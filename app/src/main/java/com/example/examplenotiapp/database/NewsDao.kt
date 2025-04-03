package com.example.examplenotiapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.examplenotiapp.model.NewsEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM Noticias")
    suspend fun getAllNews(): List<NewsEntity>

    @Insert
    suspend fun insertNews(news: List<NewsEntity>)

    @Query("DELETE FROM Noticias WHERE articleId = :articleId")
    suspend fun deleteSelectNews(articleId: String)

    @Query("DELETE FROM Noticias")
    suspend fun deleteAllNews()
}