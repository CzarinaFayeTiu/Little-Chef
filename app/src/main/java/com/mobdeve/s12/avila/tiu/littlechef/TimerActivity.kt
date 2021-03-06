package com.mobdeve.s12.avila.tiu.littlechef

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityTimerBinding

class TimerActivity : DrawerBaseActivity()  {
    private var binding: ActivityTimerBinding? = null
    private var picker: MaterialTimePicker? = null
    private var calendar: Calendar? = null
    //AlarmManager is a bridge between application and Android system alarm service.
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        createNotificationChannel()
        binding!!.selectTimeBtn.setOnClickListener{ showTimePicker() }
        binding!!.setAlarmBtn.setOnClickListener{ setAlarm() }
        binding!!.cancelAlarmBtn.setOnClickListener{ cancelAlarm() }
    }

    //to cancel the alarm
    private fun cancelAlarm() {
        val intent = Intent(this, TimerReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        if (alarmManager == null) {
            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        }
        alarmManager!!.cancel(pendingIntent)
        binding!!.selectedTime.text = "No alarm has been set"
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }

    //when user presses set alarm button
    private fun setAlarm() {
        // provides access to the system alarm services
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, TimerReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager!!.setExact(
            AlarmManager.RTC_WAKEUP, calendar!!.timeInMillis, pendingIntent
        )

        if (picker!!.hour > 12) {
            binding!!.selectedTime.setText(
                String.format("Timer set to \n"+
                        "%02d",
                    picker!!.hour - 12
                ) + " : " + String.format("%02d", picker!!.minute) + " PM"
            )
        } else {
            binding!!.selectedTime.setText("Timer set to \n"+picker!!.hour.toString() + " : " + picker!!.minute + " AM")
        }

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

    //this is where user will select the alarm time on the app
    private fun showTimePicker() {
        /*
            MatrialTimerPicker - A Dialog with a clock display and a clock
            face to choose the time.
         */
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12) //12-hr format
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()
        picker!!.show(supportFragmentManager, "littlechef")
        picker!!.addOnPositiveButtonClickListener {
            if (picker!!.hour > 12) { //PM
                binding!!.selectedTime.text =
                    String.format("Selected time is \n"+
                            "%02d",
                        picker!!.hour - 12
                    ) + " : " + String.format("%02d", picker!!.minute) + " PM" + "\nClick on Set Alarm"

            } else { //AM
                binding!!.selectedTime.text =
                    "Selected time is \n"+picker!!.hour.toString() + " : " + picker!!.minute + " AM" + "\nClick on Set Alarm"
            }

            Toast.makeText(this, "Click on Set Alarm to be alerted", Toast.LENGTH_SHORT).show()


            /*
                Calendar's getInstance method returns a Calendar object whose
                calendar fields have been initialized with the current date and time
             */
            calendar = Calendar.getInstance()
            calendar!![Calendar.HOUR_OF_DAY] =  picker!!.hour
            calendar!![Calendar.MINUTE] = picker!!.minute
            calendar!![Calendar.SECOND] = 0
            calendar!![Calendar.MILLISECOND] = 0
        }



    }

    private fun createNotificationChannel() {
        /*
            check if device running the app has Android SDK 26 or up
            Notification channels were added in Android 8.0. Attempting
            to call createNotificationChannel() on older devices would
            result in a crash, as that method will not exist.
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "littlechefReminderChannel"
            val description = "Reminder to check on your dish"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("littlechef", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            //timerReceiver has a broadcast receiver to known when time's up
            notificationManager.createNotificationChannel(channel)

            //once timer has gone off reset the timer
            binding!!.selectedTime.text = "No alarm has been set"
        }
    }
}