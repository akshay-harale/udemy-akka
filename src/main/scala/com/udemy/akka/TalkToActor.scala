package com.udemy.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.udemy.akka.Checker.{BlackUser, CheckUser, WhiteUser}
import com.udemy.akka.Recorder.NewUser
import com.udemy.akka.Storage.AddUser

import scala.concurrent.duration._


/**
  * Created by akshayh on 19/6/17.
  */

case class User(username: String, email: String)

object Recorder {

  sealed trait RecorderMsg

  case class NewUser(user: User) extends RecorderMsg

  def props(checker: ActorRef, storage: ActorRef) = Props(new Recorder(checker, storage))

}

object Checker {

  sealed trait CheckerMsg

  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckResponse

  case class BlackUser(user: User) extends CheckerMsg

  case class WhiteUser(user: User) extends CheckerMsg

}

object Storage {

  sealed trait StorageMsg

  case class AddUser(user: User) extends StorageMsg

}

class Storage extends Actor {
  var userList = List.empty[User]

  override def receive: Receive = {
    case AddUser(user) =>
      userList = user :: userList
      println("User added")
  }


}

class Checker extends Actor {
  val blacklisted = List(User("Adam", "adam@gmail.com"))

  override def receive: Receive = {
    case CheckUser(user) if blacklisted.contains(user) =>
      println("User is blacklisted")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      println(s"$user is not blacklisted")
      sender() ! WhiteUser(user)
  }
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {

  implicit val timeout = Timeout(5 seconds)

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {

    case NewUser(user) =>
      checker ? CheckUser(user) map {
        case WhiteUser(user) =>
          storage ! AddUser(user)
        case BlackUser(user) =>
          println(s"Recorder: ${user} is in blacklist")
      }
  }
}

object TalkToActor extends App {
  val system = ActorSystem("TalkingSystem")
  private val checker: ActorRef = system.actorOf(Props[Checker], "CheckerActor")
  private val storage: ActorRef = system.actorOf(Props[Storage], "StorageActor")
  private val recorder: ActorRef = system.actorOf(Recorder.props(checker, storage), "RecorderActor")
  recorder ! Recorder.NewUser(User("Akshay", "a"))
  Thread.sleep(1000)
  system.terminate()

}





