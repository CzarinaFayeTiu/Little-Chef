package com.mobdeve.s12.avila.tiu.littlechef


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

open class DrawerBaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    var initRecipes: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_base)

    }

    override fun setContentView(view: View?) {
        val drawerLayout = layoutInflater.inflate(R.layout.activity_drawer_base, null) as DrawerLayout
        val container = drawerLayout.findViewById<FrameLayout>(R.id.activityContainer)
        container.addView(view)
        super.setContentView(drawerLayout)

        val toolbar: Toolbar = drawerLayout.findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        //toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        val navigationView: NavigationView = drawerLayout.findViewById(R.id.nav_view)
        navigationView.setItemIconTintList(null)
        //navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.menu_drawer_open, R.string.menu_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
                R.id.nav_notes -> {
                    startActivity(Intent(applicationContext, NoteActivity::class.java))
                }
                R.id.nav_bookmarks -> {
                    startActivity(Intent(applicationContext, BookmarksActivity::class.java))
                }
                R.id.nav_converter-> {
                    startActivity(Intent(applicationContext, ConverterActivity::class.java))
                }
                R.id.nav_timer -> {
                    startActivity(Intent(applicationContext, TimerActivity::class.java))
                }
                R.id.nav_music -> {
                    startActivity(Intent(applicationContext, MusicActivity::class.java))
                }
                R.id.nav_share -> {
                    startActivity(Intent(applicationContext, FacebookActivity::class.java))
                }
            }

            true
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }



}