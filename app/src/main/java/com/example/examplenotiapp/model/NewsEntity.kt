package com.example.examplenotiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Noticias")
data class NewsEntity(@PrimaryKey (autoGenerate = true) val id: Int = 0,
                      val articleId: String,
                      val title:String,
                      val nameAuthor: String,
                      val pubDate:String,        //Antes era userAuthor
                      val statementOfNew: String,
                      val link:String,           //Nuevos parametros
                      val imageAuthor:String     //Nuevos Parametros
)
