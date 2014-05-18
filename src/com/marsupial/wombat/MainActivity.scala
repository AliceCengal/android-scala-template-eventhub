package com.marsupial.wombat

import android.app.{Fragment, Activity}
import android.os.{Message, Handler, Bundle}

import com.marsupial.wombat.service.{ChattyActivity, ActorConversion, EventHub, AppService}

/**
 * The starting point of the app. This Activity does not show any UI directly,
 * instead delegating that task to the Fragments. It also handles interaction and
 * transition between Fragments.
 */
class MainActivity extends Activity
                           with ChattyActivity
                           with Handler.Callback
{
  private var startTime = -1L

  override def onCreate(saved: Bundle) {
    super.onCreate(saved)
    setContentView(R.layout.main)
    addFragment(new PerthFragment)
  }

  override def handleMessage(msg: Message): Boolean = {
    msg.obj match {
      case PerthFragment.DisplayWombat =>
        startTime = System.currentTimeMillis()
        addFragment(new WombatFragment)

      case PerthFragment.BroadcastStatus =>
        if (startTime == -1L) {
          app.eventHub ! PerthFragment.Clean
        } else {
          app.eventHub ! PerthFragment.WombatLover("You spied a wombat for " + startTime + " seconds")
        }

      case WombatFragment.DoneWatchingWombat =>
        addFragment(new PerthFragment)
        val currentTime = System.currentTimeMillis()
        startTime = (currentTime - startTime) / 1000

      case _ => /* Do nothing */
    }
    true
  }

  private def addFragment(fragment: Fragment) {
    getFragmentManager.
        beginTransaction().
        replace(android.R.id.content,
                fragment).
        commit()
  }

}
