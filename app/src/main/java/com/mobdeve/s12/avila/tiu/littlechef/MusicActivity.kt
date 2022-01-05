package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.AudioManager

import android.media.MediaPlayer
import android.widget.ImageView

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

import android.widget.TextView
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityMusicBinding
import android.os.Handler
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_music.*
import com.mobdeve.s12.avila.tiu.littlechef.R


class MusicActivity : AppCompatActivity() {

    var play: ImageView? = null
    var prev:ImageView? = null
    var next:ImageView? = null
    var imageView:ImageView? = null
    var songTitle: TextView? = null
    var mSeekBarTime: SeekBar? = null
    var mMediaPlayer: MediaPlayer? = null
    var currentIndex = 0


    var binding: ActivityMusicBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        // initializing views

        play = binding!!.play
        prev = binding!!.prev
        next = binding!!.next
        songTitle = binding!!.songTitle
        imageView = binding!!.imageView
        mSeekBarTime = binding!!.seekBarTime

        // creating an ArrayList to store our songs
        val songs = ArrayList<Int>()

        songs.add(0, R.raw.song1)
        songs.add(0, R.raw.song2)
        songs.add(0, R.raw.song3)
        songs.add(0, R.raw.song4)
        songs.add(0, R.raw.song5)

        // intializing mediaplayer
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex))


        play!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mSeekBarTime!!.max = mMediaPlayer!!.getDuration()
                if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
                    mMediaPlayer!!.pause()
                    play!!.setImageResource(R.drawable.play_btn)
                } else {
                    mMediaPlayer!!.start()
                    initializeSeekBar()
                    play!!.setImageResource(R.drawable.pause_btn)
                }
                songNames()
            }
        })

        next!!.setOnClickListener {
            if (mMediaPlayer != null) {
                play!!.setImageResource(R.drawable.pause_btn)
            }
            if (currentIndex < songs.size - 1) {
                currentIndex++
            } else {
                currentIndex = 0
            }
            if (mMediaPlayer!!.isPlaying()) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer = MediaPlayer.create(applicationContext, songs[currentIndex])
            mMediaPlayer!!.start()
            songNames()
        }


        prev!!.setOnClickListener {
            if (mMediaPlayer != null) {
                play!!.setImageResource(R.drawable.pause_btn)
            }
            if (currentIndex > 0) {
                currentIndex--
            } else {
                currentIndex = songs.size - 1
            }
            if (mMediaPlayer!!.isPlaying()) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer = MediaPlayer.create(applicationContext, songs[currentIndex])
            mMediaPlayer!!.start()
            songNames()
        }
    }

    private fun songNames() {
        if (currentIndex == 0) {
            binding!!.songTitle.setText("Cute");
            binding!!.imageView.setImageResource(R.drawable.song1);
        }
        if (currentIndex == 1) {
            binding!!.songTitle.setText("Bubblegum K.K.");
            binding!!.imageView.setImageResource(R.drawable.song2);
        }
        if (currentIndex == 2) {
            binding!!.songTitle.setText("Debate");
            binding!!.imageView.setImageResource(R.drawable.song3);
        }
        if (currentIndex == 3) {
            binding!!.songTitle.setText("Sophisticated");
            binding!!.imageView.setImageResource(R.drawable.song4);
        }
        if (currentIndex == 4) {
            binding!!.songTitle.setText("Celebration");
            binding!!.imageView.setImageResource(R.drawable.song5);
        }

        seekBarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mMediaPlayer?.seekTo(progress)
                    seekBarTime.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })
    }

    private fun initializeSeekBar() {
        seekBarTime.max = mMediaPlayer!!.duration

        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run() {
                try {
                    seekBarTime.progress = mMediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e:Exception) {
                    seekBarTime.progress = 0
                }
            }
        },0)
    }

}
