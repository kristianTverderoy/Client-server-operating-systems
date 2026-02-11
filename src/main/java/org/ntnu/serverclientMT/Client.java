package org.ntnu.serverclientMT;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

  private int port;
  private String host;
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private ExecutorService executors;


  public Client(int port, String host) {

    this.port = port;
    this.host = host;
    try {
      this.socket = new Socket(host, port);
      connectToServer(this.socket);
    } catch (UnknownHostException e) {
      //TODO fill in exceptions

    } catch (IOException e) {

    }


  }

  public void connectToServer(Socket socket) {


    Scanner scanner = new Scanner(System.in);
    try {
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException e) {
      System.err.println("Could not connect to server");
    }

//     this.executors = Executors.newFixedThreadPool(10);
//     Runnable task = () -> {
//       while (socket.isConnected()) {
//         bufferedWriter.write();
//         bufferedReader.
//       }
//     };
//     this.executors.submit(task);


    if (socket.isConnected()) {
      try {
        String message = scanner.nextLine();
        if (message != null && !message.isEmpty()) {
          this.bufferedWriter.write(message);
          this.bufferedWriter.newLine();
          this.bufferedWriter.flush();
        }

      } catch (IOException e) {
        System.err.println("Socket is not connected");
      }
    }

    try  {
      boolean ready = this.bufferedReader.ready();
      if (ready) {
        System.out.println(bufferedReader.readLine());
      }
    } catch (IOException e) {
      System.err.println("Buffered reader does not exist");

    }
  }


}






