package tech.tosee.bluetoothdemo.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Process
import android.util.Log
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.ReentrantLock


/*
 * @author: geh
 * @version: V100R001C01
 * @date: 2021/4/17
 */
class MyAudioTrack() {

    var buffersize: Int = 640
    private var mAudioTrack: AudioTrack? = null
    private val reentrantLock:ReentrantLock = ReentrantLock()
    private val condition = reentrantLock.newCondition()
    @Volatile
    private var mStatus = false
    private val recordingBufferlist = LinkedBlockingQueue<ByteArray>(50)

    fun initTrack() {
//        mBufferSizeInBytes = AudioTrack.getMinBufferSize(
//            SAMPLERATE,
//            CHANNEL,
//            AUDIOFORMAT
//        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                            AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .setLegacyStreamType(AudioManager.STREAM_VOICE_CALL)
                                    .build()
                    )
                    .setAudioFormat(
                            AudioFormat.Builder()
                                    .setEncoding(AUDIOFORMAT)
                                    .setSampleRate(SAMPLERATE)
                                    .setChannelMask(CHANNEL)
                                    .build()
                    )
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(buffersize * 2)
                    .build()
        } else {
            mAudioTrack = AudioTrack(
                    AudioManager.STREAM_VOICE_CALL,
                    SAMPLERATE,
                    CHANNEL,
                    AUDIOFORMAT,
                    buffersize * 2,
                    AudioTrack.MODE_STREAM
            )
        }

    }

    fun addAudioData(data: ByteArray?) {
        if (data != null) {

            if (recordingBufferlist.offer(data)) {
            } else {
                recordingBufferlist.clear()
                recordingBufferlist.offer(data)
            }

            reentrantLock.lock()
            condition.signal()
            reentrantLock.unlock()
        }

    }


    private fun getRecordBuf(): ByteArray? {
        return recordingBufferlist.poll()
    }


    //播放本地文件
    fun startPlay() {
        mStatus = true
        Thread({
            try {
                Process.setThreadPriority(
                        Process.THREAD_PRIORITY_URGENT_AUDIO
                )
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "Set PlayAudioThread thread priority failed: ${e.message}")
                e.printStackTrace()
            }
            try {
                mAudioTrack?.play()
            } catch (e: java.lang.Exception) {
                Log.e(TAG, "Set PlayAudioThread thread priority failed: ${e.message}")
                e.printStackTrace()
            }
            while (mStatus) {
                try {
                    val data: ByteArray? = getRecordBuf()
                    if (null != data) {
                        mAudioTrack?.write(data, 0, data.size)
                    }
                    reentrantLock.lock()
                    condition.await()

                } catch (e: java.lang.Exception) {
                    break
                }finally {
                    reentrantLock.unlock()
                }
            }
        }).start()

    }


    @Throws(java.lang.IllegalStateException::class)
    fun stopPlay() {
        if(!mStatus)return
        mStatus = false
        mAudioTrack?.stop()
        release()
    }

    fun release() {
        if (mAudioTrack != null) {
            mAudioTrack?.release()
            mAudioTrack = null
        }
        recordingBufferlist.clear()
    }


    companion object {
        private const val TAG = "MyAudioTrack"
        private const val CHANNEL = AudioFormat.CHANNEL_OUT_STEREO
        private const val AUDIOFORMAT = AudioFormat.ENCODING_PCM_16BIT //16位采样精度
        private const val SAMPLERATE = 16000 //采样频率
    }
}