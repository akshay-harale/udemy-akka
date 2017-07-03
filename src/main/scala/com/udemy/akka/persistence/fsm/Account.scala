package com.udemy.akka.persistence.fsm

import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM._
import com.udemy.akka.persistence.fsm.Account._

import scala.reflect._

/**
  * Created by akshayh on 2/7/17.
  */
object Account {

  // account states
  sealed trait State extends FSMState

  case object Empty extends State{
    override def identifier: String = "Empty"
  }

  case object Active extends State{
    override def identifier: String = "Active"
  }

  //acount data

  sealed trait Data {
    val amount:Float
  }

  case object ZeroBalance extends Data{
    override val amount: Float = 0.0f
  }

  case class Balance(override val amount:Float) extends Data

  // Domain events (persist events)
  sealed trait DomainEvent
  case class AcceptedTransaction(amount:Float,`type`:TransactionType) extends DomainEvent
  case class RejectedTransaction(amount:Float,`type`:TransactionType,reason:String) extends DomainEvent

  // transaction types
  sealed trait TransactionType
  case object DR extends TransactionType
  case object CR extends TransactionType

  // commands

  case class Operation(amount:Float,`type`:TransactionType)

}

class Account extends PersistentFSM[Account.State,Account.Data,Account.DomainEvent]{

  override def persistenceId: String = "Account"

  override def applyEvent(domainEvent: DomainEvent, currentData: Data): Data = {
    domainEvent match {
      case AcceptedTransaction(amount, CR)=>
        val newAmount=currentData.amount+amount
        println(s"Your new balance is $newAmount")
        Balance(newAmount)
      case AcceptedTransaction(amount, DR)=>
        val newAmount=currentData.amount-amount
        println(s"Your new balance is $newAmount")
        if(newAmount > 0)
          Balance(newAmount)
        else
          ZeroBalance
      case RejectedTransaction(_,_,reason)=>
        println(s"transaction rejected because $reason")
        currentData
    }
  }

  override def domainEventClassTag: ClassTag[DomainEvent] = classTag[DomainEvent]

  startWith(Empty,ZeroBalance)

  when(Empty){
    case Event(Operation(amount,CR),_)=>
      println("Its your first credit transaction")
      goto(Active) applying AcceptedTransaction(amount,CR)
    case Event(Operation(amount,DR),_)=>
      println("Your account has zero balance")
      stay applying RejectedTransaction(amount,DR,"Zero balance")
  }

  when(Active){
    case Event(Operation(amount,CR),_)=>
      stay applying AcceptedTransaction(amount,CR)
    case Event(Operation(amount,DR),balace)=>
      val newBalance=balace.amount-amount
      if(newBalance > 0)
        stay applying AcceptedTransaction(amount,DR)
      else if(newBalance == 0)
        goto(Empty) applying AcceptedTransaction(amount,DR)
      else
        stay applying RejectedTransaction(amount,DR,"Insufficient balance")
  }

}