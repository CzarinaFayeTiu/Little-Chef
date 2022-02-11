package com.mobdeve.s12.avila.tiu.littlechef


import androidx.room.*

@Dao
interface NoteDao {
    //to display all saved notes in notes-home when loading
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes() : List<Notes>

    //when selecting a single note and viewing saved contents
    @Query("SELECT * FROM notes WHERE id =:id")
    fun getSpecificNote(id:Int) : Notes

    //save notes when note has not been created yet
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(note:Notes)

    @Delete
    fun deleteNote(note:Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    fun deleteSpecificNote(id:Int)

    //update existing notes
    @Update
    fun updateNote(note:Notes)
}