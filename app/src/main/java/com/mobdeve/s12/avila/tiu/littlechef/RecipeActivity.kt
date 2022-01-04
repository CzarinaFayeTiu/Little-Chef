package com.mobdeve.s12.avila.tiu.littlechef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {

    var binding: ActivityRecipeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.tvIngredients.text = "1/2 whole chicken\n6 cloves garlic, chopped\n2 pcs bay leaves\n1/4 cup soy sauce\n1/3 cup cane vinegar"

        binding!!.tvInstructions.text = "1.    Marinade the chicken in soy sauce for at least 2 hours. Drain and reserve the soy sauce."
    }
}