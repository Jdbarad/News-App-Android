package com.builditcreative.newsapp.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.builditcreative.newsapp.R
import com.builditcreative.newsapp.models.ArticleResponse
import com.builditcreative.newsapp.api.EverythingRequest
import com.builditcreative.newsapp.api.NewsApiClient
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    lateinit var newsApiClient: NewsApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        newsApiClient = NewsApiClient("a50cd99ec4f64caeacaab74924149e38")

        doMySearch("Android")

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(true)
        }
        val menuItem: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                doMySearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun doMySearch(query: String?) {
        var listNews:RecyclerView = findViewById(R.id.news_list)
        listNews.setHasFixedSize(true)
        listNews.setItemViewCacheSize(100)
        listNews.layoutManager = LinearLayoutManager(applicationContext)
        newsApiClient.getEverything(
            EverythingRequest.Builder()
                .q(query)
                .language("en")
                .build(),
            object : NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse?) {
                    if (response != null) {
                        listNews.adapter = NewsAdapter(applicationContext, response,listNews)
                    }
                }

                override fun onFailure(throwable: Throwable?) {
                    if (throwable != null) {

                    }
                }
            }
        )
    }
}