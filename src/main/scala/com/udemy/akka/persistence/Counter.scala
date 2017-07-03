package com.udemy.akka.persistence

import akka.actor.ActorLogging
import akka.persistence._

/**
  * Created by akshayh on 2/7/17.
  */
object Counter {

  sealed trait Operation {
    val count: Int
  }

  case class Increament(override val count: Int) extends Operation

  case class Decreament(override val count: Int) extends Operation

  case class Cmd(op: Operation)

  case class Evt(op: Operation)

  case class State(count: Int)

}

class Counter extends PersistentActor with ActorLogging {

  import Counter._

  override def persistenceId: String = "counter-actor"

  var state: State = State(count = 0)

  def updateState(evt: Evt): Unit = evt match {
    case Evt(Increament(count)) =>
      state = State(state.count + count)
      takeSnapshotp
    case Evt(Decreament(count)) =>
      state = State(state.count - count)
      takeSnapshotp
  }

  override def receiveRecover: Receive = {
    case evt: Evt =>
      println(s"Received $evt in receive recover def ")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"Received snapshot with data $snapshot in receive recover def")
      state=snapshot
    case RecoveryCompleted=>{
      println("Recovery completed switching to receive mode")
    }
  }

  override def receiveCommand: Receive = {
    case cmd@Cmd(op) =>
      println(s"counter received $cmd with operation $op in receive command def")
      persist(Evt(op)) { evt =>
        updateState(evt)
      }
    case SaveSnapshotSuccess(metadata)=>
      println("Snapshot save success ")
    case SaveSnapshotFailure(metadata,reason)=>
      println("Snapshot save success ")

    case "print" =>
      println(s"current state of counter is $state")
  }

  //override def recovery: Recovery = Recovery.none
  def takeSnapshotp={
    if(state.count % 5 ==0){
      saveSnapshot(state)
    }
  }


}