package com.example.examplenotiapp.viewmodel

import com.example.examplenotiapp.model.NewsModel

interface OnClickListener {
    fun onClick(newsModel: NewsModel)
    fun onLongClick(newsModel: NewsModel)
    fun onClickBtn(newsModel: NewsModel)
}
