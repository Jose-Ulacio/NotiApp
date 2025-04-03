package com.example.examplenotiapp.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.examplenotiapp.App
import com.example.examplenotiapp.databinding.ActivityMainBinding
import com.example.examplenotiapp.model.NewsEntity
import com.example.examplenotiapp.model.NewsModel
import com.example.examplenotiapp.viewmodel.NewsAdapter
import com.example.examplenotiapp.viewmodel.NewsViewModel
import com.example.examplenotiapp.viewmodel.OnClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter

    private lateinit var viewModel: NewsViewModel

    private val listNews = mutableListOf<NewsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        //Inicializar ViewModel
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // Configurar RecyclerView
        adapter = NewsAdapter(listNews, this)
        val recyclerView: RecyclerView = mBinding.reclyclerView
        recyclerView.adapter = adapter

        // Observar los datos del ViewModel
        viewModel.newsList.observe(this, Observer { news ->
            //adapter.updateList(news)
            listNews.clear()
            listNews.addAll(news)
            adapter.notifyDataSetChanged()
            hideProgressBar()
        })

        //Observar el estado de la carga
        viewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading){
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })

        //Observar Errores
        viewModel.errorMessage.observe(this, Observer { error ->
            if (error != null){
                shorError(error)
            }
        })

        // Obtener noticias al iniciar la Aplicacion
        if (isNetworkAvailable(this)) {
            loadNewsFromApi()
        } else {
            loadNewsFromDb()
            shorError("No hay conexión a Internet. Mostrando noticias almacenadas.")
        }

        //Actualizar Noticias
        mBinding.btnRefresh.setOnClickListener {
            if (isNetworkAvailable(this)){
                loadNewsFromApi()
            } else {
                shorError("No hay conexión a Internet. Verifica tu conexión.")
            }
        }


    }

    private fun loadNewsFromApi(){
        viewModel.searchNews()
    }

    private fun loadNewsFromDb(){
        viewModel.loadNewsFromDb()
        viewModel.newsList.observe(this, Observer { news ->
            if (news.isEmpty()){
                shorError("No hay noticias almacenadas. Conéctate a internet para obtener noticias.")
            }
        })
    }

    //Funciones del ClickListener
    override fun onClick(newsModel: NewsModel) {
        //Cambia el estado Expanded de la Noticia Clickeada
        newsModel.expanded = !newsModel.expanded

        //Cierra las demas Noticias
        listNews.forEach {
            if (it != newsModel) {
                it.expanded = false
            }
        }
        //Actualiza el Adaptador
        adapter.notifyDataSetChanged()
    }

        //Eliminar la Tienda Seleccionada
    override fun onLongClick(newsModel: NewsModel) {
        showAlertDialog(newsModel)
    }

    override fun onClickBtn(newsModel: NewsModel) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("link", newsModel.link)
        startActivity(intent)
    }

    //Utils, Esto podria pasarse a otra clase?
    private fun showProgressBar(){
        mBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        mBinding.progressBar.visibility = View.GONE
    }

    private fun shorError(message: String){
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    //Funcion para determinar si hay o no conexion a internet
    fun isNetworkAvailable(context: Context): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showAlertDialog(newsModel: NewsModel) {
        MaterialAlertDialogBuilder(this)
            .setTitle("¿Desea Eliminar la Noticia?")
            .setPositiveButton("Eliminar Noticia") { _, _ ->
                viewModel.deleteNews(newsModel.articleId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}

