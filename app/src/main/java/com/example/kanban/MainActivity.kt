package com.example.kanban

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kanban.authentication.Login
import com.example.kanban.authentication.Signup
import com.example.kanban.homescreen.Homescreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val i = Intent(this@MainActivity, Login::class.java)
        startActivity(i)
    }
}