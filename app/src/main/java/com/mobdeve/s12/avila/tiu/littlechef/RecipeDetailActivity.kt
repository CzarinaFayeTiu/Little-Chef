package com.mobdeve.s12.avila.tiu.littlechef

import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.Constants
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityRecipeDetailBinding
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel
import java.util.*

class RecipeDetailActivity : AppCompatActivity() {

    //actionbar
    private var actionBar:ActionBar? = null
    //db helper
    private var dbHelper:MyDbHelper? = null
    private var recipeId:String? = null

    var binding: ActivityRecipeDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //setting up actionbar
        actionBar = supportActionBar
        actionBar!!.title = "Recipe Details"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        //init db helper
        dbHelper = MyDbHelper(this)

        //get record id from intent
        val intent = intent
        recipeId = intent.getStringExtra("RECORD_ID")

        showRecordDetails()

    }

    private fun showRecordDetails() {
        //get record details

        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_ID} =\"$recipeId\""

        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor!!.getString(0)
                val name = cursor!!.getString(1)
                val category = cursor!!.getString(2)
                val ingredients = cursor!!.getString(3)
                val instructions = cursor!!.getString(4)
                val image = cursor!!.getString(5)
                val link = cursor!!.getString(6)
                val addedTime = cursor!!.getString(7)
                val updatedTime = cursor!!.getString(8)

                //convert timestamp
                /* for API 24 only (we're at API 23)
                val calendar1 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = addedTime.toLong()
                val timeAdded = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar1)

                val calendar2 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = updatedTime.toLong()
                val timeUpdated = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar1)
                */

                //set data
                binding!!.tvRecipeName.text = name
                binding!!.tvRecipeCategory.text = category
                binding!!.tvRecipeIngredients.text = ingredients
                binding!!.tvRecipeInstructions.text = instructions
                if (image == "null") {
                    //no image in record, set default
                    binding!!.ivRecipeImage.setImageResource(R.drawable.littlechef_logo)
                } else {
                    //have image in record
                    binding!!.ivRecipeImage.setImageURI(Uri.parse(image))
                }
            } while (cursor.moveToNext())
        }
        //close db connection
        db.close()
    }

}