package com.udemy.akka

import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import com.udemy.akka.Aphrodite.{RestartException, ResumeException, StopException}

/**
  * Created by akshayh on 19/6/17.
  */

class Aphrodite extends Actor{


  override def preStart(): Unit = {
    println("Aphrodite preStart hook ..... ")
    super.preStart()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Aphrodite preRestart hook ..... ")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Aphrodite postRestart hook ..... ")
    super.postRestart(reason)
  }

  override def postStop(): Unit = {
    println("Aphrodite postStop hook ..... ")
    super.postStop()
  }

  override def receive: Receive = {
    case "Resume"=>
      throw ResumeException
    case "Stop"=>
      throw StopException
    case "Restart"=>
      throw RestartException
    case _=>println("Unknown")
  }
}

object Aphrodite{
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

class Hera extends Actor{
  import Aphrodite._
  var childRef:ActorRef=_
  import scala.concurrent.duration._
  override val supervisorStrategy =OneForOneStrategy(10,1 seconds){
    case ResumeException => Resume
    case RestartException => Restart
    case StopException => Stop
    case _:Exception => Escalate

  }

  override def receive: Receive = {
    case msg=>
      println(s"Hera recieved $msg")
      childRef ! msg
      Thread.sleep(100)
  }

  override def preStart(): Unit = {
    childRef=context.actorOf(Props[Aphrodite],"Aphrodite")
    super.preStart()
    Thread.sleep(100)
  }

}

object Supervision extends App{
  val system=ActorSystem("SupervisionDemo")
  private val hera: ActorRef = system.actorOf(Props[Hera], "hera")
//  hera ! "Resume"
//  Thread.sleep(100)
//  println()

//  hera ! "Restart"
//  Thread.sleep(100)
//  println()

  hera ! "Stop"
  Thread.sleep(100)
  println()
  system.terminate()
}

