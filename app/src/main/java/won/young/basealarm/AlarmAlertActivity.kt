package won.young.basealarm

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import won.young.basealarm.databinding.ActivityAlarmAlertBinding

class AlarmAlertActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setTurnScreenOn(true)
            setShowWhenLocked(true)
            val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            km.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_alert)
        binding.dismissButton.setOnClickListener {
            Log.d("###", "clicked")
            val stopAlarm = Intent(this, AlarmService::class.java)
            stopAlarm.setAction("alarm_alert")
            stopService(stopAlarm)
            finish()
        }
    }
}