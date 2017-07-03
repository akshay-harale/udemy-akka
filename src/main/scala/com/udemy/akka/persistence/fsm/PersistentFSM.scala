package com.udemy.akka.persistence.fsm

import akka.actor.{ActorRef, ActorSystem, Props}

/**
  * Created by akshayh on 2/7/17.
  */
object PersistentFSM extends App{
  import Account._
  private val system: ActorSystem = ActorSystem("persistent-fsm-actor")
  private val account: ActorRef = system.actorOf(Props[Account],"account")
  account ! Operation(1000,CR)
  account ! Operation(10,DR)

  Thread.sleep(1000)
  system.terminate()



}
