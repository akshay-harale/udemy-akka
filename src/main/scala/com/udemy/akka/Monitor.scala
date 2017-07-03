package com.udemy.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.actor.Actor.Receive


/**
  * Created by akshayh on 19/6/17.
  */

class Ares(athena:ActorRef) extends Actor{
  override def preStart(): Unit = {
    context.watch(athena)
  }

  override def postStop(): Unit = {
    println("Ares post stop")
  }
  override def receive: Receive = {
    case Terminated=>{

      context.stop(self)
    }
  }
}
class Athena extends Actor{
  override def receive: Receive = {
    case msg=>
      println(s"Athena recieved ${msg}")
      context.stop(self)
  }
}


object Monitor extends App{
  val system =ActorSystem("MonitorSystem")


  private val athena: ActorRef = system.actorOf(Props[Athena],"athena")
  private val ares: ActorRef = system.actorOf(Props(new Ares(athena)),"ares")
  athena ! "hello"
  system.terminate()
}
