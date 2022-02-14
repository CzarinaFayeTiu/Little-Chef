package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.Constants
import com.mobdeve.s12.avila.tiu.littlechef.DBHelper.MyDbHelper
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMainBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityRecipeDetailBinding
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.util.*

class RecipeDetailActivity : YouTubeBaseActivity() {

    //db helper
    private var dbHelper:MyDbHelper? = null
    private var recipeId:String? = null
    lateinit var tts:TextToSpeech
    var binding: ActivityRecipeDetailBinding? = null

    //YouTube
    lateinit var VIDEO_ID:String
    val YOUTUBE_API_KEY = "AIzaSyBVMcIdayZEZt4ZhX4ac1ee6aTj0cyXO0A"

    private lateinit var youtubePlayer: YouTubePlayerView

    lateinit var youtubePlayerInit : YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        //init db helper
        dbHelper = MyDbHelper(this)

        //get record id from intent
        val intent = intent
        recipeId = intent.getStringExtra("RECORD_ID")

        showRecordDetails()

        /** Youtube Player **/
        youtubePlayer = binding!!.youtubePlayer

        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.cueVideo(VIDEO_ID)
                p1?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "Video Error Occurred", Toast.LENGTH_LONG).show()
            }
        }

        youtubePlayer.initialize(YOUTUBE_API_KEY, youtubePlayerInit)



        /** Text to Speech **/

        tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                status ->
            if(status != TextToSpeech.ERROR){
                //set language
                tts.language = Locale.UK
            }
        })

        var speakBtn = binding!!.speakBtn
        var ingredientsSpeakBtn = binding!!.ingredientsSpeakBtn


        speakBtn.setOnClickListener {
                //get edit text
                val toSpeak = binding!!.tvRecipeInstructions.text.toString()

                if (tts.isSpeaking) {
                    tts.stop()
                    speakBtn!!.setImageResource(R.drawable.ic_baseline_volume_up_24)
                } else {
                    if(toSpeak == ""){
                        //if no text is written
                        Toast.makeText(this, "Enter Text", Toast.LENGTH_SHORT).show()
                    }else{
                        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
                        speakBtn!!.setImageResource(R.drawable.ic_baseline_volume_off_24)
                    }
                }
        }
        ingredientsSpeakBtn.setOnClickListener {
            val ingredientsSpeak = binding!!.tvRecipeIngredients.text.toString()
            if (tts.isSpeaking) {
                tts.stop()
                ingredientsSpeakBtn!!.setImageResource(R.drawable.ic_baseline_volume_up_24)
            } else {
                if(ingredientsSpeak == ""){
                    //if no text is written
                    Toast.makeText(this, "Enter Text", Toast.LENGTH_SHORT).show()
                }else{
                    tts.speak(ingredientsSpeak, TextToSpeech.QUEUE_FLUSH, null)
                    ingredientsSpeakBtn!!.setImageResource(R.drawable.ic_baseline_volume_off_24)
                }
            }
        }


        //back Button
        binding!!.imgBack.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }


    }

    private fun showRecordDetails() {
        //get record details

        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_ID} =\"$recipeId\""

        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor!!.getString(1)
                val category = cursor!!.getString(2)
                val ingredients = cursor!!.getString(3)
                val instructions = cursor!!.getString(4)
                val link = cursor!!.getString(6)

                //set data
                binding!!.tvRecipeName.text = name
                binding!!.tvRecipeCategory.text = category
                binding!!.tvRecipeIngredients.text = ingredients
                binding!!.tvRecipeInstructions.text = instructions
                VIDEO_ID = link


            } while (cursor.moveToNext())
        }
        //close db connection
        db.close()
    }

}