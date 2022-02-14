package com.mobdeve.s12.avila.tiu.littlechef.models

//Model class for RecyclerView
data class RecipeModel(
    var id: String,
    var name: String,
    var category: String,
    var ingredients: String,
    var instructions: String,
    var image:String,
    var link: String,
    var bookmark: String,
    var addedTime:String,
    var updatedTime:String
)
