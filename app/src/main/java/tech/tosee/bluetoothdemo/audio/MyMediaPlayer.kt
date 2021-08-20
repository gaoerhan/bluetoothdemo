package tech.tosee.bluetoothdemo.audio

import android.content.Context
import android.media.MediaPlayer
import tech.tosee.bluetoothdemo.R

/**
 * @author: geh
 * @version: 1.0.1
 * @date: 2021/7/19
 */
class MyMediaPlayer {

    lateinit var mMediaPlayer: MediaPlayer
    var isPrepare  = false
    fun initMedia(context: Context) {
        mMediaPlayer = MediaPlayer.create(context, R.raw.called)
//        mMediaPlayer = MediaPlayer()
//        mMediaPlayer.setDataSource(Environment.getExternalStorageDirectory().path + "/music/ceshi.wav")
        mMediaPlayer.isLooping = true
//        mMediaPlayer.prepareAsync()
//        mMediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
//            override fun onPrepared(mediaPlayer: MediaPlayer) {
//                isPrepare = true
//            }
//        })

    }

    fun getCurrentProgress():Int{

        return mMediaPlayer.currentPosition
    }

    fun getMaxProgress(): Int{
        return mMediaPlayer.duration
    }

    fun isPlaying():Boolean{
        return mMediaPlayer.isPlaying
    }
    fun playMedia() {
        mMediaPlayer.start()
    }


    fun stopMedia() {
        mMediaPlayer.pause()
    }

    fun releaseMedia(){
        mMediaPlayer.stop()
        isPrepare = false
        mMediaPlayer.release()
    }
}