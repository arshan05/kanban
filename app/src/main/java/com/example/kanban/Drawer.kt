package com.example.kanban

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.kanban.authentication.Login
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Drawer : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var appBarConfiguration:AppBarConfiguration
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
//        toolbar.navigationIcon = resources.getDrawable(R.drawable.menu)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView = findViewById<NavigationView>(R.id.navigationView)
        navView.setupWithNavController(navController)
        val logOut = navView.menu.findItem(R.id.logout)
        logOut.setOnMenuItemClickListener {
            it.isChecked = true
            drawerLayout.close()

            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("is_logged_in")
            editor.apply()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            true
        }
//        navView.setNavigationItemSelectedListener {
//            if (it.itemId == R.id.logout){
//                it.isChecked = true
//                drawerLayout.close()
//
//                val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                editor.remove("is_logged_in")
//                editor.apply()
//                val intent = Intent(this,Login::class.java)
//                startActivity(intent)
//            }
//            else {
//                navView.setupWithNavController(navController)
//            }
//            true
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}