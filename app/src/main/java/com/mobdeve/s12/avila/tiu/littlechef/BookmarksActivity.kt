package com.mobdeve.s12.avila.tiu.littlechef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.mobdeve.s12.avila.tiu.littlechef.Adapter.RecipeAdapter
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.Constants
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityBookmarksBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding

class BookmarksActivity :DrawerBaseActivity()  {

    //db helper
    lateinit var dbHelper: MyDbHelper
    //order by sor queries
    private val NEWEST_FIRST = "${Constants.C_ADDED_TIMESTAMP} DESC"


    //lateinit var toggle: ActionBarDrawerToggle
    var binding: ActivityBookmarksBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //init db helper
        dbHelper = MyDbHelper(this)

        loadRecipes()


    }

    private fun loadRecipes() {
        val adapterRecipe = RecipeAdapter(this, dbHelper.searchBookmarks())

        binding!!.rvRecipes.adapter = adapterRecipe
    }

    public override fun onResume() {
        super.onResume()
        loadRecipes()
    }


}