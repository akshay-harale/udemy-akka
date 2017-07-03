package com.udemy.akka.router

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
class RouterActor extends Actor{
  var routees:List[ActorRef] = _

  override def preStart() = {
    routees=List.fill(5)(
      context.actorOf(Props[Worker])
    )
  }
  override def receive: Receive = {
    case msg:Work=>
      println("I am a router and I received a message .. ")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

class RouterGroup(routees:List[String]) extends Actor{
  override def receive: Receive = {
    case msg:Work=>{
      println("I am a router group and I received Work message")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward msg
    }
  }
}