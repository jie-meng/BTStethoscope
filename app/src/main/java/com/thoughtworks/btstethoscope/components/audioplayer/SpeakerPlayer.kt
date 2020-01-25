package com.thoughtworks.btstethoscope.components.audioplayer

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import com.github.piasy.rxandroidaudio.StreamAudioRecorder
import com.thoughtworks.btstethoscope.components.audiorecorder.AudioCache
import com.thoughtworks.btstethoscope.definitions.SAMPLE_RATE_IN_HERTZ
import com.thoughtworks.btstethoscope.utils.StreamAudioPlayerEx


class SpeakerPlayer(
    private val context: Context,
    private val audioCache: AudioCache
) : AudioPlayer {

//    private val streamAudioPlayer = StreamAudioPlayerEx.getInstance()

    override fun play() {
//        streamAudioPlayer.init()

        val audioPlayer = StreamAudioPlayerEx.getInstance()
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_IN_HERTZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioPlayer.init(
            false,
            SAMPLE_RATE_IN_HERTZ,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 10
        )
        val audioBytes = audioCache.asByteArray()
        audioPlayer.play(audioBytes, audioBytes.size)

        try {
//            val audioTrack = AudioTrack(
//                AudioManager.STREAM_MUSIC,
//                SAMPLE_RATE_IN_HERTZ,
//                AudioFormat.CHANNEL_OUT_MONO,
//                AudioFormat.ENCODING_PCM_16BIT,
//                audioCache.getSize(),  //buffer length in bytes
//                AudioTrack.MODE_STATIC
//            )
//            audioTrack.write(audioCache.asByteArray(), 0, audioCache.getSize())
//            audioTrack.play()

//            var mp = MediaPlayer()
//            mp.setDataSource(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
//            mp.setAudioStreamType(AudioManager.STREAM_MUSIC)
//            mp.prepare()
//            mp.start()

//            val inputStream = FileInputStream(Environment.getExternalStorageDirectory().absolutePath + File.separator + "record.mp3")
//
//            val buffer = ByteArray(StreamAudioRecorder.DEFAULT_BUFFER_SIZE)
//            var read = inputStream.read(buffer)
//            while (read > 0) {
//                streamAudioPlayer.play(buffer, read)
//                inputStream.read(buffer)
//            }
//            inputStream.close()
//            streamAudioPlayer.release()
        } catch (e: Exception) {
            print(e)
        }
    }
}