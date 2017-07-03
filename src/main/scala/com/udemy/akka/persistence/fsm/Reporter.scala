package com.udemy.akka.persistence.fsm

import akka.NotUsed
import akka.actor.ActorSystem
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
/**
  * Created by akshayh on 2/7/17.
  */
object Reporter extends App{
  private val system: ActorSystem = ActorSystem("persistent-query")


  val queries: LeveldbReadJournal = PersistenceQuery(system).readJournalFor(
    LeveldbReadJournal.Identifier
  )
  val source:Source[EventEnvelope,NotUsed]= queries.eventsByPersistenceId("Account",0L,Long.MaxValue)


  implicit val mat=ActorMaterializer()(system)
  source.runForeach{
    evt=>println(s"Event : $evt")
  }

  Thread.sleep(2000)
  system.terminate()

}
