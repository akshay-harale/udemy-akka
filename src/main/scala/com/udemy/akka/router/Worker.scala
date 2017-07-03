package com.udemy.akka.router

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
class Worker extends Actor{
  override def receive: Receive = {
    case Work()=>
      println(s"I recieved work message and my actor ref is $self")
  }
}

object Worker {
  case class Work()
}