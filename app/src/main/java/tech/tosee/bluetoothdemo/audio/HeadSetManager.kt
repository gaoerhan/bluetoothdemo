package tech.tosee.bluetoothdemo.audio

import android.content.*
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent

/**
 * @author: geh
 * @date: 2021/7/29
 */
class HeadSetManager(context: Context) {

    private val mContext = context
    private lateinit var headPhonePlugReceiver: HeadPhonePlugReceiver
    private var isRegist = false
    private var audioManager: AudioManager? = null
    private var mComponentName:ComponentName? = null

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mComponentName =  ComponentName(context.getPackageName(), MediaButtonReceiver::class.java.name)
    }
    fun startHeadset(){
        audioManager?.registerMediaButtonEventReceiver(mComponentName)
        registHeadSetBroad()
    }

    fun stopHeadset(){
        audioManager?.unregisterMediaButtonEventReceiver(mComponentName)
        unRegistHeadSetBroad()
        audioManager = null
        mComponentName = null
    }




    fun registHeadSetBroad(){
        headPhonePlugReceiver = HeadPhonePlugReceiver()
        val filter = IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        if (!isRegist) {
            mContext.registerReceiver(headPhonePlugReceiver, filter)
            isRegist = true
        }
    }

    fun unRegistHeadSetBroad(){
        if (isRegist) {
            mContext.unregisterReceiver(headPhonePlugReceiver)
            isRegist = false
        }
    }


    inner class HeadPhonePlugReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action
            when (action) {
                Intent.ACTION_HEADSET_PLUG -> {
                    if (intent.hasExtra("state")) {
                        val state = intent.getIntExtra("state", 0)
                        if (state == 0) {
                            audioManager?.unregisterMediaButtonEventReceiver(mComponentName)
                        } else if (state == 1) {
                            audioManager?.registerMediaButtonEventReceiver(mComponentName)
                        }
                    }
                }
            }
        }
    }



     class MediaButtonReceiver : BroadcastReceiver() {
        private val TAG = "mediabuttonreceiver"
        override fun onReceive(context: Context?, intent: Intent) {

            // ??????Action
            val intentAction = intent.getAction()
            // ??????KeyEvent??????
            val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT) as KeyEvent

            Log.e(
                TAG, "Action ---->" + intentAction + "  KeyEvent----->"
                        + keyEvent.toString()
            )
            // ?????? / ?????? ??????
            val keyAction = keyEvent.action

            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)
                && (KeyEvent.ACTION_DOWN == keyAction)
            ) {
                // ?????????????????????
                val keyCode = keyEvent.keyCode

                // ?????????????????????
                val downtime = keyEvent.eventTime

                // ??????????????? keyCode
                val sb = StringBuilder()
                // ?????????????????????????????? ??? ??????????????????????????????
                if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                    sb.append("KEYCODE_MEDIA_NEXT")
                }
                // ????????????????????????MEDIA_BUTTON???????????????????????????????????? KEYCODE_HEADSETHOOK ?????????
                // KEYCODE_MEDIA_PLAY_PAUSE
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
                    sb.append("KEYCODE_MEDIA_PLAY_PAUSE")
                    sb.append("KEYCODE_MEDIA_PLAY_PAUSE")
                }
                if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                    sb.append("KEYCODE_HEADSETHOOK")
                }
                if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
                    sb.append("KEYCODE_MEDIA_PREVIOUS")
                }
                if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
                    sb.append("KEYCODE_MEDIA_STOP")
                }
                // ????????????????????????
                Log.e(TAG, sb.toString())
//			Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show()
            } else if (KeyEvent.ACTION_UP == keyAction) {
                Log.e(TAG, "aaa")
            }

        }
    }
}