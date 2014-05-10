package com.marsupial.wombat.service

import android.os.{Message, Handler}

/**
 * A basic Actor that uses Android's messaging framework.
 */
trait HandlerActor {
  def !(msg: AnyRef): Unit
  def ?(msg: AnyRef)(implicit requester: Handler): Unit
  def tell(msg: AnyRef): Unit
  def request(msg: AnyRef)(implicit requester: Handler): Unit
}

class ActorShell(h: Handler) extends HandlerActor {
  override def !(msg: AnyRef): Unit = {
    Message.obtain(h, 0, msg).sendToTarget()
  }

  override def tell(msg: AnyRef): Unit = {
    Message.obtain(h, 0, msg).sendToTarget()
  }

  override def ?(msg: AnyRef)(implicit requester: Handler): Unit = {
    Message.obtain(h, 0, (requester, msg)).sendToTarget()
  }

  override def request(msg: AnyRef)(implicit requester: Handler): Unit = {
    Message.obtain(h, 0, (requester, msg)).sendToTarget()
  }
}

trait ActorConversion {
  implicit def handlerToActor(h: Handler): HandlerActor = new ActorShell(h)
}
