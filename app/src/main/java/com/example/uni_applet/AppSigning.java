package com.example.uni_applet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by Genda on 2023/9/8.
 */
public class AppSigning {

   public final static String MD5 = "MD5";
   public final static String SHA1 = "SHA1";
   public final static String SHA256 = "SHA256";

   private static final String TAG = "AppSigning";

   public static String getSignInfo(Context context, String type) {
      if (context == null || type == null) {
         return null;
      }
      String packageName = context.getPackageName();
      if (packageName == null) {
         return null;
      }
      String tmp = "error!";
      try {
         Signature[] signs = getSignatures(context, packageName);
         for (Signature sig : signs) {

            if (MD5.equals(type)) {
               tmp = getSignatureByteString(sig, MD5);
            } else if (SHA1.equals(type)) {
               tmp = getSignatureByteString(sig, SHA1);
            } else if (SHA256.equals(type)) {
               tmp = getSignatureByteString(sig, SHA256);
            }
         }
      } catch (Exception e) {
         Log.e(TAG, e.toString());
      }
      return tmp;
   }

   public static String getSha1(Context context) {
      return getSignInfo(context, SHA1);
   }

   public static String getMD5(Context context) {
      return getSignInfo(context, MD5);
   }

   public static String getSHA256(Context context) {
      return getSignInfo(context, SHA256);
   }

   private static Signature[] getSignatures(Context context, String packageName) {
      PackageInfo packageInfo = null;
      try {
         packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
         return packageInfo.signatures;
      } catch (Exception e) {
         Log.e(TAG, e.toString());
      }
      return null;
   }

   private static String getSignatureString(Signature sig, String type) {
      byte[] hexBytes = sig.toByteArray();
      String fingerprint = "error!";
      try {
         MessageDigest digest = MessageDigest.getInstance(type);
         if (digest != null) {
            byte[] digestBytes = digest.digest(hexBytes);
            StringBuilder sb = new StringBuilder();
            for (byte digestByte : digestBytes) {
               sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
            }
            fingerprint = sb.toString();
         }
      } catch (Exception e) {
         Log.e(TAG, e.toString());
      }

      return fingerprint;
   }

   private static String getSignatureByteString(Signature sig, String type) {
      byte[] hexBytes = sig.toByteArray();
      String fingerprint = "error!";
      try {
         MessageDigest digest = MessageDigest.getInstance(type);
         if (digest != null) {
            byte[] digestBytes = digest.digest(hexBytes);
            StringBuilder sb = new StringBuilder();
            for (byte digestByte : digestBytes) {
               sb.append(((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3)).toUpperCase());
               sb.append(":");
            }
            fingerprint = sb.substring(0, sb.length() - 1).toString();
         }
      } catch (Exception e) {
         Log.e(TAG, e.toString());
      }

      return fingerprint;
   }
}

