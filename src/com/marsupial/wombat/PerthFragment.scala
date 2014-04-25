package com.marsupial.wombat

import android.app.Fragment
import android.os.{Message, Handler}
import android.widget.{TextView, Button}
import android.view.View

import com.marsupial.wombat.service.{HandlerActor, EventHub, AppService}
import com.marsupial.wombat.PerthFragment.{RequestStatus, ShowStatus, DisplayWombat}

/**
 * Shows a skyline of Perth
 */
class PerthFragment extends Fragment
                            with AppService.FragmentInjection
                            with FragmentViewUtil
                            with View.OnClickListener
                            with Handler.Callback {

  val bridge = HandlerActor.sync(this)

  def layoutId = R.layout.perth_layout

  def btnSwitch = component[Button](R.id.button)
  def textStatus = component[TextView](R.id.textView)

  override def onStart() {
    super.onStart()
    app.eventHub ! EventHub.Subscribe(bridge)
    btnSwitch.setOnClickListener(this)
    app.eventHub ! RequestStatus
  }

  override def onStop() {
    super.onStop()
    app.eventHub ! EventHub.Unsubscribe(bridge)
  }

  def handleMessage(msg: Message): Boolean = {
    msg.obj match {
      case ShowStatus(status) => textStatus.setText(status)
      case _                  => /* Do nothing */
    }
    true
  }

  def onClick(v: View): Unit = {
    app.eventHub ! DisplayWombat
  }
}

object PerthFragment {

  case object DisplayWombat

  case object RequestStatus

  case class ShowStatus(status: String)

}