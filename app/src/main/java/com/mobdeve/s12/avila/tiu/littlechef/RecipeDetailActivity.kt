package com.mobdeve.s12.avila.tiu.littlechef

import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.Constants
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityRecipeDetailBinding
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.util.*

class RecipeDetailActivity : DrawerBaseActivity() {

    //actionbar
    //private var actionBar:ActionBar? = null
    //db helper
    private var dbHelper:MyDbHelper? = null
    private var recipeId:String? = null
    lateinit var tts:TextToSpeech
    var binding: ActivityRecipeDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        /*

        //setting up actionbar
        actionBar = supportActionBar
        actionBar!!.title = "Recipe Details"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)*/

        //init db helper
        dbHelper = MyDbHelper(this)

        //get record id from intent
        val intent = intent
        recipeId = intent.getStringExtra("RECORD_ID")

        showRecordDetails()

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                status ->
            if(status != TextToSpeech.ERROR){
                //set language
                tts.language = Locale.UK
            }
        })

        var speakBtn = binding!!.speakBtn

        speakBtn.setOnClickListener {
            //get edit text
            val toSpeak = binding!!.tvRecipeInstructions.text.toString()
            if(toSpeak == ""){
                //if no text is written
                Toast.makeText(this, "Enter Text", Toast.LENGTH_SHORT).show()
            }else{
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
        stopBtn.setOnClickListener {
            if(tts.isSpeaking){
                tts.stop()
            }else{
                Toast.makeText(this, "Not Speaking", Toast.LENGTH_SHORT).show()
            }
        }


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

                //set data
                binding!!.tvRecipeName.text = name
                binding!!.tvRecipeCategory.text = category
                binding!!.tvRecipeIngredients.text = ingredients
                binding!!.tvRecipeInstructions.text = instructions
                if (image == "null") {
                    //no image in record, set default
                    binding!!.ivRecipeImage.setImageResource(R.drawable.littlechef_logo)
                } else if (image == "recipe1") {
                    //recipe 1 - chocolate cake
                    binding!!.ivRecipeImage.setImageResource(R.drawable.recipe1)
                } else if (image == "recipe2") {
                    //recipe 2 - chicken adobo
                    binding!!.ivRecipeImage.setImageResource(R.drawable.recipe2)
                }else if (image == "recipe3") {
                    //recipe 3 - mango float
                    binding!!.ivRecipeImage.setImageResource(R.drawable.recipe3)
                }else if (image == "recipe4") {
                    //recipe 4 - sinigang
                    binding!!.ivRecipeImage.setImageResource(R.drawable.recipe4)
                }else if (image == "recipe5") {
                    //recipe 5 - chocolate cake
                    binding!!.ivRecipeImage.setImageResource(R.drawable.recipe5)
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