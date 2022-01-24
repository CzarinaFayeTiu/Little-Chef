package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.provider.MediaStore
import android.util.Log
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
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL

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

        //First start Only
        var prefs: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        var firstTime: Boolean = prefs.getBoolean("firstTime", true)

        if (firstTime) {
            loadInitRecipes()
            loadRecipes()
        }
    }


    private fun loadRecipes() {
        val adapterRecipe = RecipeAdapter(this, dbHelper.getAllRecipes(NEWEST_FIRST))

        binding!!.rvRecipes.adapter = adapterRecipe
    }

    private fun searchRecipes(query:String) {
        val adapterRecipe = RecipeAdapter(this, dbHelper.searchRecords(query))

        binding!!.rvRecipes.adapter = adapterRecipe
    }

    public override fun onResume() {
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

    private fun loadInitRecipes() {

        //Recipe 1 - Chocolate Cake
        dbHelper.insertRecord(
            "Chocolate Cake",
            "Dessert",
            "butter and flour for coating and dusting the     cake pan\n" +
                    "3  cups all-purpose flour\n" +
                    "3  cups granulated sugar\n" +
                    "1  1/2 cups unsweetened cocoa powder\n" +
                    "1  tablespoon baking soda\n" +
                    "1  1/2 teaspoons baking powder\n" +
                    "1  1/2 teaspoons salt\n" +
                    "4  large eggs\n" +
                    "1  1/2 cups buttermilk\n" +
                    "1  1/2 cups warm water\n" +
                    "1/2  cup vegetable oil\n" +
                    "2  teaspoons vanilla extract",
            "1.    Preheat oven to 350 degrees Fahrenheit.\tButter three 9-inch cake rounds. Dust with flour and tap out the excess.\n\n" +
                    "2.    Mix together flour, sugar, cocoa, baking\tsoda, baking powder, and salt in a stand mixer using a low speed until combined.\n\n" +
                    "3.    Add eggs, buttermilk, warm water, oil, and\tvanilla. Beat on a medium speed until smooth. This should take just a couple of minutes.\n\n" +
                    "4.    Divide batter among the three pans. I\tfound that it took just over 3 cups of the batter to divide it evenly.\n\n" +
                    "5.    Bake for 30-35 minutes in a 350 degree\toven until a toothpick inserted into the center comes out clean.\n\n" +
                    "6.    Cool on wire racks for 15 minutes and\tthen turn out the cakes onto the racks and allow to cool completely.\n\n" +
                    "7.    Frost with your favorite frosting and\tenjoy!",
            "recipe1",
            "dsJtgmAhFF4",
            "null",
            "1",
            "1"
        )

        //Recipe 2 - Chicken Adobo
        dbHelper.insertRecord(
            "Chicken Adobo",
            "Chicken",
            "2  lbs chicken cut into serving pieces\n" +
                    "3  pieces dried bay leaves\n" +
                    "8  tablespoons soy sauce\n" +
                    "4  tablespoons white vinegar\n" +
                    "5  cloves garlic crushed\n" +
                    "1  1/2 cups water\n" +
                    "3  tablespoons cooking oil\n" +
                    "1  teaspoon sugar\n" +
                    "1/4  teaspoon salt optional\n" +
                    "1  teaspoon whole peppercorn" +
                    "2  teaspoons vanilla extract",
            "1.    Combine chicken, soy sauce, and garlic in a large bowl. Mix well. Marinate the chicken for at least 1 hour. Note: the longer the time, the better\n" +
                    "2.    Heat a cooking pot. Pour cooking oil.\n" +
                    "3.    When the oil is hot enough, pan-fry the marinated chicken for 2 minutes per side.\n" +
                    "4.    Pour-in the remaining marinade, including garlic. Add water. Bring to a boil\n" +
                    "5.    Add dried bay leaves and whole peppercorn. Simmer for 30 minutes or until the chicken gets tender\n" +
                    "6.    Add vinegar. Stir and cook for 10 minutes.\n" +
                    "7.    Put-in the sugar, and salt. Stir and turn the heat off. Serve hot. Share and Enjoy!",
            "recipe2",
            "mtyULaM6RfQ",
            "null",
            "1",
            "1"
        )

        //Recipe 3 - Mango Float
        dbHelper.insertRecord(
            "Mango Float",
            "Dessert",
            "3  ripe medium size mangoes, slice into strips \n" +
                    "2  (200g)pack Graham crackers\n" +
                    "2  (250ml) all-purpose cream, chilled overnight\n" +
                    "1  (380g) big can condensed milk",
            "1.    In a bowl beat using hand mixer 2 pack(250ml) all-purpose cream(chilled overnight) in high speed. When it double its size add condensed milk and beat until well combined.  Set aside or let it chill in the ref until ready to use.\n" +
                    "2.    Arrange graham crackers in a deep container.  Fill gaps with bits and pieces of graham crackers.\n" +
                    "Put cream on top of graham and spread evenly.  Add strips of mangoes on top of the cream.\n" +
                    "3.    Add another layer of graham crackers, spread cream on top of graham and strips of mangoes on top of cream.  Repeat process until you reach desired number of layers.  I made 4 layers of graham crackers, cream and mangoes on this recipe.\n" +
                    "4.    Cover and chill in the refrigerator overnight to let the graham soaked into the cream.  The result is a cake-like texture and cream is so smooth and creamy.",
            "recipe3",
            "JS6atiDtmlI",
            "null",
            "1",
            "1"
        )

        //Recipe 4 - Sinigang
        dbHelper.insertRecord(
            "Sinigang",
            "Pork",
            "2  lbs pork belly or buto-buto\n" +
                    "1  bunch spinach or kang-kong\n" +
                    "3  tablespoons fish sauce\n" +
                    "12  pieces string beans sitaw, cut in 2 inch length\n" +
                    "2  pieces tomato quartered\n" +
                    "3  pieces chili or banana pepper\n" +
                    "1  tablespoons cooking oil\n" +
                    "2  quarts water\n" +
                    "1  piece onion sliced\n" +
                    "2  pieces taro gabi, quartered\n" +
                    "1  pack sinigang mix good for 2 liters water",
            "1.    Heat the pot and put-in the cooking oil\n" +
                    "2.    Sauté the onion until its layers separate from each other\n" +
                    "3.    Add the pork belly and cook until outer part turns light brown\n" +
                    "4.    Put-in the fish sauce and mix with the ingredients\n" +
                    "5.    Pour the water and bring to a boil\n" +
                    "6.    Add the taro and tomatoes then simmer for 40 minutes or until pork is tender\n" +
                    "7.    Put-in the sinigang mix and chili\n" +
                    "8.    Add the string beans (and other vegetables if there are any) and simmer for 5 to 8 minutes\n" +
                    "9.    Put-in the spinach, turn off the heat, and cover the pot. Let the spinach cook using the remaining heat in the pot.\n" +
                    "10.    Serve hot. Share and enjoy!",
            "recipe4",
            "t-beBtUZz3E",
            "bookmarked",
            "1",
            "1"
        )

        //Recipe 5 - Blueberry Cheesecake
        dbHelper.insertRecord(
            "Blueberry Cheesecake",
            "Dessert",
            "Blueberry Sauce\n" +
                    "  1  cup crushed graham crackers\n" +
                    "  1/4  cup butter, melted\n" +
                    "  2  tablespoons white sugar\n" +
                    "  250  grams cream cheese, at room temperature\n" +
                    "  1/2  cup sugar\n" +
                    "  2  eggs\n" +
                    "  1/2  cup heavy cream\n" +
                    "  juice from half a lemon\n" +
                    "  1  tablespoon all-purpose flour\n" +
                    "  ready-made blueberry filling (for the topping)",
            "1.    Make the crust: Combine crushed graham, sugar, and butter in a bowl. Transfer to a greased and lined 6-inch round springform pan. Pat down evenly to form the crust then chill in the refrigerator.\n" +
                    " \n" +
                    "2.    Make the filling: Beat cream cheese in a bowl and add sugar. Add eggs one at a time then add heavy cream, lemon juice, and flour. Mix until well-combined.\n" +
                    " \n" +
                    "3.    Pour the filling onto the chilled crust and bake in a waterbath at 400°F for 10 minutes. Lower heat at 325°F for another 30-40 minutes. Turn off the heat and leave the cake inside the oven for 1 hour. Take the cheesecake out of the oven and allow to completely cool.\n" +
                    " \n" +
                    "4.    Refrigerate the cheesecake for at least 4 hours before serving.\n" +
                    " \n" +
                    "5.    When ready to serve, top the cheesecake with blueberry filling.",
            "recipe5",
            "fBMB_D_jSls",
            "bookmarked",
            "1",
            "1"
        )

        var prefs: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("firstTime", false)
        editor.apply()
    }


}