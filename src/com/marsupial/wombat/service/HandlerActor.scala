package com.marsupial.wombat.service

import android.os.{Looper, Message, Handler}

/**
 * A basic Actor that uses Android's messaging framework.
 */
trait HandlerActor extends Handler with Actor {

  override def !(msg: AnyRef): Unit = {
    Message.obtain(this, 0, msg).sendToTarget()
  }
  
  override def tell(msg: AnyRef): Unit = {
    Message.obtain(this, 0, msg).sendToTarget()
  }

  override def ?(msg: AnyRef)(implicit requester: HandlerActor): Unit = {
    Message.obtain(this, 0, (requester, msg)).sendToTarget()
  }

  override def request(msg: AnyRef)(implicit requester: HandlerActor): Unit = {
    Message.obtain(this, 0, (requester, msg)).sendToTarget()
  }
  
}

trait Actor {
  def !(msg: AnyRef): Unit
  def ?(msg: AnyRef)(implicit requester: HandlerActor): Unit
  def tell(msg: AnyRef): Unit
  def request(msg: AnyRef)(implicit requester: HandlerActor): Unit
}

class ActorShell(h: Handler) extends Actor {
  override def !(msg: AnyRef): Unit = {
    Message.obtain(h, 0, msg).sendToTarget()
  }

  override def tell(msg: AnyRef): Unit = {
    Message.obtain(h, 0, msg).sendToTarget()
  }

  override def ?(msg: AnyRef)(implicit requester: HandlerActor): Unit = {
    Message.obtain(h, 0, (requester, msg)).sendToTarget()
  }

  override def request(msg: AnyRef)(implicit requester: HandlerActor): Unit = {
    Message.obtain(h, 0, (requester, msg)).sendToTarget()
  }
}

trait ActorConversion {
  implicit def handlerToActor(h: Handler): Actor = new ActorShell(h)
}

object HandlerActor {

  /**
   * Create a synchronous Handler that runs tasks on the main thread
   */
  def sync(callback: Handler.Callback): HandlerActor = {
    new SyncHandlerActor(callback)
  }

  /**
   * Create an asynchronous Handler that runs tasks on a separate HandlerThread,
   * one whose looper is passed in as parameter.
   */
  def async(looper: Looper,
            callback: Handler.Callback): HandlerActor = {
    new AsyncHandlerActor(looper, callback)
  }

  private class AsyncHandlerActor(looper: Looper,
                                  callback: Handler.Callback)
      extends Handler(looper, callback)
              with HandlerActor

  private class SyncHandlerActor(callback: Handler.Callback)
      extends Handler(callback)
              with HandlerActor

}
