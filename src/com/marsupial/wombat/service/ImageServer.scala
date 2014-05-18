package com.marsupial.wombat.service

import com.marsupial.wombat.framework.Server
import android.graphics.Bitmap

/**
 * Downloads and caches image on request
 */
object ImageServer {

  /**
   * Downloads or fetches the image from the URL.
   *
   * Reply: Image
   */
  case class DispatchImage(url: String)

  /**
   * Container for image reply
   */
  case class Image(url: String, img: Bitmap)

}

private[service] class ImageServer extends Server {
  import ImageServer._

  override def handleRequest(req: AnyRef): Unit = {
    req match {
      case DispatchImage(url) => sendImage(url)
      case _ =>
    }
  }

  private def sendImage(url: String) {
    requester ! Image(url, null)
  }

}
