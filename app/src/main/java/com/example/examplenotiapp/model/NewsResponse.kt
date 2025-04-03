package com.example.examplenotiapp.model

import android.os.Parcelable.Creator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsResponse(
    @SerialName("status") var status:String,
    @SerialName("results") var results: List<NewsArticle>
)

@Serializable
data class NewsArticle(
    @SerialName("article_id") var article_id:String,
    @SerialName("title") var title:String?,
    @SerialName("source_name") var source_name:String?,
    @SerialName("pubDate") var pubDate:String?,
    @SerialName("description") var description: String?,
    @SerialName("link") var link:String?,
    @SerialName("image_url") var image_url:String?
)
