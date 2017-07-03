package com.udemy.akka.actorpath

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by akshayh on 27/6/17.
  */
object Watch extends App{
  val system=ActorSystem("Watch-actor-selection")
  private val counter: ActorRef = system.actorOf(Props[Counter],"counter")
  private val watcher: ActorRef = system.actorOf(Props[Watcher],"watcher")
  system.terminate()
}
