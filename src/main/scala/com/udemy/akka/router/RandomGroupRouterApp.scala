package com.udemy.akka.router

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
object RandomGroupRouterApp extends App{
  val system: ActorSystem = ActorSystem("sample")
  try {


    //  val router = {
    //    val routees = Vector.fill(5) {
    //      val r = system.actorOf(Props[Worker])
    //      ActorRefRoutee(r)
    //    }
    //    Router(RandomRoutingLogic(), routees)
    //  }
    //  val routerPool = system.actorOf(Props[Worker],"router5")

    system.actorOf(Props[Worker],"w1")
    system.actorOf(Props[Worker],"w2")
    system.actorOf(Props[Worker],"w3")
    system.actorOf(Props[Worker],"w4")
    system.actorOf(Props[Worker],"w5")

    val routerPool: ActorRef = system.actorOf(FromConfig.props(), "router3")

    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    routerPool ! Work()

    routerPool ! Work()

    routerPool ! Work()

    Thread.sleep(100)
    system.terminate()
    println("End")
  } catch {
    case e:akka.ConfigurationException=> println(e.printStackTrace())

    case e:Throwable => println(e.printStackTrace())

  } finally {
    system.terminate()
  }

}
