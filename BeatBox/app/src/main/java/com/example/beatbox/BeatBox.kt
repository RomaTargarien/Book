package com.example.beatbox

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.SoundPool
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.lang.Exception

private const val TAG = "BeatBox"
private const val SOUNDS_FOLDER = "sample_sounds"
private const val MAX_STREAMS = 5

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BeatBox(private val assets: AssetManager){

    val sounds: List<Sound>
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val soundPool = SoundPool.Builder().setMaxStreams(5).build()

    init {
        sounds = loadSounds()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>
        try {
            soundNames = assets.list(SOUNDS_FOLDER)!!
        } catch (e: Exception){
            Log.d(TAG,"Cant do it ${e.printStackTrace()}")
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames.forEach {filename->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            try {
                load(sound)
                sounds.add(sound)
            }
            catch (e: IOException){
                Log.e(TAG, "Cound not load sound $filename", e)
            }
        }
        return sounds
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun load(sound: Sound){
        val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
        val soundId = soundPool.load(afd,1)
        sound.soundId = soundId
    }
    fun play(sound: Sound){
        sound.soundId?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }
    fun release(){
        soundPool.release()
    }
}