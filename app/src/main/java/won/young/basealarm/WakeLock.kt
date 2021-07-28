package won.young.basealarm

import android.content.Context
import android.os.PowerManager

object WakeLock {
    private var cpuWakeLock: PowerManager.WakeLock? = null

    fun acquireCpuWakeLock(context: Context){
        if (cpuWakeLock != null) return

        val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        cpuWakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "base:alarm"
        )
        cpuWakeLock!!.acquire()
    }

    fun releaseCpuLock() {
        if (cpuWakeLock != null) {
            cpuWakeLock!!.release()
            cpuWakeLock = null
        }
    }
}