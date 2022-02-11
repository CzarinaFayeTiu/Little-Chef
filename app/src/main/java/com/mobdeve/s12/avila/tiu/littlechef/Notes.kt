package com.mobdeve.s12.avila.tiu.littlechef

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// room database integration - Table names in SQLite are case-insensitive
@Entity(tableName = "Notes")
class Notes: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    @ColumnInfo(name = "title")
    var title:String? = null

    @ColumnInfo(name = "sub_title")
    var subTitle:String? = null

    //date-time of when notes was created
    @ColumnInfo(name = "date_time")
    var dateTime:String? = null

    @ColumnInfo(name = "note_text")
    var noteText:String? = null

    //img_path of uploaded image from phone
    @ColumnInfo(name = "img_path")
    var imgPath:String? = null

    //web_link of recipes
    @ColumnInfo(name = "web_link")
    var webLink:String? = null

    //background color of the note card when saved
    @ColumnInfo(name = "color")
    var color:String? = null


    override fun toString(): String {

        return "$title : $dateTime"

    }
}