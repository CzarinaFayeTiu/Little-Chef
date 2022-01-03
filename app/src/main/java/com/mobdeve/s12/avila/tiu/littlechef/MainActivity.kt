package com.mobdeve.s12.avila.tiu.littlechef

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.NavHeaderBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        val drawerLayout:DrawerLayout = binding!!.drawerLayout
        val navView:NavigationView = binding!!.navView


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open,  R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //onclick listener for nav view

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.nav_notes -> Toast.makeText(applicationContext, "Clicked My Notes", Toast.LENGTH_SHORT).show()
                R.id.nav_bookmarks -> Toast.makeText(applicationContext, "Clicked Bookmark", Toast.LENGTH_SHORT).show()
                R.id.nav_converter -> Toast.makeText(applicationContext, "Clicked Converter", Toast.LENGTH_SHORT).show()
                R.id.nav_timer -> Toast.makeText(applicationContext, "Clicked Timer", Toast.LENGTH_SHORT).show()
                R.id.nav_music -> Toast.makeText(applicationContext, "Clicked Music", Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(applicationContext, "Clicked Share", Toast.LENGTH_SHORT).show()
            }

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}