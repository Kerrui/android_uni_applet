package com.applet.mqtt;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

   @Override
   public void onNewToken(@NonNull String token) {
      super.onNewToken(token);
   }
}
