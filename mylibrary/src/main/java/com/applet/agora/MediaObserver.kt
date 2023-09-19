package com.applet.agora

import androidx.annotation.IntRange
import io.agora.rtc2.IMetadataObserver
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class MediaObserver(
  private val emit: (data: Map<String, Any?>?) -> Unit
) : IMetadataObserver {
  private var maxMetadataSize = AtomicInteger(1024)
  private var metadataList = Collections.synchronizedList<String>(mutableListOf())

  fun addMetadata(metadata: String) {
    metadataList.add(metadata)
  }

  fun setMaxMetadataSize(@IntRange(from = 0, to = 1024) size: Int) {
    maxMetadataSize.set(size)
  }

  override fun onReadyToSendMetadata(timeStampMs: Long, sourceType: Int): ByteArray {
    if (metadataList.size > 0) {
      return metadataList.removeAt(0).toByteArray()
    }
    return "".toByteArray()
  }

  override fun getMaxMetadataSize(): Int {
    return maxMetadataSize.get()
  }

  override fun onMetadataReceived(buffer: ByteArray, uid: Int, timeStampMs: Long) {
    emit(
      hashMapOf(
        "data" to arrayListOf(String(buffer), uid.toUInt().toLong(), timeStampMs)
      )
    )
  }
}
