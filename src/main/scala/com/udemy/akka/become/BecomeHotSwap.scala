package com.udemy.akka.become

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by akshayh on 1/7/17.
  */
object BecomeHotSwap extends App{
  import UserStorage._

  val system = ActorSystem("Hotswap-Become")
  private val userStorage: ActorRef = system.actorOf(Props[UserStorage],"userStorage")

  userStorage ! Operation(DBOperation.Create,Some(User("sam","sam@gmail.com")))
  userStorage ! Connect
  userStorage ! Disconnect
  Thread.sleep(100)
  system.terminate()

}
