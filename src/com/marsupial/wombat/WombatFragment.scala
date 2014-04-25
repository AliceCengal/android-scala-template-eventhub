package com.marsupial.wombat

import android.app.Fragment
import android.view.View
import android.widget.Button

import com.marsupial.wombat.service.AppService
import com.marsupial.wombat.WombatFragment.DoneWatchingWombat

/**
 * Page where you can gaze on a wombat.
 */
class WombatFragment extends Fragment
                             with AppService.FragmentInjection
                             with FragmentViewUtil
                             with View.OnClickListener {

  def layoutId: Int = R.layout.wombat_layout

  def btnBack = component[Button](R.id.button)

  override def onStart() {
    super.onStart()
    btnBack.setOnClickListener(this)
  }

  def onClick(v: View): Unit = {
    app.eventHub ! DoneWatchingWombat
  }

}

object WombatFragment {

  case object DoneWatchingWombat

}