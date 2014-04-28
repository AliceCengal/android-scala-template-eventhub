package com.marsupial.wombat

import android.app.Fragment
import android.os.{Message, Handler}
import android.widget.{TextView, Button}
import android.view.View

import com.marsupial.wombat.service.{HandlerActor, EventHub, AppService}

/**
 * Shows a skyline of Perth
 */
class PerthFragment extends Fragment
                            with AppService.FragmentInjection
                            with FragmentViewUtil
                            with View.OnClickListener
                            with Handler.Callback {

  import PerthFragment._

  private val bridge = HandlerActor.sync(this)

  private def btnSwitch = component[Button](R.id.button)
  private def textStatus = component[TextView](R.id.textView)

  override def layoutId = R.layout.perth_layout

  override def onStart() {
    super.onStart()
    app.eventHub ! EventHub.Subscribe(bridge)
    btnSwitch.setOnClickListener(this)
    app.eventHub ! BroadcastStatus
  }

  override def onStop() {
    super.onStop()
    app.eventHub ! EventHub.Unsubscribe(bridge)
  }

  override def handleMessage(msg: Message): Boolean = {
    msg.obj match {
      case Clean                 => textStatus.setText("Click to see a wombat")
      case WombatLover(affinity) => textStatus.setText(affinity)
      case _                     => /* Do nothing */
    }
    true
  }

  override def onClick(v: View): Unit = {
    app.eventHub ! DisplayWombat
  }

}

object PerthFragment {

  /**
   * User wants to see a wombat.
   */
  case object DisplayWombat

  /**
   * Request for a broadcast of the current status.
   */
  case object BroadcastStatus

  /**
   * Current status, in this case the user's affinity to wombats.
   */
  sealed abstract class CurrentStatus
  case object Clean extends CurrentStatus
  case class WombatLover(affinity: String) extends CurrentStatus

}