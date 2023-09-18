package com.applet.audio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.applet.feature.LibApp;
import com.example.mylibrary.R;


public class AudioPlayManager {

    private static final String TAG = "AudioPlayManager";

    private static class AudioPlayManagerHolder {
        @SuppressLint("StaticFieldLeak")
        private static final AudioPlayManager sInstance = new AudioPlayManager();
    }

    public static AudioPlayManager getInstance() {
        return AudioPlayManagerHolder.sInstance;
    }

    private Context mContext;
    private MediaPlayer mSoundMediaPlayer;
    private MediaPlayer mRingMediaPlayer;
    private Vibrator mVibrator;
    private AudioManager mAudioManager;

    private HeadsetReceiver mHeadsetReceiver;
    private OnSoundMediaPlayerListener mOnSoundMediaPlayerListener;
    private OnRingMediaPlayerListener mOnRingMediaPlayerListener;

    //WeakReference<List<MediaPlayer>> k;
    //HashMap<Integer, MediaPlayer> po;

    private AudioPlayManager() {
        mContext = LibApp.getContext();

        initSoundMediaPlayer();
        initRingMediaPlayer();
        initAudioManager();

        mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
    }

    private void initAudioManager() {
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
    }

    private void initSoundMediaPlayer() {
        mSoundMediaPlayer = new MediaPlayer();
        mSoundMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION); // 通知
        mSoundMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mSoundMediaPlayer != null) {
                    mSoundMediaPlayer.start();
                }
            }
        });
        mSoundMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mOnSoundMediaPlayerListener != null) {
                    mOnSoundMediaPlayerListener.onError("what = " + what + " extra = " + extra);
                }
                return false;
            }
        });
    }

    private void initRingMediaPlayer() {
        mRingMediaPlayer = new MediaPlayer();
        mRingMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        mRingMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mRingMediaPlayer != null) {
                    mRingMediaPlayer.start();
                }
            }
        });
        mRingMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mOnRingMediaPlayerListener != null) {
                    mOnRingMediaPlayerListener.onError("what = " + what + " extra = " + extra);
                }
                return false;
            }
        });
    }

    public void playSound(String path, final OnSoundMediaPlayerListener soundMediaPlayerListener) {
        if (mSoundMediaPlayer == null) return;

        boolean isPlaying = mSoundMediaPlayer.isPlaying();
        if (isPlaying) return;

        if (soundMediaPlayerListener != null) {
            this.mOnSoundMediaPlayerListener = soundMediaPlayerListener;
        }
        try {
            String uriString;
            if (TextUtils.isEmpty(path)) {
                uriString = "android.resource://" + mContext.getPackageName() + "/" + R.raw.audio_msg_sound;
            } else {
                uriString = path;
            }

            mSoundMediaPlayer.reset();
            mSoundMediaPlayer.setLooping(false);
            mSoundMediaPlayer.setDataSource(mContext, Uri.parse(uriString));
            mSoundMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (soundMediaPlayerListener != null) {
                soundMediaPlayerListener.onError(e.getMessage());
            }
        }
    }

    public void ringPlay(String path, boolean isCall, OnRingMediaPlayerListener ringMediaPlayerListener) {

        if (mRingMediaPlayer == null) return;

        boolean isPlaying = mRingMediaPlayer.isPlaying();
        if (isPlaying) return;

        if (ringMediaPlayerListener != null) {
            this.mOnRingMediaPlayerListener = ringMediaPlayerListener;
        }

        try {
            int ringMode = mAudioManager.getRingerMode();
            if (ringMode == AudioManager.RINGER_MODE_SILENT) return;

            if (!isCall) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    VibrationEffect vibrationEffect = VibrationEffect.createWaveform(new long[]{500, 800}, 0);
                    mVibrator.vibrate(vibrationEffect);
                } else {
                    mVibrator.vibrate(new long[]{500, 1000}, 1);
                }
            }

            if (ringMediaPlayerListener != null) {
                ringMediaPlayerListener.setControlStream();
            }

            Uri ringUri;
            if (TextUtils.isEmpty(path)) {
                ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            } else {
                ringUri = Uri.parse(path);
            }
            mRingMediaPlayer.reset();
            mRingMediaPlayer.setLooping(true);
            mRingMediaPlayer.setDataSource(mContext, ringUri);
            mRingMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (ringMediaPlayerListener != null) {
                ringMediaPlayerListener.onError(e.getMessage());
            }
        }
    }

    public void ringStop() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingMediaPlayer != null) {
            mRingMediaPlayer.stop();
        }
    }

    public void changeBluetoothMode() {
        if (mAudioManager == null) return;
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.setSpeakerphoneOn(false);
    }

    public void registerHeadsetReceiver(Activity activity) {
        if (mHeadsetReceiver != null) return;
        Log.e(TAG, "registerHeadsetReceiver: '-----> start");
        mHeadsetReceiver = new HeadsetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        activity.registerReceiver(mHeadsetReceiver, filter);
        Log.e(TAG, "registerHeadsetReceiver: '-----> end");
    }

    public void unregisterHeadsetReceiver(Context context) {
        if (mHeadsetReceiver == null) return;
        try {
            context.unregisterReceiver(mHeadsetReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class HeadsetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.e(TAG, "action = " + action);
            if (TextUtils.isEmpty(action)) {
                return;
            }

            switch (action) {
                case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED:
                    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                    @SuppressLint("MissingPermission") int status1 = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
                    if (status1 == BluetoothProfile.STATE_CONNECTED) {
                        Log.e(TAG, "onReceive: '------> 蓝牙耳机已连接");
                        changeBluetoothMode();
                    }
                    break;
                default:
                    return;
            }
        }
    }

    public int getHeadsetStatus(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn()) {
            return 1;
        }

        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            return -1;
        } else if (ba.isEnabled()) {
            @SuppressLint("MissingPermission") int a2dp = ba.getProfileConnectionState(BluetoothProfile.A2DP);
            @SuppressLint("MissingPermission") int headset = ba.getProfileConnectionState(BluetoothProfile.HEADSET);
            @SuppressLint("MissingPermission") int health = ba.getProfileConnectionState(BluetoothProfile.HEALTH);

            int flag = -1;
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
            } else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
            if (flag != -1) {
                return 2;
            }
        }
        return -2;
    }
}
