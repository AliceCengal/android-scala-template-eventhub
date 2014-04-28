package com.marsupial.wombat

import android.app.Fragment
import android.view.View
import android.widget.Button

import com.marsupial.wombat.service.AppService

/**
 * Page where you can gaze on a wombat.
 */
class WombatFragment extends Fragment
                             with AppService.FragmentInjection
                             with FragmentViewUtil
                             with View.OnClickListener {

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