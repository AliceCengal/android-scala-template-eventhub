package com.marsupial.wombat.service

import com.marsupial.wombat.framework.EventfulApp

/**
 * Provide global backend services
 */
class AppService extends android.app.Application with EventfulApp {

  def hello = "Hello"

}
