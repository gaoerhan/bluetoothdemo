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

            // 获得Action
            val intentAction = intent.getAction()
            // 获得KeyEvent对象
            val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT) as KeyEvent

            Log.e(
                TAG, "Action ---->" + intentAction + "  KeyEvent----->"
                        + keyEvent.toString()
            )
            // 按下 / 松开 按钮
            val keyAction = keyEvent.action

            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)
                && (KeyEvent.ACTION_DOWN == keyAction)
            ) {
                // 获得按键字节码
                val keyCode = keyEvent.keyCode

                // 获得事件的时间
                val downtime = keyEvent.eventTime

                // 获取按键码 keyCode
                val sb = StringBuilder()
                // 这些都是可能的按键码 ， 打印出来用户按下的键
                if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                    sb.append("KEYCODE_MEDIA_NEXT")
                }
                // 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
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
                // 输出点击的按键码
                Log.e(TAG, sb.toString())
//			Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show()
            } else if (KeyEvent.ACTION_UP == keyAction) {
                Log.e(TAG, "aaa")
            }

        }
    }
}