package com.udemy.akka.router

import akka.actor.{ActorRef, ActorSystem, Props}
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
object RouterApp extends App {
  val system = ActorSystem("RouterSystem")
  private val router: ActorRef = system.actorOf(Props[RouterActor])
  router ! Work()
  router ! Work()
  router ! Work()
  Thread.sleep(100)
  system.terminate()
}
