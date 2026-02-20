package org.ntnu.client;

import java.util.ArrayList;

public class ClientMain {

  public static void main(String[] args) {
    long startTime = System.nanoTime();
    ArrayList<Thread> threads = new ArrayList<>();



    for (int i = 0; i < 10; i++) {
      Thread t = new Thread(() -> {
        Client client = new Client(8080, "127.0.0.1");
      });
      threads.add(t);
      t.start();
    }

    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        System.err.println("Thread was interrupted");
      }
    }


    System.out.println("pepe");

    long endTime = System.nanoTime();
    long durationInNanos = endTime - startTime;
    long durationInMillis = durationInNanos / 1_000_000;

    System.out.println("Execution time: " + durationInMillis + " ms");
    System.out.println("Execution time: " + durationInNanos + " ns");
  }
}