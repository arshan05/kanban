package com.example.kanban

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
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

        drawerLayout = findViewById(R.id.drawer_layout)

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.home,R.id.invitations), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.navigationView).setupWithNavController(navController)

//        drawerLayout = findViewById(R.id.drawer_layout)
//        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
//        drawerLayout.addDrawerListener(actionBarToggle)
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBarToggle.syncState()
//
//        navView = findViewById(R.id.navigationView)

//        setSupportActionBar(findViewById<MaterialToolbar>(R.id.topAppBar))
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
//        navController = findNavController(R.id.nav_host_fragment_container)
//        val navigationView = findViewById<NavigationView>(R.id.bottomNavigationView)
//
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.home, R.id.invitations), drawerLayout)
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navigationView.setupWithNavController(navController)

//        val navigationView = findViewById<NavigationView>(R.id.bottomNavigationView)
//
//        val navControllerFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
//        navController = navControllerFragment.navController
//        NavigationUI.setupWithNavController(navigationView, navController)
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return findNavController(R.id.nav_host_fragment).navigateUp(drawerLayout)
//    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}