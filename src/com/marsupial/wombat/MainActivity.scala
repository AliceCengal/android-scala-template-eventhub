package com.marsupial.wombat

import android.app.{Fragment, Activity}
import android.os.{Message, Handler, Bundle}

import com.marsupial.wombat.service.{HandlerActor, EventHub, AppService}

/**
 * The starting point of the app. This Activity does not show any UI directly,
 * instead delegating that task to the Fragments. It also handles interaction and
 * transition between Fragments.
 */
class MainActivity extends Activity
                           with AppService.ActivityInjection
                           with Handler.Callback {

  val bridge = HandlerActor.sync(this)
  var startTime = -1L

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)
    setContentView(R.layout.main)
    addFragment(new PerthFragment)
  }

  override def onStart() {
    super.onStart()
    app.eventHub ! EventHub.Subscribe(bridge)
  }

  override def onStop() {
    super.onStop()
    app.eventHub ! EventHub.Unsubscribe(bridge)
  }

  def handleMessage(msg: Message): Boolean = {
    msg.obj match {
      case PerthFragment.DisplayWombat =>
        startTime = System.currentTimeMillis()
        addFragment(new WombatFragment)

      case PerthFragment.RequestStatus =>
        if (startTime == -1L) {
          app.eventHub ! PerthFragment.ShowStatus("Click to see a wombat")
        } else {
          app.eventHub ! PerthFragment.ShowStatus("You spied a wombat for " + startTime + " seconds")
        }

      case WombatFragment.DoneWatchingWombat =>
        addFragment(new PerthFragment)
        val currentTime = System.currentTimeMillis()
        startTime = (currentTime - startTime) / 1000

      case _ =>
    }
    true
  }

  def addFragment(fragment: Fragment) {
    getFragmentManager.
        beginTransaction().
        replace(android.R.id.content,
                fragment).
        commit()
  }
}
