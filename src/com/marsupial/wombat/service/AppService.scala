package com.marsupial.wombat.service

import android.app.{Activity, Fragment}

/**
 * Provide global backend services
 */
class AppService extends android.app.Application {

  private val eventHubHandle = HandlerActor.sync(new EventHub)

  override def onCreate() {
    super.onCreate()
    // Do other initialization
  }

  def eventHub: HandlerActor = eventHubHandle

}

object AppService {

  /**
   * Allow easy access to the Application object in Activity
   */
  trait ActivityInjection {
    self: Activity =>

    def app = self.getApplication.asInstanceOf[AppService]

  }

  /**
   * Allow easy access to the Application object in Fragment
   */
  trait FragmentInjection {
    self: Fragment =>

    def app = self.getActivity.getApplication.asInstanceOf[AppService]

  }

}