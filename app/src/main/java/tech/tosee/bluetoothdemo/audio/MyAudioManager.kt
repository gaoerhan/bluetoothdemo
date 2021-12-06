package tech.tosee.bluetoothdemo.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import android.util.Log


class MyAudioManager {
    val TAG = "AudioManager"
    private val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
    private val EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE"
    private val routeChangeListener: RouteChangeListener? = null
    private var bluetoothManager: DBBluetoothManager? = null
    private var audioManager: AudioManager? = null
    private var headSetManager: HeadSetManager? = null
    private var savedIsSpeakerPhoneOn = false
    private var savedIsMicrophoneMute = false
    private var audioFocusChangeListener: OnAudioFocusChangeListener? = null
    private var mAudioFocusRequest: AudioFocusRequest? = null
    private var wiredHeadsetReceiver: BroadcastReceiver? = null
    private var mVolumeReceiver: VolumeReceiver? = null
    private var mContext: Context? = null

    inner class WiredHeadsetReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra("state", STATE_UNPLUGGED)
            val microphone = intent.getIntExtra("microphone", HAS_NO_MIC)
            val name = intent.getStringExtra("name")
            Log.e(TAG, "耳机广播 刷新")
        }

            private  val STATE_UNPLUGGED = 0
            private  val TAG = "AudioManager"
            private  val STATE_PLUGGED = 1
            private  val HAS_NO_MIC = 0
            private  val HAS_MIC = 1

    }

    inner class VolumeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (VOLUME_CHANGED_ACTION == intent.action && (intent.getIntExtra(
                    EXTRA_VOLUME_STREAM_TYPE,
                    -1
                ) == AudioManager.STREAM_VOICE_CALL
                        || intent.getIntExtra(
                    EXTRA_VOLUME_STREAM_TYPE,
                    -1
                ) == 6 /*6表示蓝牙*/)
            ) {
                val streamVolume: Int = getStreamVolume(AudioManager.STREAM_VOICE_CALL)

            }
        }
    }

    fun getStreamVolume(streamType: Int): Int {
        return audioManager!!.getStreamVolume(streamType)
    }

    constructor(context: Context) {
        mContext = context
        bluetoothManager = DBBluetoothManager.create(context,this)
        wiredHeadsetReceiver = WiredHeadsetReceiver()
        mVolumeReceiver = VolumeReceiver()
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        headSetManager = HeadSetManager(context)
    }

    fun start() {

        savedIsSpeakerPhoneOn = audioManager!!.isSpeakerphoneOn
        savedIsMicrophoneMute = audioManager!!.isMicrophoneMute
        audioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
            val typeOfChange: String
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    typeOfChange = "AUDIOFOCUS_GAIN"
                }
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> typeOfChange = "AUDIOFOCUS_GAIN_TRANSIENT"

                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE -> typeOfChange =
                    "AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE"
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> typeOfChange =
                    "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK"
                AudioManager.AUDIOFOCUS_LOSS -> typeOfChange = "AUDIOFOCUS_LOSS"
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
//                    bluetoothManager?.resetState()
                    typeOfChange = "AUDIOFOCUS_LOSS_TRANSIENT"

                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> typeOfChange =
                    "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK"
                else -> typeOfChange = "AUDIOFOCUS_INVALID"

            }
            Log.d(TAG, "onAudioFocusChange: $typeOfChange")

        }
        var requestFocusResult = 0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            requestFocusResult = audioManager!!.requestAudioFocus(
                audioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        } else {
            if (mAudioFocusRequest == null) {
                mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                            .build()
                    )
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener!!)
                    .build()
            }
            requestFocusResult = mAudioFocusRequest?.let { audioManager!!.requestAudioFocus(it) }
                ?: 0
        }
        if (requestFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(TAG, "Audio focus request granted for VOICE_CALL streams")
        } else {
            Log.e(TAG, "Audio focus request failed")
        }
        audioManager!!.mode = AudioManager.MODE_IN_COMMUNICATION
        setMicrophoneMute(false)
        registerReceiver(wiredHeadsetReceiver, IntentFilter(Intent.ACTION_HEADSET_PLUG))
        registerReceiver(
            mVolumeReceiver,
            IntentFilter(VOLUME_CHANGED_ACTION)
        )
        bluetoothManager?.start()
        headSetManager?.startHeadset()


    }


    fun stop() {
        bluetoothManager?.stop()
        headSetManager?.stopHeadset()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            audioManager!!.abandonAudioFocus(audioFocusChangeListener)
        } else {
            if (mAudioFocusRequest != null) {
                audioManager!!.abandonAudioFocusRequest(mAudioFocusRequest!!)
            }
        }
        audioFocusChangeListener = null
    }


    fun setAudioMode(mode: Int){
        audioManager?.mode = mode
    }
    fun startScoAndStopSpeaker() {
        bluetoothManager?.startScoAudio()
        audioManager?.setSpeakerphoneOn(false)

    }
    fun openSpeaker(){
        audioManager?.setSpeakerphoneOn(true)
    }

    fun stopScoAndOpenSpeaker() {
        bluetoothManager?.stopScoAudio()
        audioManager?.setSpeakerphoneOn(true)

    }

    fun stopScoAndStopSpeaker(){
        bluetoothManager?.stopScoAudio()
        audioManager?.setSpeakerphoneOn(false)
    }

    private fun setMicrophoneMute(on: Boolean) {
        val wasMuted = audioManager!!.isMicrophoneMute
        if (wasMuted == on) {
            return
        }
        audioManager!!.isMicrophoneMute = on
    }

    private fun setSpeakerphoneOn(on: Boolean) {
        if (on) {
            audioManager!!.isSpeakerphoneOn = on
        } else {
            audioManager!!.isSpeakerphoneOn = false //关闭扬声器
        }
    }

    internal interface RouteChangeListener {
        fun onRouteChange()
    }

    private fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter) {
        mContext?.registerReceiver(receiver, filter)
    }

    /**
     * Helper method for unregistration of an existing receiver.
     */
    private fun unregisterReceiver(receiver: BroadcastReceiver) {
        mContext?.unregisterReceiver(receiver)
    }

}