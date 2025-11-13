package com.example.interprep.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun playUrl(url: String, onComplete: () -> Unit = {}) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            setOnPreparedListener { it.start() }
            setOnCompletionListener {
                onComplete()
                stop()
            }
            setOnErrorListener { _, what, extra ->
                Log.e("AudioPlayer", "Error playing audio: $what / $extra")
                stop()
                true
            }
            prepareAsync()
        }
    }

    fun stop() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) { }
        mediaPlayer = null
    }
}
