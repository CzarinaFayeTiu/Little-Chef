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
            "https://thestayathomechef.com/the-most-amazing-chocolate-cake/",
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
            "https://panlasangpinoy.com/filipino-chicken-adobo-recipe/",
            "null",
            "1",
            "1"
        )

        //Recipe 3 - Mango Float
        dbHelper.insertRecord(
            "Mango Float",
            "Dessert",
            "5  mangoes, ripe but not soft\n" +
                    "4  cups heavy cream(960 mL), or canned all-purpose cream, chilled overnight\n" +
                    "1  can condensed milk, chilled overnight\n" +
                    "2  teaspoons vanilla extract\n" +
                    "½  teaspoon kosher salt\n" +
                    "21  crackers graham cracker, plus more, crushed, for topping",
            "1.    Peel the mangoes and cut the lobes off as close to the pit as possible. Thinly slice half of the mango lobes into ¹⁄₁₆-inch (15 mm)-thick slices, keeping the slices intact and transferring to a tray or plate. Cover and refrigerate until ready to assemble. Cut the remaining mango into ½-inch (1.24 cm) cubes. Transfer to a bowl, cover, and refrigerate until ready to assemble.\n" +
                    "2.    Add 2 cups of cream to a chilled large bowl and use an electric hand mixer to beat on medium-high speed until stiff peaks form, 3–5 minutes. Add the remaining 2 cups of cream and beat again until stiff peaks form, 3–5 minutes more.\n" +
                    "3.    Add half of the condensed milk and beat until fully incorporated. Add the remaining condensed milk, vanilla, and salt and beat until fully incorporated and stiff peaks form, 4–7 minutes. Give the mixture a final stir with a rubber spatula.\n" +
                    "4.    Line a chilled 7 x 11 (17 x 27 cm) -inch glass dish with parchment paper, leaving overhang on all sides.\n" +
                    "5.    Cover the bottom of the dish with a single layer of graham crackers, breaking to fill in any gaps as necessary. Spread 2–3 cups (480-720 ml) of the whipped cream mixture evenly over the graham crackers, all the way to the edges and corners of the dish. Top with ½ of the diced mango. Repeat to make another layer. For the final layer, arrange the graham crackers over the diced mango and top with 2–3 cups of whipped cream. Arrange the thinly sliced mango decoratively on top. Sprinkle crushed graham crackers around the edge of the float.\n" +
                    "6.    Cover the mango float in plastic wrap and refrigerate for 4–6 hours, until the cream sets and the graham crackers soften and absorb some moisture. Transfer the float to the freezer for 8 hours, or overnight.\n" +
                    "7.    Remove the float from the freezer and use the parchment paper to lift out of the dish. Let thaw for 5–10 minutes before slicing.\n" +
                    "8.    Serve with your favorite dessert wine, cocktail, coffee, or tea.",
            "recipe3",
            "https://tasty.co/recipe/mango-float",
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
            "https://panlasangpinoy.com/pork-sinigang-na-baboy-recipe/",
            "bookmarked",
            "1",
            "1"
        )

        //Recipe 5 - Blueberry Cheesecake
        dbHelper.insertRecord(
            "Blueberry Cheesecake",
            "Dessert",
            "Blueberry Sauce\n" +
                    "  3  cups fresh or frozen blueberries\n" +
                    "  1/4  cup granulated sugar (50g)\n" +
                    "  1/4  cup water\n" +
                    "  1  tablespoon corn starch\n" +
                    "Crust\n" +
                    "  2  1/2 cups graham cracker crumbs\n" +
                    "  1/3  cup melted butter\n" +
                    "  1  tablespoon granulated sugar\n" +
                    "Filling\n" +
                    "  3  packages cream cheese (24 oz or 750g total), room temperature\n" +
                    "  2  cups sour cream or Greek yogurt\n" +
                    "  1  1/2 cups granulated sugar (300g)\n" +
                    "  4  large eggs\n" +
                    "  1  tablespoon vanilla" +
                    "  1   pack sinigang mix good for 2 liters water",
            "Blueberry Sauce\n" +
                    "  1.    In a medium pan, combine blueberries and sugar. Whisk together water and corn starch and add to the pan.\n" +
                    "  2.    Cook over medium heat, stirring often, until bubbly and thickened, about 5-8 minutes.\n" +
                    "  3.    Set aside to cool to room temperature (the fridge makes this go much quicker)\n" +
                    "Crust\n" +
                    "  1.    Preheat oven to 325 degrees F and line a 9\" Springform pan with parchment paper (pinch it in between the bottom and the sides)\n" +
                    "  2.    Stir together graham cracker crumbs, butter and sugar and press into the pan and about 1/2\" up the sides.\n" +
                    "  3.    Bake for 10-12 minutes, just until dry. Remove to cool and reduce oven temperature to 275 degrees F.\n" +
                    "Filling\n" +
                    "  1.    In a large bowl, beat cream cheese with an electric mixer until smooth.\n" +
                    "  2.    Add sour cream, sugar, eggs and vanilla and beat until smooth.\n" +
                    "  3.    Pour half of the filling into another bowl. Stir in ½ cup blueberry sauce.\n" +
                    "  4.    Pour blueberry cheesecake filling into prepared crust.\n" +
                    "  5.    Carefully pour plain cheesecake filling over the blueberry, drizzling back and forth to cover in an even layer. Spread to smooth.\n" +
                    "  6.    Drop teaspoons of blueberry sauce over the top of the cheesecake and gently swirl. Reserve remaining sauce for serving.\n" +
                    "  7.    Bake at 275 degrees F for 1.5-2 hours, until the outer 2\" are set and the center is still somewhat jiggly.\n" +
                    "  8.    Turn oven off and let sit in warm oven for 1 more hour.\n" +
                    "  9.    Remove from oven and set on the counter to cool to room temperature. Refrigerate at least 8 hours or overnight before slicing.\n" +
                    "  10.    Remove from pan and serve with whipped cream (optional) and remaining blueberry sauce.",
            "recipe5",
            "https://www.thereciperebel.com/blueberry-cheesecake/",
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