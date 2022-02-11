package com.mobdeve.s12.avila.tiu.littlechef



import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityNoteBinding

class NoteActivity: DrawerBaseActivity()  {
    var binding:ActivityNoteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        replaceFragment(HomeFragment.newInstance(),false)

    }

    /*
        Replace fragment function - activty_note.xml
        calls for a frame layout for design ; frame layout will be replace throughout the
        use of notes
     */
    fun replaceFragment(fragment: Fragment, istransition:Boolean){
        val fragmentTransition = supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.add(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragments = supportFragmentManager.fragments
        if (fragments.size == 0){
            finish()
        }
    }


}