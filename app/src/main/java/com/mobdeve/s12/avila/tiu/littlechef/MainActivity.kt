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
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
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
    private fun searchCategories(query:String) {
        if (query != "All") {
            val adapterRecipe = RecipeAdapter(this, dbHelper.searchCategories(query))
            binding!!.rvRecipes.adapter = adapterRecipe
        } else
            loadRecipes()

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

        val filter = menu!!.findItem(R.id.action_filter)
        val spinner = filter.actionView as Spinner
        var search = ""

        ArrayAdapter.createFromResource(
            this,
            R.array.filter, //array is stored in strings.xml
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                search = parent?.getItemAtPosition(position).toString()
                searchCategories(search)
                Log.d("MAINACTIVITY", "FILTER STRING: $search")
            } })



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
            "null",
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
            "null",
            "1",
            "1"
        )

        //Recipe 6 - Sweet and Sour Tilapia
        dbHelper.insertRecord(
            "Sweet and Sour Tilapia",
            "Seafood",
            "  2 pieces tilapia, cleaned\n" +
                    "  2 tablespoons Knorr Liquid Seasoning\n" +
                    "  1 piece red bell pepper, julienne\n" +
                    "  1 piece green bell pepper, julienne\n" +
                    "  1 piece carrot, julienne\n" +
                    "  5 cloves garlic, crushed\n" +
                    "  1 piece onion, sliced\n" +
                    "  2 knobs ginger, julienne\n" +
                    "  2 tablespoons cornstarch\n" +
                    "  2 cups water\n" +
                    "  5 1/2 tablespoons white vinegar\n" +
                    "  8 tablespoons tomato ketchup\n" +
                    "  7 tablespoons white sugar\n" +
                    "  1 1/4 cup cooking oil\n" +
                    "  Salt and ground black pepper to taste",
            "1.    Apply Knorr Liquid Seasoning all over the tilapia. Let it stand for 10 minutes so that the fish can absorb the flavor of the seasoning.\n" +
                    " \n" +
                    "2.     Heat around 1 cup of cooking oil in a pan. Once the oil gets hot, fry the fish until the color turns golden brown. Flip to fry the opposite side. Remove from the pan. Place on a clean plate. Set aside.\n" +
                    " \n" +
                    "3.    Make the sauce by heating 3 tablespoons of cooking oil in a cooking pot. Saute onion and garlic for 10 seconds. Add ginger. Saute for 12 seconds.\n" +
                    " \n" +
                    "4.    Pour white vinegar and water. Let boil.\n" +
                    " \n" +
                    "5.    Add tomato ketchup. Stir until it completely dilutes in the mixture.\n" +
                    " \n" +
                    "6.    Add sugar. Stir until the sugar gets fully incorporated.\n" +
                    " \n" +
                    "7.    Put carrots and bell peppers. Cook for 3 minutes.\n" +
                    " \n" +
                    "8.    Combine cornstarch and 3 tablespoons water. Mix well. Pour into the cooking pot. Stir until sauce thickens.\n" +
                    " \n" +
                    "9.    Season with salt and ground black pepper. Add a piece of tilapia and cook for 3 minutes.\n" +
                    " \n" +
                    "10.    Transfer to a serving plate. Serve with rice. Share and enjoy!",
            "recipe6",
            "N90og8QI4IQ",
            "null",
            "1",
            "1"
        )

        //Recipe 7 - Chinese Cabbage Soup
        dbHelper.insertRecord(
            "Chinese Cabbage Soup",
            "Soup",
            "  200 g ground pork\n" +
                    "  500 g Chinese cabbage\n" +
                    "  1  handful green onions and coriander together, chopped\n" +
                    "  1  teaspoon vegetable stock powder\n" +
                    "  1/2  teaspoon salt\n" +
                    "  2  tablespoons minced garlic, black pepper, coriander roots together\n" +
                    "  2  tablespoons cooking oil\n" +
                    "  1  teaspoon soy sauce\n",
            "1.    Heat the cooking oil in a pan on high heat.\n" +
                    " \n" +
                    "2.     Add the minced garlic, black pepper and coriander roots. Sauté for 1 minute.\n" +
                    " \n" +
                    "3.    Add the ground pork. Sauté the ingredients together.\n" +
                    " \n" +
                    "4.    Season the ground pork with soy sauce and sauté until the ground pork is not pink.\n" +
                    " \n" +
                    "5.    Put the pot of water on the stove.\n" +
                    " \n" +
                    "6.    Add the cooked ground pork into the pot.\n" +
                    " \n" +
                    "7.    Add vegetable seasoning powder and salt.\n" +
                    " \n" +
                    "8.    Wait until the water boils, then add the Chinese cabbage. Allow the soup to boil for 7 minutes.\n" +
                    " \n" +
                    "9.    After 7 minutes, add the chopped green onions and chopped coriander.\n" +
                    " \n" +
                    "10.    Using a spoon to stir the ingredients together. ENJOY!",
            "recipe7",
            "pmW3SWpP2ig",
            "null",
            "1",
            "1"
        )

        //Recipe 8 - Classic Lasagna
        dbHelper.insertRecord(
            "Classic Lasagna",
            "Pasta",
            "  3/4 lb. lasagna noodles\n" +
                    "  1 tsp. extra-virgin olive oil, plus more for drizzling\n" +
                    "  2 lb. ground beef\n" +
                    "  4 cloves garlic, minced\n" +
                    "  2 tsp. dried oregano\n" +
                    "  Kosher salt\n" +
                    "  Freshly ground black pepper\n" +
                    "  2 (32-0z.) jars marinara\n" +
                    "  16 oz. whole milk ricotta\n" +
                    "  1/2 c. freshly grated Parmesan, divided\n" +
                    "  1/4 c. chopped parsley, plus more for garnish\n" +
                    "  1 large egg\n" +
                    "  2 lb. sliced mozzarella",
            "1.    Preheat oven to 375º. In a large pot of salted boiling water, cook pasta according to package directions until al dente, less 2 minutes. Drain and drizzle a bit of olive oil to prevent noodles from sticking together.\n" +
                    " \n" +
                    "2.     In a large pot over medium-high heat, heat oil. Cook ground beef until no longer pink, breaking up with a wooden spoon. Remove from heat and drain fat. Return beef to skillet and add garlic and oregano and cook, stirring, for 1 minute. Season with salt and pepper, then add marinara and stir until warmed through.\n" +
                    " \n" +
                    "3.    Combine ricotta, 1/4 cup Parmesan, parsley, and egg in a large mixing bowl and season with salt and pepper. Set aside.\n" +
                    " \n" +
                    "4.    In a large casserole dish, evenly spread a quarter of the meat sauce across the bottom of the dish, then top with a single layer of lasagna noodles, a layer of ricotta mixture, a single layer of mozzarella, and a layer of meat sauce. Repeat layers, topping the last layer of noodles with meat sauce, Parmesan, and mozzarella.\n" +
                    " \n" +
                    "5.    Cover with foil and bake for 15 minutes, then increase temperature to 400º and bake uncovered for 18 to 20 minutes.\n" +
                    " \n" +
                    "6.    Cover with foil and bake for 15 minutes, then increase temperature to 400º and bake uncovered for 18 to 20 minutes.\n",
            "recipe8",
            "uCH4axRLeXM",
            "null",
            "1",
            "1"
        )

        //Recipe 9 - Spaghetti alla Carbonara
        dbHelper.insertRecord(
            "Spaghetti alla Carbonara",
            "Pasta",
            "  220g Spaghetti or Spaghettoni (the largest spaghetti)\n" +
                    "  25g Guanciale (the cheek of the pork) or pancetta (Italian bacon) cut into small cubes.\n" +
                    "  2 tbsp olive oil\n" +
                    "  2 eggs\n" +
                    "  50g Parmesan cheese (or aged pecorino) freshly grated.\n" +
                    "  Freshly ground black pepper.\n",
                    "1.     Cook the pasta in a large pan of boiling salted water until al dente.\n" +
                    " \n" +
                    "2.    Meanwhile, heat the oil in a pan and fry the guanciale or pancetta until crisp.\n" +
                    " \n" +
                    "3.    Lightly beat the eggs in a large bowl with the grated cheese and pepper.\n" +
                    " \n" +
                    "4.    When the pasta is ready, drain and add to the pan with the guanciale. Then mix well to coat everything. \n" +
                    " \n" +
                    "5.    Take off the heat. Allow to cool slightly. \n" +
                    " \n" +
                    "6.    Then add the egg and cheese mixture. Stir to coat the pasta and serve immediately.\n",
            "recipe9",
            "3AAdKl1UYZs",
            "null",
            "1",
            "1"
        )

        //Recipe 10 - Spaghetti alla Carbonara
        dbHelper.insertRecord(
            "Salted Egg Chicken Wings",
            "Chicken",
            "  513 grams chicken wing part\n" +
                    "  4 tablespoon cornstarch\n" +
                    "  3 tablespoon fish sauce\n" +
                    "  100 grams margarine/butter\n" +
                    "  3 cloves garlic\n" +
                    "  4 pieces salted egg yolk\n" +
                    "  3 pieces red chili\n" +
                    "  1 cup salted egg powder\n" +
                    "  cooking oil\n" +
                    "  black ground pepper\n",
            "1.     Marinate the chicken wings with 3 tablespoon of fish sauce, 4 tablespoon of cornstarch and a dash of black ground pepper. Mix to evenly coat the chicken wings with the mixture. Let it sit for 15 minutes.\n" +
                    " \n" +
                    "2.    Dredge the marinated chicken wings in flour before frying in medium heat. Cook for 8 minutes or until the chicken wings turn golden brown in color.\n" +
                    " \n" +
                    "3.    Drain the excess oil from the fried chicken wings using a paper towel. Set aside. In a cooking pan melt 100 grams of butter. Saute 3 cloves of minced garlic then add 4 pieces of salted egg yolk.\n" +
                    " \n" +
                    "4.    Pour in the fried chicken wings, mix until all the chicken wings are evenly coated. Set aside for later.  \n" +
                    " \n" +
                    "5.    In a low heat, cook 3 pieces of slice red chili. Add 1 cup of salted egg powder. Continue mixing for 2 minutes then turn off the stove. Coat the chicken wings in the salted egg powder mixture. Transfer in a serving plate. Enjoy!\n"
                    ,
            "recipe10",
            "TM5GotXFtas",
            "null",
            "1",
            "1"
        )

        var prefs: SharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("firstTime", false)
        editor.apply()
    }


}