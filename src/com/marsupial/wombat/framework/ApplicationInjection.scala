package com.marsupial.wombat.framework

import android.app.{Fragment, Activity}
import android.os.Handler

/**
 * Mixin intended for the companion object of the app's subclass of
 * android.app.Application.
 *
 * Created by athran on 5/18/14.
 */
abstract class ApplicationInjection[APP <: android.app.Application with EventfulApp] {

  /**
   * Allow easy access to the Application object in Activity
   */
  trait ActivityInjection {
    self: Activity =>

    def app = self.getApplication.asInstanceOf[APP]

  }

  /**
   * Allow easy access to the Application object in Fragment
   */
  trait FragmentInjection {
    self: Fragment =>

    def app = self.getActivity.getApplication.asInstanceOf[APP]

  }

  case class Initialize(app: APP)

}

/**
 * Allow easy access to the Application object in Activity
 */
trait ActivityInjection[APP <: EventfulApp] {
  self: Activity =>

  def app = self.getApplication.asInstanceOf[APP]

}

/**
 * Allow easy access to the Application object in Fragment
 */
trait FragmentInjection[APP <: EventfulApp] {
  self: Fragment =>

  def app = self.getActivity.getApplication.asInstanceOf[APP]

}

/**
 * A message to be sent to Server object so that they can initialize
 * themselves with whatever Android Service they need for their work.
 */
case class Initialize[APP <: EventfulApp](app: APP)

/**
 * This mixin turns the subclass of Application into an EventHub host.
 */
trait EventfulApp extends android.app.Application {

  private val eventHubHandle = new Handler(new EventHub)

  def eventHub: Handler = eventHubHandle

}


