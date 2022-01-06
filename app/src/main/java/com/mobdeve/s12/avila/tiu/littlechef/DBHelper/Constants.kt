package com.mobdeve.s12.avila.tiu.littlechef.DBHelper

object Constants {
    //db name
    const val DB_NAME = "LITTLECHEF_DB"
    const val DB_VERSION = 1
    const val TABLE_NAME = "RECIPES_TABLE"

    //columns/fields
    const val C_ID = "ID"
    const val C_NAME = "NAME"
    const val C_CATEGORY = "CATEGORY"
    const val C_INGREDIENTS = "INGREDIENTS"
    const val C_INSTRUCTIONS = "INSTRUCTIONS"
    const val C_IMAGE = "IMAGE"
    const val C_LINK = "LINK"
    const val C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP"
    const val C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP"

    //create table query
    const val CREATE_TABLE = (
            "CREATE TABLE " + TABLE_NAME + "(" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    C_NAME + " TEXT," +
                    C_CATEGORY + " TEXT," +
                    C_INGREDIENTS + " TEXT," +
                    C_INSTRUCTIONS + " TEXT," +
                    C_IMAGE + " TEXT," +
                    C_LINK + " TEXT," +
                    C_ADDED_TIMESTAMP + " TEXT," +
                    C_UPDATED_TIMESTAMP + " TEXT" +
                    ")"
            )


}