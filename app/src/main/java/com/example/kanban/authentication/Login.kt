package com.example.kanban.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kanban.Drawer
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityLoginBinding
import com.example.kanban.homescreen.Homescreen
import com.example.kanban.tables.Users
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
class Login : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding

    //    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        database = Firebase.database.reference

        loginBinding = DataBindingUtil.setContentView(this@Login, R.layout.activity_login)

        loginBinding.loginBtn.setOnClickListener {
            loginBinding.usernameLoginInput.isErrorEnabled = false
            loginBinding.passwordLoginInput.isErrorEnabled = false
            database.child("Users").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var usernameExists = false
                    var incorrectPassword = true
                    snapshot.children.forEach { child ->
                        val users = child.getValue(Users::class.java)
                        if (child != null && child.key.toString() == loginBinding.usernameLoginInput.editText?.text.toString()) {
                            usernameExists = true
                            if (users?.password == loginBinding.passwordLoginInput.editText?.text.toString()) {
                                currentUser = Users(users.name, users.username, users.password)
                                incorrectPassword = false
                                val intent = Intent(this@Login, Drawer::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    if (usernameExists && incorrectPassword) {
                        loginBinding.passwordLoginInput.error = "incorrect password"
                    } else if (!usernameExists && incorrectPassword) {
                        MaterialAlertDialogBuilder(this@Login)
                            .setIcon(R.drawable.ic_baseline_warning_24)
                            .setTitle("Invalid User")
                            .setMessage("Enter valid username and password")
                            .setPositiveButton("ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

        }

        loginBinding.redirectToSignup.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
    }
}