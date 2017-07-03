package com.udemy.akka.actorpath

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}


/**
  * Created by akshayh on 27/6/17.
  */
class Watcher extends Actor {
  var counterRef:ActorRef = _
  val selection = context.actorSelection("/user/counter")
  selection ! Identify(None)
  override def receive: Receive = {
    case ActorIdentity(_,Some(ref)) =>
      println(s"Actor reference for counter is $ref")
    case ActorIdentity(_,None)=>
      println("Actor selection of actor does not exist ")

  }
}
