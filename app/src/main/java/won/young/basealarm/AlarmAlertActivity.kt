package won.young.basealarm

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import won.young.basealarm.databinding.ActivityAlarmAlertBinding

class AlarmAlertActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmAlertBinding

    private var isPlaying = false
    private var mediaPlayer: MediaPlayer? = null

    private var vibrator: Vibrator? = null
    private val pattern = longArrayOf(100,200,300,400,300,200)

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
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        mediaPlayer = MediaPlayer()
        initListener()
        play()
    }

    private fun initListener() {
        binding.dismissButton.setOnClickListener {
            stop()
            WakeLock.releaseCpuLock()
            finish()
        }
    }
    override fun onBackPressed() {
        if (isPlaying) return
        super.onBackPressed()
    }

    private fun play() {
        isPlaying = true
        val mediaURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        mediaPlayer!!.setDataSource(this, mediaURI)
        startAlarm(mediaPlayer!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = intArrayOf(20,20,20,20,20,20)
            val waveform = VibrationEffect.createWaveform(pattern, amplitude, 0)
            vibrator!!.vibrate(waveform)
        } else {
            vibrator!!.vibrate(pattern, 0)
        }
    }

    private fun startAlarm(player: MediaPlayer) {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * 0.7).toInt(), AudioManager.FLAG_PLAY_SOUND)
        player.setAudioStreamType(AudioManager.STREAM_ALARM)
        player.isLooping = true
        player.prepare()
        player.start()
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
}