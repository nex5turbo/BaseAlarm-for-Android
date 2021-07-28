package won.young.basealarm

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.Vibrator

class AlarmService: Service() {

    private var isPlaying = false
    private var vibrator: Vibrator? = null
    private var mediaPlayer: MediaPlayer? = null
    private val pattern = longArrayOf(500, 500)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        play()
        return START_STICKY
    }

    override fun onCreate() {
        WakeLock.acquireCpuWakeLock(this)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        super.onCreate()
    }

    override fun onDestroy() {
        stop()
        WakeLock.releaseCpuLock()
        super.onDestroy()
    }

    private fun stop() {
        if (isPlaying) {
            isPlaying = false
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            vibrator!!.cancel()
        }
    }

    private fun play() {
        isPlaying = true
        val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(this, alert)
        startAlarm(mediaPlayer!!)
        vibrator!!.vibrate(pattern, 0)
    }

    private fun startAlarm(player: MediaPlayer) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * 0.7).toInt(), AudioManager.FLAG_PLAY_SOUND)
        player.setAudioStreamType(AudioManager.STREAM_ALARM)
        player.isLooping = true
        player.prepare()
        player.start()
    }
}