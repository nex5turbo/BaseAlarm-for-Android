package won.young.basealarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

const val TAG = "AlarmReceiver"
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "received")

        val playAlarm = Intent(context, AlarmService::class.java)
        playAlarm.setAction("alarm_alert")
        context!!.startService(playAlarm)

        val intent = Intent(context, AlarmAlertActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
        context.startActivity(intent)
    }
}