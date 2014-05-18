package com.marsupial.wombat.service

import android.os.{HandlerThread, Handler}
import com.marsupial.wombat.framework.{Initialize, EventfulApp}

/**
 * Provide global backend services
 */
class AppService extends android.app.Application with EventfulApp {

  private var services: Seq[Handler] = List.empty

  override def onCreate() {
    super.onCreate()
    initializeServices()
  }

  def imageServer: Handler = services(0)

  private def initializeServices() {
    val thread = new HandlerThread("ServiceThread")
    thread.start()
    services = List(new ImageServer).
                 map(callback => new Handler(thread.getLooper, callback))
    services.foreach(_ ! Initialize(this))
  }

}
