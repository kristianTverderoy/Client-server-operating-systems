package org.ntnu.MT;

import org.ntnu.client.Client;
import org.ntnu.util.ArithmeticOperations;

import java.io.*;
import java.lang.classfile.CodeBuilder.CatchBuilder;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MultiThreadedServer {

  private volatile boolean isOn = false;
  private List<Socket> connectedClients = new ArrayList<>();
  private final int processPort;

  public MultiThreadedServer(int processPort) {
    if (processPort < 0 || processPort > 65535) {
      throw new IllegalArgumentException("Invalid port number.");
    }

    this.processPort = processPort;
  }

  public void run() {
    if (!isOn) {
      startServer();
    }

    try {
      ServerSocket ss = new ServerSocket(processPort);
      new Thread(() -> acceptNewClient(ss)).start();

      System.out.println("Server is listening on port" + processPort);

    } catch (IOException e) {
      System.err.println("Socket error");
    }

    while (isOn) {
      Thread.onSpinWait();
    }
  }

  public void acceptNewClient(ServerSocket serverSocket) {
    while (isOn) {
      try {
        Socket socket = serverSocket.accept();
        new Thread(() -> interactWithClient(socket)).start();

      } catch (IOException e) {
        System.err.println("There was an error accepting the client");
      }
    }

  }

  private void listenForNoMoreClients() {

    while (isOn) {
      int activeThreads = Thread.activeCount();
      if (activeThreads < 3) {
        isOn = false;
        System.out.println("Server shutting off.");
      }
    }
  }

  public void startServer() {
    this.isOn = true;
    new Thread(this::listenForNoMoreClients).start();
  }

  public void interactWithClient(Socket socket) {

    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
      while (!socket.isClosed()) {
        String messageFromClient = bufferedReader.readLine();
        if (messageFromClient != null) {
          if (!messageFromClient.equals("exit")) {
            String[] parts = messageFromClient.split(" ");
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[2]);

            String messageResponse;

            switch (parts[1]) {
              case "+": {
                double addResult = ArithmeticOperations.add(a, b);
                messageResponse = String.valueOf(addResult);
                break;
              }
              case "-": {
                double addResult = ArithmeticOperations.sub(a, b);
                messageResponse = String.valueOf(addResult);
                break;
              }
              case "*": {
                double addResult = ArithmeticOperations.mul(a, b);
                messageResponse = String.valueOf(addResult);
                break;
              }
              case "/": {
                double addResult = ArithmeticOperations.div(a, b);
                messageResponse = String.valueOf(addResult);
                break;
              }

              case "exit": {
                try {
                  messageResponse = "Goodbye!";
                  bufferedWriter.write(messageResponse);
                  bufferedWriter.newLine();
                  bufferedWriter.flush();
                  socket.close();
                  break;
                } catch (IOException e) {
                  System.err.println("Could not close the socket connection.");
                }
              }
              default: {
                messageResponse = "You didnt follow the correct pattern to write to the server.";
                break;
              }
            }

            try {
              Thread.sleep(2000);
              bufferedWriter.write(messageResponse);
              bufferedWriter.newLine();
              bufferedWriter.flush();
            } catch (InterruptedException e) {
              System.err.println("Could not sleep the current thread.");
            }

          }

        }
      }
    } catch (IOException e) {
      System.err.println("No socket input/output stream");
    }
  }
}
