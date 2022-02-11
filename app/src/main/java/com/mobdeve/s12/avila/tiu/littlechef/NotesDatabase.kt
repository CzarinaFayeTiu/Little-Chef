package com.mobdeve.s12.avila.tiu.littlechef


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
/*
    define entities to represent the objects that you want to store.
    Each entity corresponds to a table in the associated Room database,
    and each instance of an entity represents a row of data in the
    corresponding table
*/
@Database(entities = [Notes::class], version = 1, exportSchema = false)

abstract class NotesDatabase : RoomDatabase() {
    /*
        companion object is for writing a function or any member of the class
        that can be called without having the instance of the class.
        It is static
    */
    companion object {
        var notesDatabase: NotesDatabase? = null

        //name of db is notes db
        @Synchronized
        fun getDatabase(context: Context): NotesDatabase {
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context
                    , NotesDatabase::class.java
                    , "notes.db"
                ).allowMainThreadQueries().build()
            }
            return notesDatabase!!
        }
    }
    //The abstract methods merely define a contract that derived classes must implement
    abstract fun noteDao():NoteDao
}