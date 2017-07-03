package com.udemy.akka.persistence

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by akshayh on 2/7/17.
  */
object PersistenceApp extends App{
  import Counter._
  private val system: ActorSystem = ActorSystem("Persistence-actors")
  private val counter: ActorRef = system.actorOf(Props[Counter])
  counter ! Cmd(Increament(3))
  counter ! Cmd(Increament(5))
  counter ! Cmd(Decreament(3))

  counter ! "print"
  Thread.sleep(1000)
  system.terminate()


}
