package com.udemy.akka.fsm

import akka.actor.{Actor, ActorRef, ActorSystem, FSM, Props, Stash}
import com.udemy.akka.User
import com.udemy.akka.fsm.UserStorageFSM._


object UserStorageFSM {

  sealed trait State

  case object Connected extends State

  case object Disconnected extends State

  sealed trait Data

  case object EmptyData extends Data

  trait DBOperation

  object DBOperation {

    case object Create extends DBOperation

    case object Read extends DBOperation

    case object Update extends DBOperation

    case object Delete extends DBOperation

  }

  case object Connect

  case object Disconnect

  case class Operation(operation: DBOperation, user: Option[User])

}

class UserStorageFSM extends FSM[UserStorageFSM.State, UserStorageFSM.Data] with Stash {
  // 1. Define start with
  startWith(Disconnected, EmptyData)

  // 2. Define states
  when(Disconnected) {
    case Event(Connect, _) =>
      println("UserStorage connected to DB")
      unstashAll()
      goto(Connected) using (EmptyData)
    case Event(_, _) =>
      stash()
      stay using (EmptyData)
  }
  when(Connected) {
    case Event(Disconnect, _) =>
      println("Disconnected from Database")
      goto(Disconnected) using (EmptyData)
    case Event(Operation(op, user), _) =>
      println(s"User storage recieved $op operation to do in $user")
      stay using (EmptyData)
  }

  // 3. initialize
  initialize()
}

object FiniteStateMachine extends App {

  val system = ActorSystem("HotSwap-FSM")
  private val userFSM: ActorRef = system.actorOf(Props[UserStorageFSM], "UserSTorageFSM")
  userFSM ! Connect
  userFSM ! Operation(DBOperation.Create, Some(User("sam", "sam@gmail.com")))
  userFSM ! Disconnect
  system.terminate()

}
