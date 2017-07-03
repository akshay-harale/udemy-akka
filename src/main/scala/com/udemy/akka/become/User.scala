package com.udemy.akka.become

import akka.actor.{Actor, Stash}
import akka.actor.Actor.Receive
import com.udemy.akka.become.UserStorage.{Connect, Disconnect, Operation}

/**
  * Created by akshayh on 1/7/17.
  */
case class User(username:String,email:String)

object UserStorage{
  trait DBOperation
  object DBOperation{
    case object Create extends DBOperation
    case object Read extends DBOperation
    case object Update extends DBOperation
    case object Delete extends DBOperation
  }

  case object Connect
  case object Disconnect

  case class Operation(operation:DBOperation,user:Option[User])
}

class UserStorage extends Actor with Stash{
  override def receive: Receive = disconnected

  def connected:Actor.Receive= {
    case Disconnect=>
      println("User storage disconnected from DB")
      unstashAll()
      context.unbecome()
    case Operation(op,user)=>
      println(s"User storage recieved $op to do in user : $user")
  }

  def disconnected:Actor.Receive = {
    case Connect =>
      println("User storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }


}
