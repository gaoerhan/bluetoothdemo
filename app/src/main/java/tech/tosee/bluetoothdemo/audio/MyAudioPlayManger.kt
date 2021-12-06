
package tech.tosee.bluetoothdemo.audio

import android.os.Handler
import android.util.Log
import tech.tosee.bluetoothdemo.utils.AudioUtil


/**
 * @author: geh
 * @date: 2021/7/20
 */
class MyAudioPlayManger {

    private var audioTrack: MyAudioTrack? = null
    private var audioRecord: MyAudioRecord? = null
    private var volumeCallback: Handler? = null
    @Volatile
    private var isStop = false

    private var lasttime = 0L
    private var nullData = ByteArray(1280){0}
    private val dataCallBack: MyAudioRecord.AudioDataCallBack = object : MyAudioRecord.AudioDataCallBack{
        override fun onAudioData(byteArray: ByteArray) {
            if(!isStop) audioTrack?.addAudioData(byteArray)
            else audioTrack?.addAudioData(nullData)
            val now = System.currentTimeMillis()
            if(now - lasttime > 200){
                volumeCallback?.sendEmptyMessage(AudioUtil.culateVolume(byteArray))
                lasttime = now
            }
        }
    }

    fun initPlayer(){
        audioTrack = MyAudioTrack()
        audioRecord = MyAudioRecord()
        audioTrack?.initTrack()
        audioRecord?.let {
            it.initRecordIng()
            it.setAudioDataCallBack(dataCallBack)
        }
    }


    fun startPlayer(){
        audioRecord?.startAudioData()
        audioTrack?.startPlay()
    }


    fun stopPlayer(){
        audioRecord?.stopRecord()
        audioTrack?.stopPlay()
        volumeCallback = null

    }

    fun setVolumeCall(handler:Handler){
        volumeCallback = handler
    }


    fun pausePlay(isStop:Boolean){
        this.isStop = isStop
    }

}