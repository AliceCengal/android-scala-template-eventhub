package com.marsupial.wombat.service

import scala.collection.mutable

import android.os.{Message, Handler}
import scala.ref.WeakReference

/**
 * A global event hub that allows different components of
 * the app to communicate without explicit coupling.
 */
object EventHub {

  /**
   * Subscribe to the global event stream
   */
  case class Subscribe(who: HandlerActor)

  /**
   * Unsubscribe from the global event stream
   */
  case class Unsubscribe(who: HandlerActor)

}

private[service] class EventHub extends Handler.Callback {

  import EventHub._

  private val subscribers: mutable.Set[WeakReference[HandlerActor]] = mutable.Set.empty

  def handleMessage(incoming: Message): Boolean = {
    incoming.obj match {
      case Subscribe(who)   => subscribers.add(new WeakReference[HandlerActor](who))
      case Unsubscribe(who) => purgeSubscribers(who)
      case a: AnyRef        => broadcastEvent(a)
    }
    true
  }

  private def purgeSubscribers(who: HandlerActor): Unit = {
    subscribers.retain((weakHandler) => {
      weakHandler.get.isDefined && weakHandler() != who
    })
  }

  private def broadcastEvent(event: AnyRef): Unit = {
    for (
      weakRefToHandler <- subscribers;
      maybeHandler     <- weakRefToHandler.get
    ) {
      maybeHandler ! event
    }
  }

}