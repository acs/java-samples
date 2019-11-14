package acs.samples.concurrent;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.lang.System.out;
import static java.lang.System.in;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import static akka.pattern.Patterns.ask;
// import static akka.pattern.Patterns.pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class LongTask {}
class LongTask1 {}

class Message {
  private final String message;

  Message(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

class Receiver extends AbstractActor {
  private static Logger log = LoggerFactory.getLogger(Receiver.class);

  // private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  @Override
  public Receive createReceive() {
    return receiveBuilder()
      .match(String.class, s -> {
        out.println("Received String message: " + s);
        sender().tell("--> Received correctly " + s, self());
      })
      .match(LongTask.class, s -> {
        out.println("Long Task ... sleeping");
        // Thread.sleep(1000);
        sender().tell("LONG OK1", self());
        sender().tell("LONG OK2", self());
      })
      .matchAny(o -> {out.println("received unknown message"); throw new Exception("UNKNOWN!");})
      .build();
  }
}

class Sender extends AbstractActor {
  @Override
  public Receive createReceive() {
    return receiveBuilder().
      match(String.class, s -> {out.println("** Sender received " + s);}).
      build();
  }
}


public class ActorsClassic {

  private static Logger logger = LoggerFactory.getLogger(ActorsClassic.class);

  private static void terminateSystemWithPress(ActorSystem system) throws IOException {
    out.println("Press any key to terminate");
    in.read();
    out.println("Shutting down actor system...");
    system.terminate();
  }

  public static void main (String[] args) throws IOException {

    out.println("Creating the actor demo");
    ActorSystem system = ActorSystem.create("demo");
    ActorRef receiverActor = system.actorOf(Props.create(Receiver.class));
    ActorRef senderActor = system.actorOf(Props.create(Sender.class));

    // Send a message
    out.println("Sending a message to receiver");
    // receiverActor.tell("Hello good friend", ActorRef.noSender());
    receiverActor.tell("Sent this message please", senderActor);
    CompletionStage<Object> future =
      ask(receiverActor, new LongTask(), Duration.ofMillis(1000)).
      whenComplete(
        // res is filled with the first message from the receiver when addressing this ask message
        (res, error) -> {
          out.println("Ask answered: " + res);
        });
    receiverActor.tell(new LongTask(), senderActor);
    receiverActor.tell(new LongTask1(), ActorRef.noSender());

    terminateSystemWithPress(system);
  }
}