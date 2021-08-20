package tech.tosee.bluetoothdemo.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process

/*
 * @author: geh
 * @version: V100R001C01
 * @date: 2021/4/13
 */
class MyAudioRecord() {
    private var audioRecord: AudioRecord? = null
    @Volatile
    private var isRecording = false
    private var audioDataCallBack: AudioDataCallBack? = null
    var buffersize:Int = 1280


     fun initRecordIng() {
        if (audioRecord != null) {
            return
        }
//         buffersize =
//            AudioRecord.getMinBufferSize(SAMPLERATE, CHANNEL, AUDIOFORMAT)
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION,
            SAMPLERATE, CHANNEL, AUDIOFORMAT, buffersize
        )
    }



    fun startAudioData() {
        if (isRecording) return

        try {
            audioRecord?.startRecording()
        } catch (e: Exception) {
            e.printStackTrace()
            stopRecord()
        }
        isRecording = true
        Thread {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
            while (audioRecord != null && audioRecord?.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                // Wait until we can start recording...
            }
            val buffer = ByteArray(buffersize)
            while (isRecording && !Thread.interrupted()) {
                val ret: Int = audioRecord?.read(buffer, 0, buffer.size)?:-1
                if (ret == AudioRecord.ERROR || ret == AudioRecord.ERROR_INVALID_OPERATION || ret == AudioRecord.ERROR_BAD_VALUE) {
                    stopRecord()
                }else{
                    audioDataCallBack?.onAudioData(buffer)
                }
            }
        }.start()

    }


    fun stopRecord() { //停止录音
        if(!isRecording)return
        isRecording = false
        if (audioRecord != null) {
            audioRecord!!.setRecordPositionUpdateListener(null)
            try {
                audioRecord!!.stop()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } finally {
                if (audioRecord != null) {
                    audioRecord!!.release()
                    audioRecord = null
                }
            }
        }
    }



    companion object {
        private const val TAG = "MyAudioRecord"
        private const val CHANNEL = AudioFormat.CHANNEL_IN_STEREO
        private const val AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT //16位采样精度
        private const val SAMPLERATE = 16000 //采样频率
    }

    fun setAudioDataCallBack(audioDataCallBack: AudioDataCallBack){
        this.audioDataCallBack = audioDataCallBack
    }
    interface AudioCallBack{
        fun onAudioVolume(volume: Int)
    }
    interface AudioDataCallBack{
        fun onAudioData(byteArray: ByteArray)
    }
}