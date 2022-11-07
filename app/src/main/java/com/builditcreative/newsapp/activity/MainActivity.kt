package com.builditcreative.newsapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.builditcreative.newsapp.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler:Handler = Handler()
        handler.postDelayed(Runnable {
            if (firebaseAuth.uid==null) {
                //goto signin
                val intent = Intent(this, SigninActivity::class.java)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, HomeActivity::class.java)
                this.startActivity(intent)
            }
        },3000)

    }
}