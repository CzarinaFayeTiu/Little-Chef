package com.mobdeve.s12.avila.tiu.littlechef.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mobdeve.s12.avila.tiu.littlechef.models.RecipeModel

class MyDbHelper(context: Context?):SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        //create table on that db
        db.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //upgrade database (if any structure change, change db version_
        //drop old table if exists

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME)
    }

    //insert record to db
    fun insertRecord(
        name:String?,
        category:String?,
        ingredients:String?,
        instructions:String?,
        image:String?,
        link:String?,
        bookmark:String?,
        addedTime:String?,
        updatedTime:String?,
    ): Long {
        //get writable database to write data
        val db = this.writableDatabase
        val values = ContentValues()
        //id will be inserted automatically as AUTOINCREMENT was set in query
        //insert data
        values.put(Constants.C_NAME, name)
        values.put(Constants.C_CATEGORY, category)
        values.put(Constants.C_INGREDIENTS, ingredients)
        values.put(Constants.C_INSTRUCTIONS, instructions)
        values.put(Constants.C_IMAGE, image)
        values.put(Constants.C_LINK, link)
        values.put(Constants.C_BOOKMARK, bookmark)
        values.put(Constants.C_ADDED_TIMESTAMP, addedTime)
        values.put(Constants.C_UPDATED_TIMESTAMP, updatedTime)

        //insert tow, it will return record id of saved record
        val id = db.insert(Constants.TABLE_NAME, null, values)
        //close db connection
        db.close()
        //return id of inserted record
        return id
    }

    //get all data
    fun getAllRecipes(orderBy:String): ArrayList<RecipeModel> {
        //it will return list of records since we have used return type ArrayList<RecipeModel>
        val recipeList = ArrayList<RecipeModel>()
        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} ORDER BY $orderBy"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        //looping through all records and add to list
        if (cursor.moveToFirst()) {
            do {
                val id = cursor!!.getString(0)
                val name = cursor!!.getString(1)
                val category = cursor!!.getString(2)
                val ingredients = cursor!!.getString(3)
                val instructions = cursor!!.getString(4)
                val image = cursor!!.getString(5)
                val link = cursor!!.getString(6)
                val bookmark = cursor!!.getString(7)
                val addedTime = cursor!!.getString(8)
                val updatedTime = cursor!!.getString(9)
                //add record to list
                recipeList.add(RecipeModel(id,name,category,ingredients,instructions,image,link,bookmark,addedTime,updatedTime))
            } while (cursor.moveToNext())
        }
        //close db
        db.close()
        //return to queried result list
        return recipeList
    }

    //search data by Typing
    fun searchRecords(query:String):ArrayList<RecipeModel> {
        val recipeList = ArrayList<RecipeModel>()

        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_NAME} LIKE '%$query%'"
        val db = this.writableDatabase
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
                val bookmark = cursor!!.getString(7)
                val addedTime = cursor!!.getString(8)
                val updatedTime = cursor!!.getString(9)
                //add record to list
                recipeList.add(RecipeModel(id,name,category,ingredients,instructions,image,link,bookmark,addedTime,updatedTime))
            } while (cursor.moveToNext())
        }
        //close db
        db.close()
        //return to queried result list
        return recipeList
    }

    //search data by Tags
    fun searchCategories(query:String):ArrayList<RecipeModel> {
        val recipeList = ArrayList<RecipeModel>()

        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_CATEGORY} LIKE '%$query%'"
        val db = this.writableDatabase
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
                val bookmark = cursor!!.getString(7)
                val addedTime = cursor!!.getString(8)
                val updatedTime = cursor!!.getString(9)
                //add record to list
                recipeList.add(RecipeModel(id,name,category,ingredients,instructions,image,link,bookmark,addedTime,updatedTime))
            } while (cursor.moveToNext())
        }
        //close db
        db.close()
        //return to queried result list
        return recipeList
    }

    //search data using Bookmarks
    fun searchBookmarks():ArrayList<RecipeModel> {
        val recipeList = ArrayList<RecipeModel>()
        var q = "bookmarked"

        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.C_BOOKMARK} LIKE '%$q%'"
        val db = this.writableDatabase
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
                val bookmark = cursor!!.getString(7)
                val addedTime = cursor!!.getString(8)
                val updatedTime = cursor!!.getString(9)
                //add record to list
                recipeList.add(RecipeModel(id,name,category,ingredients,instructions,image,link,bookmark,addedTime,updatedTime))
            } while (cursor.moveToNext())
        }
        //close db
        db.close()
        //return to queried result list
        return recipeList
    }

    //update record to db - Bookmarks
    fun updateRecord (id:String,
                        bookmark: String?): Long
    {
        //get writeable database
        val db = this.writableDatabase
        val values = ContentValues()

        //insert data
        values.put(Constants.C_BOOKMARK, bookmark)

        //update
        return db.update(Constants.TABLE_NAME,
            values,
            "${Constants.C_ID}=?",
            arrayOf(id)).toLong()
    }


}