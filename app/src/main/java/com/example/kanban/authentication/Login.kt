package com.example.kanban.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.kanban.MainActivity
import com.example.kanban.R
import com.example.kanban.databinding.ActivityLoginBinding
import com.example.kanban.tables.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference

        loginBinding = DataBindingUtil.setContentView(this@Login,R.layout.activity_login)

        loginBinding.loginBtn.setOnClickListener{
            loginBinding.usernameLoginInput.isErrorEnabled = false
            loginBinding.passwordLoginInput.isErrorEnabled = false
            database.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var usernameExists = false

                    snapshot.children.forEach { child ->
                    val users = child.getValue(Users::class.java)
                        Log.d("data",child.key.toString())
                        Log.d("name",users?.name.toString())
                        if (child != null && child.key.toString() == loginBinding.usernameLoginInput.editText?.text.toString()) {
                            usernameExists = true
                            if (users?.password == loginBinding.passwordLoginInput.editText?.text.toString()){
                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    if (usernameExists) {
                        loginBinding.passwordLoginInput.error = "incorrect password"
                    } else {
                        loginBinding.passwordLoginInput.error = "incorrect password"
                        loginBinding.usernameLoginInput.error = "incorrect username"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

        }

        loginBinding.redirectToSignup.setOnClickListener{
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
    }
}