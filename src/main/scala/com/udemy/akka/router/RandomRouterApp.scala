package com.udemy.akka.router

import akka.actor.{ActorSystem, Props}
import akka.routing.{ActorRefRoutee, FromConfig, RandomRoutingLogic, Router}
import com.udemy.akka.router.Worker.Work

/**
  * Created by akshayh on 1/7/17.
  */
object RandomRouterApp extends App {
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
    val routerPool = system.actorOf(FromConfig.props(Props[Worker]), "router1")

    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    //  router.route(Work(),routerPool)
    routerPool ! Work()

    routerPool ! Work()

    routerPool ! Work()

    Thread.sleep(100)
    system.terminate()
  } catch {
    case e:akka.ConfigurationException=> println(e.printStackTrace())

    case e:Throwable => println(e.printStackTrace())

  } finally {
    system.terminate()
  }
}
