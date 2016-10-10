import akka.actor.{ActorSystem, Props}
import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import router.Routes

// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
  * Created by Nauman Badar on 2016-04-28.
  */


/*
ApplicationLoader as copied from play docs to enable compile DI. After extending trait 'ApplicationLoader'
application.conf has to set the property 'play.application.loader' to this class.
 */

class MyApplicationLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }
    new MyComponents(context).application
  }
}

class MyComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  //    override def router: Router = Router.empty


  lazy val assets = new controllers.Assets(httpErrorHandler)

  //all controllers need to initialised
  lazy val homeController = new controllers.HomeController

  //provide all controller objects and asset object to Routes, can check its constructor in auto-generated router in target folder
  lazy val router = new Routes(httpErrorHandler, homeController, assets)


  //for starting your Actor system

  /*
    val system: ActorSystem = ActorSystem("ParseActorSystem")
    val master = system.actorOf(Props(classOf[MasterActor],ProductionParseSystem),"MasterActor")
    master ! MasterActor.StartProcessing
  */


}