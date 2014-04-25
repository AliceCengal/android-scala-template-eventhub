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
  import MainActivity._

  val bridge = HandlerActor.sync(this)

  def layoutId = R.layout.perth_layout

  def btnSwitch = component[Button](R.id.button)
  def textStatus = component[TextView](R.id.textView)

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

  def handleMessage(msg: Message): Boolean = {
    msg.obj match {
      case Clean                 => textStatus.setText("Click to see a wombat")
      case WombatLover(affinity) => textStatus.setText(affinity)
      case _                     => /* Do nothing */
    }
    true
  }

  def onClick(v: View): Unit = {
    app.eventHub ! DisplayWombat
  }

}

object PerthFragment {

  /**
   * User wants to see a wombat.
   */
  case object DisplayWombat

}