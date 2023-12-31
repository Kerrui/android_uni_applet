package com.applet.agora

import android.graphics.Rect
import android.util.Log
import androidx.annotation.IntRange
import io.agora.rtc2.ClientRoleOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IMediaExtensionObserver
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.UserInfo

class RtcEngineEvents {
  companion object {
    const val Warning = "Warning"
    const val Error = "Error"
    const val ApiCallExecuted = "ApiCallExecuted"
    const val JoinChannelSuccess = "JoinChannelSuccess"
    const val RejoinChannelSuccess = "RejoinChannelSuccess"
    const val LeaveChannel = "LeaveChannel"
    const val LocalUserRegistered = "LocalUserRegistered"
    const val UserInfoUpdated = "UserInfoUpdated"
    const val ClientRoleChanged = "ClientRoleChanged"
    const val UserJoined = "UserJoined"
    const val UserOffline = "UserOffline"
    const val ConnectionStateChanged = "ConnectionStateChanged"
    const val NetworkTypeChanged = "NetworkTypeChanged"
    const val ConnectionLost = "ConnectionLost"
    const val TokenPrivilegeWillExpire = "TokenPrivilegeWillExpire"
    const val RequestToken = "RequestToken"
    const val AudioVolumeIndication = "AudioVolumeIndication"
    const val ActiveSpeaker = "ActiveSpeaker"
    const val FirstLocalAudioFrame = "FirstLocalAudioFrame"
    const val FirstLocalVideoFrame = "FirstLocalVideoFrame"
    const val UserMuteVideo = "UserMuteVideo"
    const val VideoSizeChanged = "VideoSizeChanged"
    const val RemoteVideoStateChanged = "RemoteVideoStateChanged"
    const val LocalVideoStateChanged = "LocalVideoStateChanged"
    const val RemoteAudioStateChanged = "RemoteAudioStateChanged"
    const val LocalAudioStateChanged = "LocalAudioStateChanged"
    const val RequestAudioFileInfo = "RequestAudioFileInfo"
    const val LocalPublishFallbackToAudioOnly = "LocalPublishFallbackToAudioOnly"
    const val RemoteSubscribeFallbackToAudioOnly = "RemoteSubscribeFallbackToAudioOnly"
    const val AudioRouteChanged = "AudioRouteChanged"
    const val CameraFocusAreaChanged = "CameraFocusAreaChanged"
    const val CameraExposureAreaChanged = "CameraExposureAreaChanged"
    const val FacePositionChanged = "FacePositionChanged"
    const val RtcStats = "RtcStats"
    const val LastmileQuality = "LastmileQuality"
    const val NetworkQuality = "NetworkQuality"
    const val LastmileProbeResult = "LastmileProbeResult"
    const val LocalVideoStats = "LocalVideoStats"
    const val LocalAudioStats = "LocalAudioStats"
    const val RemoteVideoStats = "RemoteVideoStats"
    const val RemoteAudioStats = "RemoteAudioStats"
    const val AudioMixingFinished = "AudioMixingFinished"
    const val AudioMixingStateChanged = "AudioMixingStateChanged"
    const val AudioEffectFinished = "AudioEffectFinished"
    const val RtmpStreamingStateChanged = "RtmpStreamingStateChanged"
    const val TranscodingUpdated = "TranscodingUpdated"
    const val StreamInjectedStatus = "StreamInjectedStatus"
    const val StreamMessage = "StreamMessage"
    const val StreamMessageError = "StreamMessageError"
    const val MediaEngineLoadSuccess = "MediaEngineLoadSuccess"
    const val MediaEngineStartCallSuccess = "MediaEngineStartCallSuccess"
    const val ChannelMediaRelayStateChanged = "ChannelMediaRelayStateChanged"
    const val ChannelMediaRelayEvent = "ChannelMediaRelayEvent"
    const val FirstRemoteVideoFrame = "FirstRemoteVideoFrame"
    const val FirstRemoteAudioFrame = "FirstRemoteAudioFrame"
    const val FirstRemoteAudioDecoded = "FirstRemoteAudioDecoded"
    const val UserMuteAudio = "UserMuteAudio"
    const val StreamPublished = "StreamPublished"
    const val StreamUnpublished = "StreamUnpublished"
    const val RemoteAudioTransportStats = "RemoteAudioTransportStats"
    const val RemoteVideoTransportStats = "RemoteVideoTransportStats"
    const val UserEnableVideo = "UserEnableVideo"
    const val UserEnableLocalVideo = "UserEnableLocalVideo"
    const val FirstRemoteVideoDecoded = "FirstRemoteVideoDecoded"
    const val MicrophoneEnabled = "MicrophoneEnabled"
    const val ConnectionInterrupted = "ConnectionInterrupted"
    const val ConnectionBanned = "ConnectionBanned"
    const val AudioQuality = "AudioQuality"
    const val CameraReady = "CameraReady"
    const val VideoStopped = "VideoStopped"
    const val MetadataReceived = "MetadataReceived"
    const val FirstLocalAudioFramePublished = "FirstLocalAudioFramePublished"
    const val FirstLocalVideoFramePublished = "FirstLocalVideoFramePublished"
    const val AudioPublishStateChanged = "AudioPublishStateChanged"
    const val VideoPublishStateChanged = "VideoPublishStateChanged"
    const val AudioSubscribeStateChanged = "AudioSubscribeStateChanged"
    const val VideoSubscribeStateChanged = "VideoSubscribeStateChanged"
    const val RtmpStreamingEvent = "RtmpStreamingEvent"
    const val UserSuperResolutionEnabled = "UserSuperResolutionEnabled"
    const val UploadLogResult = "UploadLogResult"
    const val VirtualBackgroundSourceEnabled = "VirtualBackgroundSourceEnabled"
    const val SnapshotTaken = "SnapshotTaken"
    const val RecorderStateChanged = "RecorderStateChanged"
    const val RecorderInfoUpdated = "RecorderInfoUpdated"
    const val ProxyConnected = "ProxyConnected"
    const val ContentInspectResult = "ContentInspectResult"
    const val WlAccMessage = "WlAccMessage"
    const val WlAccStats = "WlAccStats"
    const val ClientRoleChangeFailed = "ClientRoleChangeFailed"
    const val LocalVoicePitchInHz = "LocalVoicePitchInHz"
    // 扩展插件监听
    const val ExtensionEvent = "ExtensionEvent"
    const val ExtensionStarted = "ExtensionStarted"
    const val ExtensionStopped = "ExtensionStopped"
    const val ExtensionError = "ExtensionError"

    fun toMap(): Map<String, String> {
      return hashMapOf(
        "Warning" to Warning,
        "Error" to Error,
        "ApiCallExecuted" to ApiCallExecuted,
        "JoinChannelSuccess" to JoinChannelSuccess,
        "RejoinChannelSuccess" to RejoinChannelSuccess,
        "LeaveChannel" to LeaveChannel,
        "LocalUserRegistered" to LocalUserRegistered,
        "UserInfoUpdated" to UserInfoUpdated,
        "ClientRoleChanged" to ClientRoleChanged,
        "UserJoined" to UserJoined,
        "UserOffline" to UserOffline,
        "ConnectionStateChanged" to ConnectionStateChanged,
        "NetworkTypeChanged" to NetworkTypeChanged,
        "ConnectionLost" to ConnectionLost,
        "TokenPrivilegeWillExpire" to TokenPrivilegeWillExpire,
        "RequestToken" to RequestToken,
        "AudioVolumeIndication" to AudioVolumeIndication,
        "ActiveSpeaker" to ActiveSpeaker,
        "FirstLocalAudioFrame" to FirstLocalAudioFrame,
        "FirstLocalVideoFrame" to FirstLocalVideoFrame,
        "UserMuteVideo" to UserMuteVideo,
        "VideoSizeChanged" to VideoSizeChanged,
        "RemoteVideoStateChanged" to RemoteVideoStateChanged,
        "LocalVideoStateChanged" to LocalVideoStateChanged,
        "RemoteAudioStateChanged" to RemoteAudioStateChanged,
        "LocalAudioStateChanged" to LocalAudioStateChanged,
        "RequestAudioFileInfo" to RequestAudioFileInfo,
        "LocalPublishFallbackToAudioOnly" to LocalPublishFallbackToAudioOnly,
        "RemoteSubscribeFallbackToAudioOnly" to RemoteSubscribeFallbackToAudioOnly,
        "AudioRouteChanged" to AudioRouteChanged,
        "CameraFocusAreaChanged" to CameraFocusAreaChanged,
        "CameraExposureAreaChanged" to CameraExposureAreaChanged,
        "FacePositionChanged" to FacePositionChanged,
        "RtcStats" to RtcStats,
        "LastmileQuality" to LastmileQuality,
        "NetworkQuality" to NetworkQuality,
        "LastmileProbeResult" to LastmileProbeResult,
        "LocalVideoStats" to LocalVideoStats,
        "LocalAudioStats" to LocalAudioStats,
        "RemoteVideoStats" to RemoteVideoStats,
        "RemoteAudioStats" to RemoteAudioStats,
        "AudioMixingFinished" to AudioMixingFinished,
        "AudioMixingStateChanged" to AudioMixingStateChanged,
        "AudioEffectFinished" to AudioEffectFinished,
        "RtmpStreamingStateChanged" to RtmpStreamingStateChanged,
        "TranscodingUpdated" to TranscodingUpdated,
        "StreamInjectedStatus" to StreamInjectedStatus,
        "StreamMessage" to StreamMessage,
        "StreamMessageError" to StreamMessageError,
        "MediaEngineLoadSuccess" to MediaEngineLoadSuccess,
        "MediaEngineStartCallSuccess" to MediaEngineStartCallSuccess,
        "ChannelMediaRelayStateChanged" to ChannelMediaRelayStateChanged,
        "ChannelMediaRelayEvent" to ChannelMediaRelayEvent,
        "FirstRemoteVideoFrame" to FirstRemoteVideoFrame,
        "FirstRemoteAudioFrame" to FirstRemoteAudioFrame,
        "FirstRemoteAudioDecoded" to FirstRemoteAudioDecoded,
        "UserMuteAudio" to UserMuteAudio,
        "StreamPublished" to StreamPublished,
        "StreamUnpublished" to StreamUnpublished,
        "RemoteAudioTransportStats" to RemoteAudioTransportStats,
        "RemoteVideoTransportStats" to RemoteVideoTransportStats,
        "UserEnableVideo" to UserEnableVideo,
        "UserEnableLocalVideo" to UserEnableLocalVideo,
        "FirstRemoteVideoDecoded" to FirstRemoteVideoDecoded,
        "MicrophoneEnabled" to MicrophoneEnabled,
        "ConnectionInterrupted" to ConnectionInterrupted,
        "ConnectionBanned" to ConnectionBanned,
        "AudioQuality" to AudioQuality,
        "CameraReady" to CameraReady,
        "VideoStopped" to VideoStopped,
        "MetadataReceived" to MetadataReceived,
        "FirstLocalAudioFramePublished" to FirstLocalAudioFramePublished,
        "FirstLocalVideoFramePublished" to FirstLocalVideoFramePublished,
        "AudioPublishStateChanged" to AudioPublishStateChanged,
        "VideoPublishStateChanged" to VideoPublishStateChanged,
        "AudioSubscribeStateChanged" to AudioSubscribeStateChanged,
        "VideoSubscribeStateChanged" to VideoSubscribeStateChanged,
        "RtmpStreamingEvent" to RtmpStreamingEvent,
        "UserSuperResolutionEnabled" to UserSuperResolutionEnabled,
        "UploadLogResult" to UploadLogResult,
        "VirtualBackgroundSourceEnabled" to VirtualBackgroundSourceEnabled,
        "SnapshotTaken" to SnapshotTaken,
        "RecorderStateChanged" to RecorderStateChanged,
        "RecorderInfoUpdated" to RecorderInfoUpdated,
        "ProxyConnected" to ProxyConnected,
        "ContentInspectResult" to ContentInspectResult,
        "WlAccMessage" to WlAccMessage,
        "WlAccStats" to WlAccStats,
        "ClientRoleChangeFailed" to ClientRoleChangeFailed,
        "ExtensionEvent" to ExtensionEvent,
        "ExtensionStarted" to ExtensionStarted,
        "ExtensionStopped" to ExtensionStopped,
        "ExtensionError" to ExtensionError
      )
    }
  }
}

class RtcExtensionEventHandler(
  private val emitter: (methodName: String, data: Map<String, Any?>?) -> Unit
): IMediaExtensionObserver {
  companion object {
    const val PREFIX = "io.agora.rtc."
  }

  private fun callback(methodName: String, vararg data: Any?) {
    emitter(methodName, hashMapOf("data" to data.toList()))
  }

  override fun onEvent(provider: String?, extension: String?, key: String?, value: String?) {
    callback(RtcEngineEvents.ExtensionEvent, provider, extension, key, value)
  }

  override fun onStarted(provider: String?, extension: String?) {
    callback(RtcEngineEvents.ExtensionStarted, provider, extension)
  }

  override fun onStopped(provider: String?, extension: String?) {
    callback(RtcEngineEvents.ExtensionStopped, provider, extension)
  }

  override fun onError(provider: String?, extension: String?, error: Int, message: String?) {
    callback(RtcEngineEvents.ExtensionError, provider, extension, error, message)
  }
}

class RtcEngineEventHandler(
  private val emitter: (methodName: String, data: Map<String, Any?>?) -> Unit
) : IRtcEngineEventHandler(){
  companion object {
    const val PREFIX = "io.agora.rtc."
  }

  private fun callback(methodName: String, vararg data: Any?) {
    emitter(methodName, hashMapOf("data" to data.toList()))
  }

//  override fun onWarning(@Annotations.AgoraWarningCode warn: Int) {
//    callback(RtcEngineEvents.Warning, warn)
//  }

  override fun onError(@Annotations.AgoraErrorCode err: Int) {
    callback(RtcEngineEvents.Error, err)
  }

//  override fun onApiCallExecuted(
//    @Annotations.AgoraErrorCode error: Int,
//    api: String?,
//    result: String?
//  ) {
//    callback(RtcEngineEvents.ApiCallExecuted, error, api, result)
//  }

  override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
    callback(RtcEngineEvents.JoinChannelSuccess, channel, uid.toUInt().toLong(), elapsed)
  }

  override fun onRejoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
    callback(RtcEngineEvents.RejoinChannelSuccess, channel, uid.toUInt().toLong(), elapsed)
  }

  override fun onLeaveChannel(stats: RtcStats?) {
    callback(RtcEngineEvents.LeaveChannel, stats?.toMap())
  }

  override fun onLocalUserRegistered(uid: Int, userAccount: String?) {
    callback(RtcEngineEvents.LocalUserRegistered, uid.toUInt().toLong(), userAccount)
  }

  override fun onUserInfoUpdated(uid: Int, userInfo: UserInfo?) {
    callback(RtcEngineEvents.UserInfoUpdated, uid.toUInt().toLong(), userInfo?.toMap())
  }

  override fun onClientRoleChanged(oldRole: Int, newRole: Int, newRoleOptions: ClientRoleOptions?) {
    callback(RtcEngineEvents.ClientRoleChanged, oldRole, newRole, newRoleOptions?.audienceLatencyLevel)
  }

//  override fun onClientRoleChanged(
//    @Annotations.AgoraClientRole oldRole: Int,
//    @Annotations.AgoraClientRole newRole: Int
//  ) {
//    callback(RtcEngineEvents.ClientRoleChanged, oldRole, newRole)
//  }

  override fun onUserJoined(uid: Int, elapsed: Int) {
    callback(RtcEngineEvents.UserJoined, uid.toUInt().toLong(), elapsed)
  }

  override fun onUserOffline(uid: Int, reason: Int) {
    callback(RtcEngineEvents.UserOffline, uid.toUInt().toLong(), reason)
  }

  override fun onConnectionStateChanged(
    @Annotations.AgoraConnectionStateType state: Int,
    @Annotations.AgoraConnectionChangedReason reason: Int
  ) {
    callback(RtcEngineEvents.ConnectionStateChanged, state, reason)
  }

  override fun onNetworkTypeChanged(@Annotations.AgoraNetworkType type: Int) {
    callback(RtcEngineEvents.NetworkTypeChanged, type)
  }

  override fun onConnectionLost() {
    callback(RtcEngineEvents.ConnectionLost)
  }

  override fun onTokenPrivilegeWillExpire(token: String?) {
    callback(RtcEngineEvents.TokenPrivilegeWillExpire, token)
  }

  override fun onRequestToken() {
    callback(RtcEngineEvents.RequestToken)
  }

  override fun onAudioVolumeIndication(
    speakers: Array<out AudioVolumeInfo>?,
    @IntRange(from = 0, to = 255) totalVolume: Int
  ) {
    callback(RtcEngineEvents.AudioVolumeIndication, speakers?.toMapList(), totalVolume)
  }

  override fun onActiveSpeaker(uid: Int) {
    callback(RtcEngineEvents.ActiveSpeaker, uid.toUInt().toLong())
  }

  override fun onFirstLocalVideoFrame(source: Constants.VideoSourceType?, width: Int, height: Int, elapsed: Int) {
    callback(RtcEngineEvents.FirstLocalVideoFrame, Constants.VideoSourceType.getValue(source), width, height, elapsed)
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStateChanged"))
  override fun onUserMuteVideo(uid: Int, muted: Boolean) {
    callback(RtcEngineEvents.UserMuteVideo, uid.toUInt().toLong(), muted)
  }

  override fun onVideoSizeChanged(
    source: Constants.VideoSourceType?,
    uid: Int,
    width: Int,
    height: Int,
    @IntRange(from = 0, to = 360) rotation: Int
  ) {
    callback(RtcEngineEvents.VideoSizeChanged, Constants.VideoSourceType.getValue(source), uid.toUInt().toLong(), width, height, rotation)
  }

//  override fun onVideoSizeChanged(
//    uid: Int,
//    width: Int,
//    height: Int,
//    @IntRange(from = 0, to = 360) rotation: Int
//  ) {
//    callback(RtcEngineEvents.VideoSizeChanged, uid.toUInt().toLong(), width, height, rotation)
//  }

  override fun onRemoteVideoStateChanged(
    uid: Int,
    @Annotations.AgoraVideoRemoteState state: Int,
    @Annotations.AgoraVideoRemoteStateReason reason: Int,
    elapsed: Int
  ) {
    callback(RtcEngineEvents.RemoteVideoStateChanged, uid.toUInt().toLong(), state, reason, elapsed)
  }

  override fun onLocalVideoStateChanged(source: Constants.VideoSourceType?, state: Int, error: Int) {
    callback(RtcEngineEvents.LocalVideoStateChanged, Constants.VideoSourceType.getValue(source), state, error)
  }

  override fun onRemoteAudioStateChanged(
    uid: Int,
    @Annotations.AgoraAudioRemoteState state: Int,
    @Annotations.AgoraAudioRemoteStateReason reason: Int,
    elapsed: Int) {
    callback(RtcEngineEvents.RemoteAudioStateChanged, uid.toUInt().toLong(), state, reason, elapsed)
  }

  override fun onLocalAudioStateChanged(
    state: Int,
    error: Int
  ) {
    callback(RtcEngineEvents.LocalAudioStateChanged, state, error)
  }

  override fun onLocalPublishFallbackToAudioOnly(isFallbackOrRecover: Boolean) {
    callback(RtcEngineEvents.LocalPublishFallbackToAudioOnly, isFallbackOrRecover)
  }

  override fun onRemoteSubscribeFallbackToAudioOnly(uid: Int, isFallbackOrRecover: Boolean) {
    callback(
      RtcEngineEvents.RemoteSubscribeFallbackToAudioOnly,
      uid.toUInt().toLong(),
      isFallbackOrRecover
    )
  }

  override fun onAudioRouteChanged(@Annotations.AgoraAudioOutputRouting routing: Int) {
    callback(RtcEngineEvents.AudioRouteChanged, routing)
  }

  override fun onCameraFocusAreaChanged(rect: Rect?) {
    callback(RtcEngineEvents.CameraFocusAreaChanged, rect?.toMap())
  }

  override fun onCameraExposureAreaChanged(rect: Rect?) {
    callback(RtcEngineEvents.CameraExposureAreaChanged, rect?.toMap())
  }

  override fun onFacePositionChanged(
    imageWidth: Int,
    imageHeight: Int,
    faces: Array<out AgoraFacePositionInfo>?
  ) {
    callback(RtcEngineEvents.FacePositionChanged, imageWidth, imageHeight, faces?.toMapList())
  }

  override fun onRtcStats(stats: RtcStats?) {
    callback(RtcEngineEvents.RtcStats, stats?.toMap())
  }

  override fun onLastmileQuality(@Annotations.AgoraNetworkQuality quality: Int) {
    callback(RtcEngineEvents.LastmileQuality, quality)
  }

  override fun onNetworkQuality(
    uid: Int,
    txQuality: Int,
    rxQuality: Int
  ) {
    callback(RtcEngineEvents.NetworkQuality, uid.toUInt().toLong(), txQuality, rxQuality)
  }

  override fun onLastmileProbeResult(result: LastmileProbeResult?) {
    callback(RtcEngineEvents.LastmileProbeResult, result?.toMap())
  }

  @Deprecated("", ReplaceWith("onLocalVideoStats"))
  override fun onLocalVideoStat(sentBitrate: Int, sentFrameRate: Int) {
    // TODO Not in iOS
  }

  override fun onLocalVideoStats(source: Constants.VideoSourceType?, stats: LocalVideoStats?) {
    callback(RtcEngineEvents.LocalVideoStats, Constants.VideoSourceType.getValue(source), stats?.toMap())
  }

  override fun onLocalAudioStats(stats: LocalAudioStats?) {
    callback(RtcEngineEvents.LocalAudioStats, stats?.toMap())
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStats"))
  override fun onRemoteVideoStat(
    uid: Int,
    delay: Int,
    receivedBitrate: Int,
    receivedFrameRate: Int
  ) {
    // TODO Not in iOS
  }

  override fun onRemoteVideoStats(stats: RemoteVideoStats?) {
    callback(RtcEngineEvents.RemoteVideoStats, stats?.toMap())
  }

  override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
    callback(RtcEngineEvents.RemoteAudioStats, stats?.toMap())
  }

  @Deprecated("", ReplaceWith("onAudioMixingStateChanged"))
  override fun onAudioMixingFinished() {
    callback(RtcEngineEvents.AudioMixingFinished)
  }

  override fun onAudioMixingStateChanged(
    @Annotations.AgoraAudioMixingStateCode state: Int,
    @Annotations.AgoraAudioMixingReason reason: Int
  ) {
    callback(RtcEngineEvents.AudioMixingStateChanged, state, reason)
  }

  override fun onAudioEffectFinished(soundId: Int) {
    callback(RtcEngineEvents.AudioEffectFinished, soundId)
  }

  override fun onRtmpStreamingStateChanged(
    url: String?,
    state: Int,
    errCode: Int
  ) {
    callback(RtcEngineEvents.RtmpStreamingStateChanged, url, state, errCode)
  }

  override fun onTranscodingUpdated() {
    callback(RtcEngineEvents.TranscodingUpdated)
  }

  override fun onStreamInjectedStatus(
    url: String?,
    uid: Int,
    @Annotations.AgoraInjectStreamStatus status: Int
  ) {
    callback(RtcEngineEvents.StreamInjectedStatus, url, uid.toUInt().toLong(), status)
  }

  override fun onStreamMessage(uid: Int, streamId: Int, data: ByteArray?) {
    callback(
      RtcEngineEvents.StreamMessage,
      uid.toUInt().toLong(),
      streamId,
      data?.let { String(it, Charsets.UTF_8) })
  }

  override fun onStreamMessageError(
    uid: Int,
    streamId: Int,
    @Annotations.AgoraErrorCode error: Int,
    missed: Int,
    cached: Int
  ) {
    callback(
      RtcEngineEvents.StreamMessageError,
      uid.toUInt().toLong(),
      streamId,
      error,
      missed,
      cached
    )
  }

  override fun onMediaEngineLoadSuccess() {
    callback(RtcEngineEvents.MediaEngineLoadSuccess)
  }

  override fun onMediaEngineStartCallSuccess() {
    callback(RtcEngineEvents.MediaEngineStartCallSuccess)
  }

  override fun onChannelMediaRelayStateChanged(
    @Annotations.AgoraChannelMediaRelayState state: Int,
    @Annotations.AgoraChannelMediaRelayError code: Int
  ) {
    callback(RtcEngineEvents.ChannelMediaRelayStateChanged, state, code)
  }

  override fun onChannelMediaRelayEvent(@Annotations.AgoraChannelMediaRelayEvent code: Int) {
    callback(RtcEngineEvents.ChannelMediaRelayEvent, code)
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStateChanged"))
  override fun onFirstRemoteVideoFrame(uid: Int, width: Int, height: Int, elapsed: Int) {
    callback(RtcEngineEvents.FirstRemoteVideoFrame, uid.toUInt().toLong(), width, height, elapsed)
  }

  @Deprecated("", ReplaceWith("onRemoteAudioStateChanged"))
  override fun onFirstRemoteAudioFrame(uid: Int, elapsed: Int) {
    callback(RtcEngineEvents.FirstRemoteAudioFrame, uid.toUInt().toLong(), elapsed)
  }

  @Deprecated("", ReplaceWith("onRemoteAudioStateChanged"))
  override fun onFirstRemoteAudioDecoded(uid: Int, elapsed: Int) {
    callback(RtcEngineEvents.FirstRemoteAudioDecoded, uid.toUInt().toLong(), elapsed)
  }

  @Deprecated("", ReplaceWith("onRemoteAudioStateChanged"))
  override fun onUserMuteAudio(uid: Int, muted: Boolean) {
    callback(RtcEngineEvents.UserMuteAudio, uid.toUInt().toLong(), muted)
  }

//  @Deprecated("", ReplaceWith("onRtmpStreamingStateChanged"))
//  override fun onStreamPublished(url: String?, @Annotations.AgoraErrorCode error: Int) {
//    callback(RtcEngineEvents.StreamPublished, url, error)
//  }

//  @Deprecated("", ReplaceWith("onRtmpStreamingStateChanged"))
//  override fun onStreamUnpublished(url: String?) {
//    callback(RtcEngineEvents.StreamUnpublished, url)
//  }

  @Deprecated("", ReplaceWith("onRemoteAudioStats"))
  override fun onRemoteAudioTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
    callback(
      RtcEngineEvents.RemoteAudioTransportStats,
      uid.toUInt().toLong(),
      delay,
      lost,
      rxKBitRate
    )
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStats"))
  override fun onRemoteVideoTransportStats(uid: Int, delay: Int, lost: Int, rxKBitRate: Int) {
    callback(
      RtcEngineEvents.RemoteVideoTransportStats,
      uid.toUInt().toLong(),
      delay,
      lost,
      rxKBitRate
    )
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStateChanged"))
  override fun onUserEnableVideo(uid: Int, enabled: Boolean) {
    callback(RtcEngineEvents.UserEnableVideo, uid.toUInt().toLong(), enabled)
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStateChanged"))
  override fun onUserEnableLocalVideo(uid: Int, enabled: Boolean) {
    callback(RtcEngineEvents.UserEnableLocalVideo, uid.toUInt().toLong(), enabled)
  }

  @Deprecated("", ReplaceWith("onRemoteVideoStateChanged"))
  override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
    callback(RtcEngineEvents.FirstRemoteVideoDecoded, uid.toUInt().toLong(), width, height, elapsed)
  }

  @Deprecated("", ReplaceWith("onConnectionStateChanged"))
  override fun onConnectionInterrupted() {
    callback(RtcEngineEvents.ConnectionInterrupted)
  }

  @Deprecated("", ReplaceWith("onConnectionStateChanged"))
  override fun onConnectionBanned() {
    callback(RtcEngineEvents.ConnectionBanned)
  }

  @Deprecated("", ReplaceWith("onRemoteAudioStats"))
  override fun onAudioQuality(
    uid: Int,
    @Annotations.AgoraNetworkQuality quality: Int,
    delay: Short,
    lost: Short
  ) {
    callback(RtcEngineEvents.AudioQuality, uid.toUInt().toLong(), quality, delay, lost)
  }

  @Deprecated("", ReplaceWith("onLocalVideoStateChanged"))
  override fun onCameraReady() {
    callback(RtcEngineEvents.CameraReady)
  }

  @Deprecated("", ReplaceWith("onLocalVideoStateChanged"))
  override fun onVideoStopped() {
    callback(RtcEngineEvents.VideoStopped)
  }

  override fun onFirstLocalAudioFramePublished(elapsed: Int) {
    callback(RtcEngineEvents.FirstLocalAudioFramePublished, elapsed)
  }

  override fun onFirstLocalVideoFramePublished(source: Constants.VideoSourceType?, elapsed: Int) {
    callback(RtcEngineEvents.FirstLocalVideoFramePublished, Constants.VideoSourceType.getValue(source), elapsed)
  }

  override fun onAudioPublishStateChanged(
    channel: String?,
    oldState: Int,
    newState: Int,
    elapseSinceLastState: Int
  ) {
    callback(
      RtcEngineEvents.AudioPublishStateChanged,
      Constants.VideoSourceType.VIDEO_SOURCE_UNKNOWN,
      channel,
      oldState,
      newState,
      elapseSinceLastState
    )
  }

  override fun onVideoPublishStateChanged(
    source: Constants.VideoSourceType?,
    channel: String?,
    oldState: Int,
    newState: Int,
    elapseSinceLastState: Int
  ) {
    callback(
      RtcEngineEvents.VideoPublishStateChanged,
      Constants.VideoSourceType.getValue(source),
      channel,
      oldState,
      newState,
      elapseSinceLastState
    )
  }

  override fun onAudioSubscribeStateChanged(
    channel: String?,
    uid: Int,
    oldState: Int,
    newState: Int,
    elapseSinceLastState: Int
  ) {
    callback(
      RtcEngineEvents.AudioSubscribeStateChanged,
      channel,
      uid.toUInt().toLong(),
      oldState,
      newState,
      elapseSinceLastState
    )
  }

  override fun onVideoSubscribeStateChanged(
    channel: String?,
    uid: Int,
    oldState: Int,
    newState: Int,
    elapseSinceLastState: Int
  ) {
    callback(
      RtcEngineEvents.VideoSubscribeStateChanged,
      channel,
      uid.toUInt().toLong(),
      oldState,
      newState,
      elapseSinceLastState
    )
  }

  override fun onRtmpStreamingEvent(url: String?, error: Int) {
    callback(RtcEngineEvents.RtmpStreamingEvent, url, error)
  }

  override fun onUploadLogResult(
    requestId: String?,
    success: Boolean,
    @Annotations.AgoraUploadErrorReason reason: Int
  ) {
    callback(RtcEngineEvents.UploadLogResult, requestId, success, reason)
  }


//  public void onSnapshotTaken(int uid, String filePath, int width, int height, int errCode) {

  override fun onSnapshotTaken(
    uid: Int,
    filePath: String?,
    width: Int,
    height: Int,
    errCode: Int
  ) {
    callback(
      RtcEngineEvents.SnapshotTaken,
      uid.toUInt().toLong(),
      filePath,
      width,
      height,
      errCode
    )
  }

  override fun onContentInspectResult(result: Int) {
    callback(RtcEngineEvents.ContentInspectResult, result)
  }

  override fun onClientRoleChangeFailed(reason: Int, currentRole: Int) {
    callback(RtcEngineEvents.ClientRoleChangeFailed, reason, currentRole)
  }
}
