package serviceActors

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import serviceActors.MyActor.Greetings

/**
  * Created by nb on 16/11/02.
  */
class MyActor extends Actor with LazyLogging {
  override def receive: Receive = {
    case Greetings =>
      logger.debug("Greetings from Akka!")
  }
}

object MyActor {

  case object Greetings

}