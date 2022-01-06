package com.mobdeve.s12.avila.tiu.littlechef.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.avila.tiu.littlechef.R
import com.mobdeve.s12.avila.tiu.littlechef.RecipeDetailActivity
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel

//Adapter class for Recycler View
class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.HolderRecord>() {

    private var context: Context? = null
    private var recipeList:ArrayList<RecipeModel>? = null

    constructor(context: Context?, recipeList: ArrayList<RecipeModel>?) : this() {
        this.context = context
        this.recipeList = recipeList
    }

    inner class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView) {

        //views from row_recipe.xml
        var ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var btnMore: ImageButton = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        //inflate the layout row_record.xml
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.row_recipe, parent, false)
        )
    }

    override fun getItemCount(): Int {
        //return items/records/list size
        return recipeList!!.size
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        //get data, set data, handle clicks

        //get data
        val model = recipeList!!.get(position)

        val id = model.id
        val name = model.name
        val category = model.category
        val ingredients = model.ingredients
        val instructions = model.instructions
        val image = model.image
        val link = model.link
        val addedTime = model.addedTime
        val updatedTime = model.updatedTime

        //set data to views
        holder.tvName.text = name
        holder.tvCategory.text = category

        if (image == "null") {
            //no image in record, set default
            holder.ivImage.setImageResource(R.drawable.littlechef_logo)
        } else {
            //have image in record
            holder.ivImage.setImageURI(Uri.parse(image))
        }

        //show record in new activity on clicking record
        holder.itemView.setOnClickListener {
            //pass id to next activity to show record

            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("RECORD_ID", id)
            context!!.startActivity(intent)
        }

        //handle more button click: show delete/edit options
        holder.btnMore.setOnClickListener {
            //will implement later
        }
    }



}