package com.udemy.akka.router

import akka.actor.{ActorRef, ActorSystem, Props}
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
object RouterGroupApp extends App{
  val system=ActorSystem("RouterGroup")
  system.actorOf(Props[Worker],"w1")
  system.actorOf(Props[Worker],"w2")
  system.actorOf(Props[Worker],"w3")
  system.actorOf(Props[Worker],"w4")
  system.actorOf(Props[Worker],"w5")

  val workers:List[String]=List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5"
  )

  private val routerGroup: ActorRef = system.actorOf(Props(new RouterGroup(workers)))

  routerGroup ! Work()
  routerGroup ! Work()
  Thread.sleep(100)
  system.terminate()


}
