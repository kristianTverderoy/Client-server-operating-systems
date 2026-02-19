package org.ntnu.ST;

import org.ntnu.util.ArithmeticOperations;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadedServer {

  private final int port;
  private boolean isOn = false;
  // private Queue<Socket> connectedClients = new ArrayDeque<>();
  private Socket socket;
  private BufferedReader br;
  private BufferedWriter bw;

  public SingleThreadedServer(int port) {
    if (port < 0 || port > 65535) {
      throw new IllegalArgumentException("Invalid port number. Must be between 0 and 65535");
    }
    this.port = port;
  }

  public void run() {
    if (!isOn) {
      startServer();
    }

    try (ServerSocket ss = new ServerSocket(port)) {

      while (isOn) {
        System.out.println("Server is listening on port" + port);
        if (this.socket == null) {
          this.socket = ss.accept();
          this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
          this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        }
        System.out.println("New client connected: " + this.socket.getInetAddress().getHostAddress());
        handleClient();
        // socket.close();
      }
    } catch (IOException e) {
      System.err.println("Socket error");
    }

  }

  private synchronized void startServer() {
    this.isOn = true;
  }

  private void handleClient() {

    try {

      String messageFromClient = this.br.readLine();
      if (messageFromClient != null) {


      if (!messageFromClient.equals("exit")) {
        String[] parts = messageFromClient.split(" ");
        double a = Double.parseDouble(parts[0]);
        double b = Double.parseDouble(parts[2]);
        String messageResponse = "You didnt follow the correct pattern to write to the server.";

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
        }
        try {
          Thread.sleep(2000);
          this.bw.write(messageResponse);
          this.bw.newLine();
          bw.flush();
        } catch (InterruptedException e) {
          System.err.println("Could not sleep the current thread.");
        }

      } else if (messageFromClient.equals("exit")) {
        {
          socket.close();
          this.socket = null;
          this.br = null;
          this.bw = null;
        }
      }
      }
    } catch (IOException e) {
      System.err.println("No socket input/output stream");
    }

  }

}
