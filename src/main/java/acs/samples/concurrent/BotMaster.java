package acs.samples.concurrent;

import akka.actor.AbstractActor;

import akka.actor.Actor;
import akka.actor.ActorSystem;
import akka.actor.Props;

final class BootMsg {

}

class AkkaBot extends AbstractActor {

  @Override
  public Receive createReceive() {
    return emptyBehavior();
  }
}

class Boot extends AbstractActor {

  @Override
  public Receive createReceive() {
    return receiveBuilder().
      match(BootMsg.class, s -> {
        context().actorOf(Props.create(BotMaster.class));
      }).
      build();
  }
}

public class BotMaster extends AbstractActor {

  public BotMaster() {
    for (int index = 0; index < 10; index++) {
      getContext().actorOf(Props.create(AkkaBot.class));
    }
  }

  @Override
  public Receive createReceive() {
    return emptyBehavior();
  }

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create();
    system.actorOf(Props.create(Boot.class)).tell(new BootMsg(), Actor.noSender());
  }
}
