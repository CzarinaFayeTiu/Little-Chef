package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.mobdeve.s12.avila.tiu.littlechef.Adapter.RecipeAdapter
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.Constants
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel

class MainActivity : DrawerBaseActivity() {

    //db helper
    lateinit var dbHelper: MyDbHelper
    //order by sor queries
    private val NEWEST_FIRST = "${Constants.C_ADDED_TIMESTAMP} DESC"

    //lateinit var toggle: ActionBarDrawerToggle
    var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //init db helper
        dbHelper = MyDbHelper(this)

        loadRecipes()



/**
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
                R.id.nav_notes -> {
                    startActivity(Intent(applicationContext, NoteActivity::class.java))
                }
                R.id.nav_bookmarks -> Toast.makeText(applicationContext, "Clicked Bookmark", Toast.LENGTH_SHORT).show()
                R.id.nav_converter -> Toast.makeText(applicationContext, "Clicked Converter", Toast.LENGTH_SHORT).show()
                R.id.nav_timer -> {
                    startActivity(Intent(applicationContext, TimerActivity::class.java))
                }
                R.id.nav_music -> {
                    startActivity(Intent(applicationContext, MusicActivity::class.java))
                }
                R.id.nav_share -> Toast.makeText(applicationContext, "Clicked Share", Toast.LENGTH_SHORT).show()
            }

            true
        }**/
    }

    private fun loadRecipes() {
        val adapterRecipe = RecipeAdapter(this, dbHelper.getAllRecipes(NEWEST_FIRST))

        binding!!.rvRecipes.adapter = adapterRecipe
    }

    private fun searchRecipes(query:String) {
        val adapterRecipe = RecipeAdapter(this, dbHelper.searchRecords(query))

        binding!!.rvRecipes.adapter = adapterRecipe
    }

    override fun onResume() {
        super.onResume()
        loadRecipes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflate menu
        menuInflater.inflate(R.menu.menu_main, menu)

        //search view
        val item = menu!!.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                //search as you type
                if (newText != null) {
                    searchRecipes(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                //search when search button on keyboard is clicked
                if (query != null) {
                    searchRecipes(query)
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

/**
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }**/


}