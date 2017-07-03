package com.udemy.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.actor.Actor.Receive
import com.udemy.akka.MusicController.{Play, Stop}
import com.udemy.akka.MusicPlayer.{StartMusic, StopMusic}

/**
  * Created by akshayh on 19/6/17.
  */

object MusicPlayer {
  sealed trait PlayMsg
  case object StopMusic extends PlayMsg
  case object StartMusic extends PlayMsg

}

class MusicPlayer extends Actor{
  override def receive: Receive = {
    case StopMusic => println("I don't want to stop music ....")
    case StartMusic =>
      val controller= context.actorOf(MusicController.props,"controller")
      controller ! Play
    case _ => println("Unknown")
  }

}

object MusicController {
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg
  def props=Props[MusicController]
}

class MusicController extends Actor{
  override def receive: Receive = {
    case Play=> println("Playing music .... ")
    case Stop => println("Stop music .... ")
  }
}
object ActorCreation extends App{
  val system=ActorSystem("CreationSystem")
  private val player: ActorRef = system.actorOf(Props[MusicPlayer],"player")
  player ! StartMusic
  system.terminate()
}
