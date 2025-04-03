package com.example.examplenotiapp.model

data class NewsModel(val articleId: String,
                     val title:String,
                     val nameAuthor: String,
                     val pubDate:String,            //Antes userAuthor
                     val statementOfNew: String,
                     val link:String,               //Nuevo Parametro
                     val imageAuthor:String,        //Nuevos Parametro
                     var expanded:Boolean = false
)
