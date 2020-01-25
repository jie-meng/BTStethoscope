package com.thoughtworks.btstethoscope.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.github.piasy.rxandroidaudio.StreamAudioRecorder;

import static com.thoughtworks.btstethoscope.definitions.ConstantsKt.SAMPLE_RATE_IN_HERTZ;

public final class StreamAudioPlayerEx {
    private static final String TAG = "StreamAudioPlayerEx";

    private AudioTrack mAudioTrack;

    private StreamAudioPlayerEx() {
        // singleton
    }

    private static final class StreamAudioPlayerHolder {
        private static final StreamAudioPlayerEx INSTANCE = new StreamAudioPlayerEx();
    }

    public static StreamAudioPlayerEx getInstance() {
        return StreamAudioPlayerHolder.INSTANCE;
    }

    public synchronized void init() {
        init(false, SAMPLE_RATE_IN_HERTZ, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                StreamAudioRecorder.DEFAULT_BUFFER_SIZE);
    }

    /**
     * AudioFormat.CHANNEL_OUT_MONO
     * AudioFormat.ENCODING_PCM_16BIT
     *
     * @param bufferSize user may want to write data larger than minBufferSize, so they should able
     *                   to increase it
     */
    public synchronized void init(boolean bluetoothStream, int sampleRate, int channelConfig, int audioFormat,
                                  int bufferSize) {
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        mAudioTrack =
                new AudioTrack(bluetoothStream ? 6 : AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat,
                        Math.max(minBufferSize, bufferSize), AudioTrack.MODE_STREAM);
        mAudioTrack.play();
    }

    @WorkerThread
    public synchronized boolean play(byte[] data, int size) {
        if (mAudioTrack != null) {
            try {
                int ret = mAudioTrack.write(data, 0, size);
                switch (ret) {
                    case AudioTrack.ERROR_INVALID_OPERATION:
                        Log.w(TAG, "play fail: ERROR_INVALID_OPERATION");
                        return false;
                    case AudioTrack.ERROR_BAD_VALUE:
                        Log.w(TAG, "play fail: ERROR_BAD_VALUE");
                        return false;
                    case AudioManager.ERROR_DEAD_OBJECT:
                        Log.w(TAG, "play fail: ERROR_DEAD_OBJECT");
                        return false;
                    default:
                        return true;
                }
            } catch (IllegalStateException e) {
                Log.w(TAG, "play fail: " + e.getMessage());
                return false;
            }
        }
        Log.w(TAG, "play fail: null mAudioTrack");
        return false;
    }

    public synchronized void release() {
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
}
