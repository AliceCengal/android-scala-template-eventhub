package com.marsupial.wombat

import android.app.Fragment
import android.view.View
import android.widget.Button

import com.marsupial.wombat.service.AppService
import com.marsupial.wombat.framework.{FragmentInjection, Helpers, ActorConversion}

/**
 * Page where you can gaze on a wombat.
 */
class WombatFragment extends Fragment
                             with FragmentInjection[AppService]
                             with Helpers.EasyFragment
                             with View.OnClickListener
                             with ActorConversion
{
  import WombatFragment._

  private def btnBack = component[Button](R.id.button)

  override def layoutId = R.layout.wombat_layout

  override def onStart() {
    super.onStart()
    btnBack.setOnClickListener(this)
  }

  override def onClick(v: View): Unit = {
    app.eventHub ! DoneWatchingWombat
  }

}

object WombatFragment {

  case object DoneWatchingWombat

}