package com.example.kanban.authentication

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.InputType
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.slidingpanelayout.widget.SlidingPaneLayout.LayoutParams
import com.example.kanban.Drawer
import com.example.kanban.R
import com.example.kanban.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    //    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        database = Firebase.database.reference

        loginBinding = DataBindingUtil.setContentView(this@Login, R.layout.activity_login)
        auth = FirebaseAuth.getInstance()


        loginBinding.forgotPassword.setOnClickListener{
            var editText = TextInputEditText(this)
            editText.setHint("enter email")
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(10,10,10,10)
            editText.layoutParams = layoutParams
            MaterialAlertDialogBuilder(this@Login)
                .setTitle("Reset Password")
                .setView(editText)
                .setPositiveButton("Reset"){ dialog, which ->
                    val editTextInput: String = editText.getText().toString()

                    Firebase.auth.sendPasswordResetEmail(editTextInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                }.show()
        }

//        var email = loginBinding.emailLoginInput.editText?.text.toString()
//        var password = loginBinding.passwordLoginInput.editText?.text.toString()

        loginBinding.loginBtn.setOnClickListener {
            loginBinding.emailLoginInput.isErrorEnabled = false
            loginBinding.passwordLoginInput.isErrorEnabled = false

            var email = loginBinding.emailLoginInput.editText?.text.toString()
            var password = loginBinding.passwordLoginInput.editText?.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){
                auth.currentUser!!.isEmailVerified()
                if (it.isSuccessful && auth.currentUser!!.isEmailVerified()){
                    val intent = Intent(this,Drawer::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "invalid user", Toast.LENGTH_SHORT).show()
                }
            }






//            database.child("Users").addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    var usernameExists = false
//                    var incorrectPassword = true
//                    snapshot.children.forEach { child ->
//                        val users = child.getValue(Users::class.java)
//                        if (child != null && child.key.toString() == loginBinding.usernameLoginInput.editText?.text.toString()) {
//                            usernameExists = true
//                            if (users?.password == loginBinding.passwordLoginInput.editText?.text.toString()) {
//                                currentUser = Users(users.name, users.username, users.password)
//                                incorrectPassword = false
//                                val intent = Intent(this@Login, Drawer::class.java)
//                                startActivity(intent)
//                            }
//                        }
//                    }
//                    if (usernameExists && incorrectPassword) {
//                        loginBinding.passwordLoginInput.error = "incorrect password"
//                    } else if (!usernameExists && incorrectPassword) {
//                        MaterialAlertDialogBuilder(this@Login)
//                            .setIcon(R.drawable.ic_baseline_warning_24)
//                            .setTitle("Invalid User")
//                            .setMessage("Enter valid username and password")
//                            .setPositiveButton("ok") { dialog, _ ->
//                                dialog.dismiss()
//                            }
//                            .show()
//
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//
//            })

        }

        loginBinding.redirectToSignup.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
    }
}