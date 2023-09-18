package com.applet.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import java.io.IOException;

public class RingingUtil {

   private MediaPlayer mMediaPlayer;
   private Vibrator mVibrator;
   private boolean isPlayer = false;

   private RingingUtil() {
   }

   public static RingingUtil getInstance() {
      return RingingUtilHolder.INSTANCE;
   }

   private static class RingingUtilHolder {
      private static final RingingUtil INSTANCE = new RingingUtil();
   }

   public void startRinging(Context mContext) {
      if (isPlayer) {
         return;
      }
      isPlayer = true;
      @SuppressLint("WrongConstant") AudioManager audio = (AudioManager) mContext.getSystemService("audio");
      assert audio != null;
      int ringerMode = audio.getRingerMode();
      if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
         if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
            mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(new long[]{500, 1000}, 0);
         }
         Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
         mMediaPlayer = new MediaPlayer();
         try {
            mMediaPlayer.setDataSource(mContext, uri);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public void stopRinging() {
      isPlayer = false;
      if (mMediaPlayer != null) {
         mMediaPlayer.stop();
         mMediaPlayer.release();
         mMediaPlayer = null;
      }
      if (mVibrator != null) {
         mVibrator.cancel();
         mVibrator = null;
      }
   }
}
