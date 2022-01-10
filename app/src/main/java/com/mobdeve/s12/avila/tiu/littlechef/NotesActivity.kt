package com.mobdeve.s12.avila.tiu.littlechef

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityConverterBinding
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityNotesBinding

class NotesActivity : DrawerBaseActivity() {
    var binding: ActivityNotesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    }
}