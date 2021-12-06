package tech.tosee.bluetoothdemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tech.tosee.bluetoothdemo.audio.MyAudioManager
import tech.tosee.bluetoothdemo.base.BaseActivity
import tech.tosee.bluetoothdemo.audio.MyAudioPlayManger
import kotlin.concurrent.thread
import kotlin.coroutines.*

class MainActivity : BaseActivity() {
    val TAG = "MainActivity"

    var myAudioPlayManger: MyAudioPlayManger? = null
    private var audioManager: MyAudioManager? = null
    private var isStart = false
    private var isPlay = true
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            tv_main_volume.text = msg.what.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAllPermission()
        initView()
        audioManager = MyAudioManager(this)
        audioManager?.start()
        if (!isStart) {
            startRecord()
            btn_start_play.text = resources.getString(R.string.main_stop_record)
            isStart = true
        }

        suspend {
            Log.e("testlog","挂起第一次开始")
            suspendFun2("hello","kotlin")
            Log.e("testlog","挂起第一次结束")
            suspendFun2("hello","coroutine")
            Log.e("testlog","挂起第二次结束")
            555
        }.startCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = LogInterceptor()

            override fun resumeWith(result: Result<Int>) {
                Log.e("testlog","result:${result.getOrNull()}")
            }

        })
    }

    suspend fun suspendFun2(a:String,b:String)
            = suspendCoroutine<Int> {continuation ->
        thread {
            Log.e("testlog","开始执行")
            Thread.sleep(1000)
            Log.e("testlog","执行结束")
            continuation.resumeWith(Result.success(123))
            Log.e("testlog","结果反馈")

        }
    }

    class LogInterceptor:ContinuationInterceptor{
        override val key = ContinuationInterceptor
        override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
            return LogContinuation(continuation)
        }
    }

    class LogContinuation<T>(private val continuation: Continuation<T>)
        :Continuation<T> by continuation{
        override fun resumeWith(result: Result<T>) {
            Log.e("testlog","拦截器开始 ${result}")
            continuation.resumeWith(result)
            Log.e("testlog","拦截器结束")
        }
    }

    private fun initView() {
        btn_start_play.setOnClickListener({
            if (!isStart) {
                startRecord()
                btn_start_play.text = resources.getString(R.string.main_stop_record)
                isStart = true
            } else {
                stopRecord()
                btn_start_play.text = resources.getString(R.string.main_start_record)
                tv_main_volume.text = "0"
                isStart = false
            }
        })
        rg_play_mode_select.setOnCheckedChangeListener { group, checkId ->
            when (checkId) {
                R.id.rb_mode_communication -> audioManager?.setAudioMode(android.media.AudioManager.MODE_IN_COMMUNICATION)
                R.id.rb_mode_call -> audioManager?.setAudioMode(android.media.AudioManager.MODE_IN_CALL)
                R.id.rb_mode_normal -> audioManager?.setAudioMode(android.media.AudioManager.MODE_NORMAL)
                R.id.rb_mode_ring -> audioManager?.setAudioMode(android.media.AudioManager.MODE_RINGTONE)
            }
        }
        rg_channel_select.setOnCheckedChangeListener { group, checkId ->
            when (checkId) {
                R.id.rb_opensco_stopspeaker -> audioManager?.startScoAndStopSpeaker()
                R.id.rb_stopsco_openspeaker -> audioManager?.stopScoAndOpenSpeaker()
                R.id.rb_stopsco_stopspeaker -> audioManager?.stopScoAndStopSpeaker()
            }
        }

        btn_open_speaker.setOnClickListener { view -> audioManager?.openSpeaker() }
        btn_open_stop_play.setOnClickListener { view ->

            if (isPlay) {
                isPlay = false
                myAudioPlayManger?.pausePlay(isPlay)
                btn_open_stop_play.setText("播放")
            } else {
                isPlay = true
                myAudioPlayManger?.pausePlay(isPlay)
                btn_open_stop_play.setText("停止")
            }

        }
    }


    fun startRecord() {
        myAudioPlayManger = MyAudioPlayManger()
        myAudioPlayManger?.initPlayer()
        myAudioPlayManger?.setVolumeCall(mHandler)
        myAudioPlayManger?.startPlayer()
    }

    fun stopRecord() {
        myAudioPlayManger?.stopPlayer()
        myAudioPlayManger = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecord()
        audioManager?.stop()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.e(TAG, "keycode: " + keyCode)
        if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) { //按下了耳机键
            if (event.getRepeatCount() == 0) {  //如果长按的话，getRepeatCount值会一直变大
                //短按
                Log.e(TAG, "短按")
            } else {
                //长按
                Log.e(TAG, "长安 ${event.getRepeatCount()}")
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}