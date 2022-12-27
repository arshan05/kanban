package com.example.kanban.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.kanban.R
import com.example.kanban.database
import com.example.kanban.databinding.ActivitySignupBinding
import com.example.kanban.tables.Users
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

class Signup : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
//    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = DataBindingUtil.setContentView(this@Signup, R.layout.activity_signup)

//        database = Firebase.database.reference

        database.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach { child ->
//                    Log.d("child", child.key.toString())
////
//                    if (child != null && child.key.toString() == signupBinding.usernameSignupInput.editText.toString()) {
//                        // Username is already taken
//                        signupBinding.usernameSignupInput.error = "username already taken"
//                    }
//                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        signupBinding.signupBtn.setOnClickListener {
            writeNewUser(
                signupBinding.nameSignupInput.editText?.text.toString(),
                signupBinding.usernameSignupInput.editText?.text.toString(),
                signupBinding.paswordSignupInput.editText?.text.toString()
            )
        }
    }

    fun writeNewUser(name: String, username: String, password: String) {
        val user = Users(name=name,username, password = password)
        database.child("Users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var usernameExists = false
                snapshot.children.forEach { child ->
//                    val users = child.getValue(Users::class.java)
                    if (child != null && child.key == username) {
                        usernameExists = true
                    }
                }
                if (usernameExists) {
                    // Username is already taken
                    signupBinding.usernameSignupInput.error = "username already taken"
                } else {
                    database.child("Users").child(username).setValue(user)
                    signupBinding.usernameSignupInput.isErrorEnabled = false
                    Toast.makeText(this@Signup, "signup successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Signup, Login::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

//        database.child("Users").child(username).setValue(user)
//        Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()

        })
    }
}

