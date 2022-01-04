package com.mobdeve.s12.avila.tiu.littlechef


import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes() : List<Notes>

    @Query("SELECT * FROM notes WHERE id =:id")
    fun getSpecificNote(id:Int) : Notes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(note:Notes)

    @Delete
    fun deleteNote(note:Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    fun deleteSpecificNote(id:Int)

    @Update
    fun updateNote(note:Notes)
}