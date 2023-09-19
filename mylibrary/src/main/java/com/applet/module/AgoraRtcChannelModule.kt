package com.applet.module;

import com.alibaba.fastjson.JSONObject
import com.applet.agora.RtcChannelManager
import com.applet.agora.RtcEngineEventHandler
import com.applet.agora.RtcEngineManager
import com.applet.agora.UniCallback
import io.agora.rtc2.RtcEngine
import io.dcloud.feature.uniapp.annotation.UniJSMethod
import io.dcloud.feature.uniapp.bridge.UniJSCallback
import io.dcloud.feature.uniapp.common.UniModule

class AgoraRtcChannelModule : UniModule() {
    companion object {
        var manager: RtcChannelManager? = null
            private set
    }

    private val manager = RtcChannelManager { methodName, data -> emit(methodName, data) }

    init {
        AgoraRtcChannelModule.manager = manager
    }

    private fun emit(methodName: String, data: Map<String, Any?>?) {
        mUniSDKInstance.fireGlobalEventCallback("${RtcEngineEventHandler.PREFIX}$methodName", data)
    }

    @UniJSMethod(uiThread = false)
    fun callMethod(params: JSONObject?, callback: UniJSCallback?) {
        params?.getString("method")?.let { methodName ->
            manager.javaClass.declaredMethods.find { it.name == methodName }?.let { function ->
                function.let { method ->
                    try {
                        val parameters = mutableListOf<Any?>()
                        params.getJSONObject("args")?.toMap()?.toMutableMap()?.let {
                            if (methodName == "create") {
                                it["engine"] = RtcEngineManager.engine
                            }
                            parameters.add(it)
                        }
                        method.invoke(manager, *parameters.toTypedArray(), UniCallback(callback))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
