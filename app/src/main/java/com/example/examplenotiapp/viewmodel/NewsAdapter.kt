package com.example.examplenotiapp.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.examplenotiapp.R
import com.example.examplenotiapp.databinding.ItemNewsBinding
import com.example.examplenotiapp.model.NewsArticle
import com.example.examplenotiapp.model.NewsModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class NewsAdapter (private var list: List<NewsModel>, onClickListener: OnClickListener ):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

        val event = onClickListener

    private lateinit var mBinding: ItemNewsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        mBinding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(mBinding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val element = list[position]
        holder.inicializarDesign(element)
    }

    inner class NewsViewHolder(vista: View): RecyclerView.ViewHolder(vista){

        private val titleNews : TextView = vista.findViewById(R.id.title_news)
        private val nameAuthor : TextView = vista.findViewById(R.id.nameAutor)
        private val pubDate : TextView = vista.findViewById(R.id.pubDate)
        private val infoNews : TextView = vista.findViewById(R.id.infoNews)
        private val cardview : MaterialCardView = vista.findViewById(R.id.cardCover)
        private val imageAuthor : ImageView = vista.findViewById(R.id.imgAutor)
        private val btnMoreInfo : MaterialButton = vista.findViewById(R.id.btnMoreInfo)

        fun inicializarDesign(element: NewsModel){
            titleNews.text = element.title
            nameAuthor.text = element.nameAuthor
            pubDate.text = element.pubDate
            infoNews.text = element.statementOfNew
            cardview.setOnClickListener { event.onClick(element) }

            if (!element.imageAuthor.isNullOrEmpty()){
                Glide.with(itemView)
                    .load(element.imageAuthor)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .circleCrop() //colocar la foto en forma circular
                    .into(imageAuthor)
            } else {
                imageAuthor.setImageResource(R.drawable.baseline_broken_image_24)
            }

            btnMoreInfo.setOnClickListener { event.onClickBtn(element) }


            //Configurar Visiviladad de la Descripcion de la Noticia
            if (element.expanded){
                infoNews.visibility = View.VISIBLE
            } else {
                infoNews.visibility = View.GONE
            }

            //Long Click para monstrar el AlertDialog que elimina la Noticia
            cardview.setOnLongClickListener {
                event.onLongClick(element)
                true
            }
        }
    }
}
