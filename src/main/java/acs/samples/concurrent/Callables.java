package acs.samples.concurrent;

import java.util.concurrent.*;

// Initial steps from
// https://www.callicoder.com/java-callable-and-future-tutorial/
// https://www.callicoder.com/java-8-completablefuture-tutorial/

class FutureAndCallableExample {

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Callable<String> callable = () -> {
      // Perform some computation
      System.out.println("Entered Callable");
      Thread.sleep(2000);
      return "Hello from Callable";
    };

    System.out.println("Submitting Callable");
    Future<String> future = executorService.submit(callable);

    // This line executes immediately
    System.out.println("Do something else while callable is getting executed");

    System.out.println("Retrieve the result of the future");
    // Future.get() blocks until the result is available
    String result = future.get();
    System.out.println(result);

    executorService.shutdown();
  }
}


public class Callables {

  // Using a method
  Callable<String> callable = new Callable<String>() {
    @Override
    public String call() throws Exception {
      // Perform some computation
      Thread.sleep(2000);
      return "Return some result";
    }
  };

  // Using a Lambda
  Callable<String> callableLambda = () -> {
    // Perform some computation
    Thread.sleep(2000);
    return "Return some result";
  };



  public static void main(String[] args)  {
    System.out.println("Playing with Callables ...");
  }

}
