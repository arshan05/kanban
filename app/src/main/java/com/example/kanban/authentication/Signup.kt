package com.example.kanban.authentication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.kanban.R
import com.example.kanban.database
import com.example.kanban.databinding.ActivitySignupBinding
import com.example.kanban.tables.Users
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = DataBindingUtil.setContentView(this@Signup, R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        signupBinding.verifyEmail.setOnClickListener() {
            if (auth.currentUser!!.isEmailVerified()) {
                signupBinding.signupBtn.isEnabled = true
            } else {
                Toast.makeText(this@Signup, "Your Email is not Verified", Toast.LENGTH_SHORT).show()
            }
        }

        signupBinding.signupBtn.setOnClickListener {
            writeNewUser(
                signupBinding.nameSignupInput.editText?.text.toString(),
                signupBinding.usernameSignupInput.editText?.text.toString(),
                signupBinding.emailSignupInput.editText?.text.toString(),
                signupBinding.paswordSignupInput.editText?.text.toString()
            )
        }
    }

    fun writeNewUser(name: String, username: String, email: String, password: String) {
        val user = Users(name = name, username = username, email = email)
        database.getReference().child("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
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

                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this@Signup) {
                                if (it.isSuccessful) {

                                    database.getReference().child("Users")
                                        .child(username).setValue(user)
                                    signupBinding.usernameSignupInput.isErrorEnabled =
                                        false
                                    val intent = Intent(this@Signup, Login::class.java)
                                    startActivity(intent)

                                } else
                                    Toast.makeText(
                                        this@Signup,
                                        it.exception!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}

