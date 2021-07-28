package won.young.basealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import won.young.basealarm.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var hour: Int? = null
    var minute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initListener() // initiate button listener

    }

    private fun initListener() {
        binding.button.setOnClickListener {
            /*
            * You can get your country's time with getInstance(TimeZone.getTimeZone("your country location"))
            * I'm in Korea so I wrote this with Seoul
             */
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            var month = calendar.get(Calendar.MONTH) // Calendar.MONTH starts with 0. So June displays 5. January displays 0.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = binding.timePicker.hour
                minute = binding.timePicker.minute
            } else {
                hour = binding.timePicker.currentHour
                minute = binding.timePicker.currentMinute
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour!!)
            calendar.set(Calendar.MINUTE, minute!!)
            calendar.set(Calendar.SECOND, 0)

            setAlarm(calendar)

            Toast.makeText(this, "I'll notice you at ${calendar.get(Calendar.YEAR)}/${month+1}/${calendar.get(Calendar.DATE)} ${hour}:${minute}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setAlarm(calendar: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val aci = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)

        alarmManager.setAlarmClock(aci, pendingIntent)
    }
}