package org.ntnu.client;

public class ClientMain {

  public static void main(String[] args) {
    long startTime = System.nanoTime();

    for (int i = 0; i < 10; i++) {
      Client client = new Client(8080, "127.0.0.1");
    }


    System.out.println("pepe");

    long endTime = System.nanoTime();
    long durationInNanos = endTime - startTime;
    long durationInMillis = durationInNanos / 1_000_000;

    System.out.println("Execution time: " + durationInMillis + " ms");
    System.out.println("Execution time: " + durationInNanos + " ns");
  }
}