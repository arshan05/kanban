package com.example.kanban

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kanban.authentication.Login

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (!isLoggedIn) {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
            finish()
        }else{
            val i = Intent(this@MainActivity, Drawer::class.java)
            startActivity(i)
            finish()
        }
    }
}