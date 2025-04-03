package com.example.examplenotiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examplenotiapp.App
import com.example.examplenotiapp.NewsRepository
import com.example.examplenotiapp.model.NewsModel
import kotlinx.coroutines.launch

class NewsViewModel(application: Application): AndroidViewModel(application) {
    private val repository = NewsRepository(App.db)
    private val _newsList = MutableLiveData<List<NewsModel>>()
    val newsList: LiveData<List<NewsModel>> get() = _newsList

    private val _isLoadind = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoadind

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun searchNews(){
        viewModelScope.launch {
            _isLoadind.value = true      //mostrar ProgressBar
            _errorMessage.value = null   //Limpiar mensaje de Errrores

            try {
                val news = repository.getNewsFromApi()
                if (news.isNotEmpty()){
                    repository.saveNewsToDb(news)
                }
                val newsFromDb = repository.getNewsFromDb()
                _newsList.value = newsFromDb
            } catch (e: Exception){
                //Manejo de errores
                _errorMessage.value = "Error al Cargar Noticias: ${e.message}"
            } finally {
                _isLoadind.value = false    //ocultar progressBar
            }
        }
    }

    fun loadNewsFromDb(){
        viewModelScope.launch {
            _isLoadind.value = true
            try {
                val newsFromDb = repository.getNewsFromDb()
                _newsList.value = newsFromDb
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoadind.value = false
            }
        }
    }

    fun deleteNews(articleId: String) {
        viewModelScope.launch {
            repository.deleteNews(articleId)
            val updatedNews = repository.getNewsFromDb()
            _newsList.value = updatedNews
        }
    }
}