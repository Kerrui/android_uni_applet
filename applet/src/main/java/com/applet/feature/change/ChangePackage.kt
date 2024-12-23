package com.applet.feature.change

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.applet.feature.AppletManager
import com.applet.feature.LibConstant
import com.applet.feature.UniManager
import com.applet.feature.bean.WgtInfo
import com.hi.chat.uniplugin.feature.util.AES256
import com.hi.chat.uniplugin.feature.util.DownloadUtil
import com.hi.chat.uniplugin.feature.util.LogUtil
import com.hi.chat.uniplugin.feature.util.MD5
import com.hi.chat.uniplugin.feature.util.Util
import com.hi.chat.uniplugin.net.HttpClient
import com.hi.chat.uniplugin_log.mmkv.MMKVUtil
import com.hi.chat.uniplugin_tool.ToolUtil
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.net.URLEncoder
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

object ChangePackage {


    private const val TAG = "ChangePackage"
    private const val isDev = true

    val host = if (isDev) {
        "https://doghkxnbdedct.cloudfront.net/mini/index"
    } else {
        ""
    }

    private val uaKey: String = if (isDev) {
        val k0 = "ChdXhYqTONtHZSPWdVMdJYgDFeD".subSequence(4, 15).toString()
        val k1 = "NfapEwMEO8yWI0gbiOqYqc9Dnvc".subSequence(6, 23).toString()
        val k2 = "NdigFDSUfvLKLHdrtaDdJuALKSf".subSequence(7, 11).toString()
        val uKey = k0 + k1 + k2
        MD5.encrypt(uKey, true)
    } else {
        val k0 = "9JLfp3PT8HuxvzLOfjga045afdGF".subSequence(4, 15).toString()
        val k1 = "Okh8TdgWk74BWWi5ar2epWKo9Hflc,Fs".subSequence(6, 23).toString()
        val k2 = "9Jfl7Fdvcwr8Fadjg0dueFgds".subSequence(7, 11).toString()
        val uKey = k0 + k1 + k2
        MD5.encrypt(uKey, true)
    }

    private val dataKey: String = if (isDev) {
        val k0 = "VhFXyYblUYTyMOdMHgdD".subSequence(4, 15).toString()
        val k1 = "NfapEwZaXkoMB62muoeI/smc9Dnvc".subSequence(6, 23).toString()
        val k2 = "NdigFDSvBycUfvLKLHdrtaLKSf".subSequence(7, 11).toString()
        k0 + k1 + k2
    } else {
        val k0 = "LgIKgjwz/4fkpTJIf9kgax7d".subSequence(4, 15).toString()
        val k1 = "Lf8S46QD0bB5G9ffVol0OrECK".subSequence(6, 23).toString()
        val k2 = "O9Fsd&u52bA)faGudILf89FD".subSequence(7, 11).toString()
        k0 + k1 + k2
    }

    private const val sin_key = "1b28c2eac0672761"


    private fun setDefaultParamAndSign(context: Context, json: JSONObject): String {
        val times = System.currentTimeMillis()
        val d = generateD(context, times)
        val nonce = MD5.encrypt(UUID.randomUUID().toString() + times, true)
        json["times"] = times
        json["d"] = d
        json["nonce"] = nonce
        json.remove("sign")

        val query = StringBuffer()
        val toMap = json.toMap()
        val toSortedMap = toMap.toSortedMap()

        for (entry in toSortedMap) {
            val key = entry.key
            val value = entry.value.toString()
            query.append("$key=${URLEncoder.encode(value, "UTF-8")}&")
        }
        val part1 = query.toString().subSequence(0, query.length - 1).toString()
        val p1 = MD5.encrypt(part1, true);
        val sign = MD5.encrypt("$p1$sin_key", true);
        json["sign"] = sign

        return json.toJSONString();
    }


    private fun generateHeader(context: Context): String {
        val sdk = Build.VERSION.SDK_INT.toString()
        val version = LibConstant.SDK_VERSION
        val deviceID = ToolUtil.getDeviceId(context)
        val packageName = context.packageName
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val width = windowManager.defaultDisplay.width
        val height = windowManager.defaultDisplay.height
        val model = Build.MODEL
        val market = "GOOGLE PLAY"
        val language = Locale.getDefault().language
        val appVersion = Util.getVersionName(context)
        val device = "android"
        val zipVersion = "1"
        val zone = TimeZone.getDefault().id
        val u =
            "i|18.0.1|1.2.8|1|E0A39911-5C52-4217-BE8E-C088DF4B01F2|Asia/Shanghai|com.imstreamer.home|iOS|zh-Hans-CN,zh-Hans-CN|430|932|Apple|iPhone 14 Pro Max|1.0.4"
//        val u =
//            "i|${sdk}|${version}|${zipVersion}|${deviceID}|${zone}|${packageName}|${market}|${language}|${width}|${height}|${device}|${model}|${appVersion}"
        Log.i(TAG, u);
        return AES256.encrypt(uaKey, u);
    }

    private fun generateD(context: Context, timestamp: Long): String {

        val packageName = "com.imstreamer.home"
        val deviceId = "E0A39911-5C52-4217-BE8E-C088DF4B01F2"

        val d1 = Util.getRandomStringArray(10).lowercase()
        val d2MD5 = MD5.encrypt("$packageName$deviceId", true)
        val d2 = d2MD5.subSequence(d2MD5.length - 8, d2MD5.length)
        val d3 = Util.getRandomStringArray(((timestamp % 10) + 1).toInt()).lowercase()
        val d4 = timestamp.toString()
        return "$d1$d2$d3,$d4"
    }


    fun changePackageRequest(context: Context, appid: String?, contentStatus: Int) {
        val jsonObject = JSONObject()
        if (appid?.isNotEmpty() == true) {
            jsonObject["appid"] = appid
        }

        jsonObject["current_status"] = contentStatus
        val encodeRequest = getEncodeRequest(context, jsonObject)
        val client = HttpClient.getClient();

        client.newCall(encodeRequest.request)
            .enqueue(object : ChangePackageCallback(encodeRequest.password) {

                override fun onFailure(call: Call, e: IOException) {
                    if (attempt < maxAttempts) {
                        attempt++
                        val encodeRequest = getEncodeRequest(context, jsonObject)
                        val request = encodeRequest.request
                        this.password = encodeRequest.password
                        client.newCall(request).enqueue(this);
                    }

                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()
                    val string = body?.string()
                    val res = AES256.decrypt(this.password, string)
                    val responseJSONObject = JSONObject.parseObject(res)
                    val status = responseJSONObject.getIntValue("ok")
                    if (status == 1) {
                        handleChangePackage(context, jsonObject, responseJSONObject)
                    } else if (attempt < maxAttempts) {
                        Thread.sleep(2000)
                        attempt++
                        val encodeRequest = getEncodeRequest(context, jsonObject)
                        this.password = encodeRequest.password
                        client.newCall(encodeRequest.request).enqueue(this);
                    }
                }
            })
    }


    private fun handleChangePackage(context: Context, request: JSONObject, response: JSONObject) {
        val data = response.getJSONObject("data")
        val page = data.getJSONObject("page")
        val p1 = page.getJSONObject("p1")
        val p2 = page.getJSONObject("p2")


        val wgt = WgtInfo().apply {
            this.appid = "__UNI__5799B28"
            this.url = "https://img.nbcustomchat.com/wgt/__UNI__5799B28_info_1.wgt"
        }

        val downFilePath = context.getExternalFilesDir(null)!!.absolutePath
        val fileName: String = wgt.appid + ".wgt"

        DownloadUtil.getInstance()
            .download(wgt.url, downFilePath, fileName, object : DownloadUtil.OnDownloadListener {
                override fun onSuccess(file: File?) {

                    UniManager.releaseWgtToRunPath(
                        file!!.path,
                        wgt.appid,
                        object : UniManager.IOnWgtReleaseListener {
                            override fun onSuccess() {
                                LogUtil.t("handleResponse applet: release success")
                                MMKVUtil.getInstance()
                                    .put(LibConstant.SP_WGT_APPLET, JSON.toJSONString(wgt))
//                            mAppletInfo = appletInfo
                                Handler(Looper.getMainLooper()).post {
                                    AppletManager.openUniMP(
                                        context,
                                        wgt.appid
                                    )
                                }

                            }

                            override fun onFailed(message: String) {
                                LogUtil.t("handleResponse applet: release failed $message")
//                                changePackageRequest(
//                                    context,
//                                    request.getString("appid"),
//                                    request.getIntValue("current_status")
//                                )
                            }
                        })

                }

                override fun onLoading(progress: Int) {
                }

                override fun onFailed(message: String?) {
                    println("--->${message}")
                    changePackageRequest(
                        context,
                        request.getString("appid"),
                        request.getIntValue("current_status")
                    )
                }

            })

    }

    private fun getEncodeRequest(context: Context, jsonObject: JSONObject): EncodeRequest {
        val ua = generateHeader(context)
        val jsonObjectString = setDefaultParamAndSign(context, jsonObject)
        println("jsonObjectString-->$jsonObjectString");
        val kymd51 = "${ua.subSequence(10, 30)}$dataKey"
        val kymd52 = MD5.encrypt(kymd51, true)
        val body = AES256.encrypt(kymd52, jsonObjectString)

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), body)
        val request =
            Request.Builder().url(host).addHeader("ua", ua).post(requestBody).build()
        return EncodeRequest(request, kymd52)
    }
}
