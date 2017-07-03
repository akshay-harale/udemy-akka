package com.udemy.akka

import akka.actor.{Actor, ActorSystem, Props}

/**
  * Created by akshayh on 18/6/17.
  */

case class WhoToGreet(who: String)

class Greeter extends Actor {

  override def receive: Receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}

object HelloAkka extends App {
  // create the hello akka actor system
  val system = ActorSystem("Hello-AKka")
  // create actor greeter
  val greeter = system.actorOf(Props[Greeter], "greeter")
  // Send WhoToGreet message to actor
  greeter ! WhoToGreet("Akshay")

}
