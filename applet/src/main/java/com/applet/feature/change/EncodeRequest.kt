package com.applet.feature.change

import androidx.annotation.Keep
import okhttp3.Request

@Keep
class EncodeRequest(val request: Request, val password: String)