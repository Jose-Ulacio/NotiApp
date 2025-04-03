package com.example.examplenotiapp

import android.util.Log
import com.example.examplenotiapp.database.NewsDatabase
import com.example.examplenotiapp.model.NewsArticle
import com.example.examplenotiapp.model.NewsEntity
import com.example.examplenotiapp.model.NewsModel
import com.example.examplenotiapp.model.NewsResponse
import com.example.examplenotiapp.retrofit.RetrofitHelper
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay

class NewsRepository(private val db: NewsDatabase) {

    // Ktor HttpClient
    private fun getClient(): HttpClient {
        return HttpClient(CIO)
    }

    suspend fun getNewsFromApi(): List<NewsArticle> {
        return try {
            val response: HttpResponse = getClient().get {
                url("${RetrofitHelper.BASE_URL}api/1/news")
                parameter("apikey", "pub_70235642aa002d5101c720baf213bb0cfadba")
                parameter("language", "es")
            }

            if (response.status.isSuccess()) {
                val data = Gson().fromJson<NewsResponse>(response.bodyAsText(), NewsResponse::class.java)
                Log.e("NewsRepository", "Data: $data")
                val newResult = mapperApi(data.results)
                saveNewsToDb(newResult)
                newResult

            } else if(response.status == HttpStatusCode.TooManyRequests){
                delay(5000)
                getNewsFromApi()
            } else {
                Log.e("NewsRepository", "Error: ${response.status.description}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("NewsRepository", "Exception: ${e.message}", e)
            emptyList()
        }
    }

    private fun mapperApi(list: List<NewsArticle>): List<NewsArticle>{
        return list.map {
            val newDescription = it.description ?: ""
            it.copy(
                description = newDescription.ifEmpty {
                    "Descripcion No disponible."
                }
            )
        }
    }

    suspend fun saveNewsToDb(news: List<NewsArticle>) {
        db.newsDao().deleteAllNews()

        val newsEntities = news.map {
            NewsEntity(
                articleId = it.article_id,
                title = it.title ?:"",
                nameAuthor = it.source_name?:"",
                pubDate = it.pubDate?:"",
                statementOfNew = it.description?:"",
                link = it.link?:"",
                imageAuthor = it.image_url?:""
            )
        }
        db.newsDao().insertNews(newsEntities)
    }

    suspend fun getNewsFromDb(): List<NewsModel> {
        val newsEntities = db.newsDao().getAllNews()
        return newsEntities.map {
            NewsModel(
                articleId = it.articleId,
                title = it.title,
                nameAuthor = it.nameAuthor,
                pubDate = it.pubDate,
                statementOfNew = it.statementOfNew,
                link = it.link,
                imageAuthor = it.imageAuthor
            )
        }
    }

    suspend fun deleteNews(articleId: String) {
        db.newsDao().deleteSelectNews(articleId)
    }
    
}


