package com.udemy.akka.actorpath

import akka.actor.{ActorRef, ActorSelection, ActorSystem, PoisonPill, Props}

/**
  * Created by akshayh on 27/6/17.
  */
object ActorRefEx extends App{
  val system=ActorSystem("PathSystem")
  private val counter1: ActorRef = system.actorOf(Props[Counter],"counter")
  println(s"Actor reference for counter $counter1")
  private val counterSelection: ActorSelection = system.actorSelection("counter")
  println(s"Actor selection  for counter $counterSelection")
  counter1 ! PoisonPill
  Thread.sleep(100)
  private val counter2: ActorRef = system.actorOf(Props[Counter],"counter")
  println(s"Actor reference for counter $counter2")
  private val counterSelection2: ActorSelection = system.actorSelection("counter")
  println(s"Actor selection  for counter $counterSelection2")
  system.terminate()
}
