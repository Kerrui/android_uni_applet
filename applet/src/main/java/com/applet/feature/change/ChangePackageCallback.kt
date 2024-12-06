package com.applet.feature.change;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

open abstract class ChangePackageCallback(var password:String) : okhttp3.Callback{

    var attempt = 0
    val maxAttempts = 10


}
