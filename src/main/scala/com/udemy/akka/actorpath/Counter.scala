package com.udemy.akka.actorpath

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.udemy.akka.actorpath.Counter.{Dec, Inc}

/**
  * Created by akshayh on 27/6/17.
  */
class Counter extends Actor{
  var count=0
  override def receive: Receive = {
    case Inc(x)=>count += x
    case Dec(x)=>count -= x
  }
}

object Counter {
  final case class Inc(num:Int)
  final case class Dec(num:Int)
}
