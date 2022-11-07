package com.builditcreative.newsapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.builditcreative.newsapp.R
import com.builditcreative.newsapp.models.ArticleResponse
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

internal class NewsAdapter(
    var context: Context,
    var response: ArticleResponse,
    var recyclerView: RecyclerView
) :
    RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private lateinit var responses: ArticleResponse
    private lateinit var recycleView:RecyclerView

    init {
        this.responses = response
        this.recycleView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.list_news, parent, false)
        v.setOnClickListener{
            var intent = Intent(context,NewsActivity::class.java)
            intent.putExtra("Title",response.articles?.get(recycleView.getChildAdapterPosition(v))?.title)
            intent.putExtra("Detail",covertTimeToText(response.articles?.get(recycleView.getChildAdapterPosition(v))?.publishedAt)+" "+responses.articles?.get(recycleView.getChildAdapterPosition(v))?.author)
            intent.putExtra("Description",response.articles?.get(recycleView.getChildAdapterPosition(v))?.description)
            intent.putExtra("Image",response.articles?.get(recycleView.getChildAdapterPosition(v))?.urlToImage)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
        return MyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.titleText.text = responses.articles?.get(position)?.title
        holder.descriptionText.text = responses.articles?.get(position)?.description
        holder.authorText.text = responses.articles?.get(position)?.author
        holder.agoText.text = covertTimeToText(responses.articles?.get(position)?.publishedAt)
        Glide.with(context).load(responses.articles?.get(position)?.urlToImage).into(holder.image)
    }



    override fun getItemCount(): Int {
        return 100
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleText: TextView
        var descriptionText: TextView
        var authorText: TextView
        var agoText: TextView
        var image: ShapeableImageView


        init {
            titleText = itemView.findViewById(R.id.title_text)
            descriptionText = itemView.findViewById(R.id.description_text)
            authorText = itemView.findViewById(R.id.author_text)
            agoText = itemView.findViewById(R.id.ago_text)
            image = itemView.findViewById(R.id.imageView)
        }
    }

    fun covertTimeToText(dataDate: String?): String? {
        var convTime: String? = null
        val prefix = ""
        val suffix = "Ago •"
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val pasTime: Date = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff: Long = nowTime.getTime() - pasTime.getTime()
            val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
            if (second < 60) {
                convTime = "$second Seconds $suffix"
            } else if (minute < 60) {
                convTime = "$minute Minutes $suffix"
            } else if (hour < 24) {
                convTime = "$hour Hours $suffix"
            } else if (day >= 7) {
                convTime = if (day > 360) {
                    (day / 360).toString() + " Years " + suffix
                } else if (day > 30) {
                    (day / 30).toString() + " Months " + suffix
                } else {
                    (day / 7).toString() + " Week " + suffix
                }
            } else if (day < 7) {
                convTime = "$day Days $suffix"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            e.message?.let { Log.e("ConvTimeE", it) }
        }
        return convTime
    }

}
