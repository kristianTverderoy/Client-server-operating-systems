package org.ntnu.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

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

  private void connectToServer(Socket socket) {


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

    while (!socket.isClosed()) {


        String message = scanner.nextLine();

//      String message = "exit";
//      for (int i = 0; i < 5; i++) {
//        message = "2 + 2";
//        if (i == 4) {
          if (message.equalsIgnoreCase("exit")){
          try {
            bufferedWriter.write("exit");
            bufferedWriter.flush();
            this.socket.close();
            break;
          } catch (IOException e) {
            System.err.println("We dont associate with that server anymore.");
          }
        }

        try {

          assert this.bufferedWriter != null;
          this.bufferedWriter.write(message);
          this.bufferedWriter.newLine();
          this.bufferedWriter.flush();


        } catch (IOException e) {
          System.err.println("Socket is not connected");
        }
        try {
          assert this.bufferedReader != null;
          String response = this.bufferedReader.readLine();
          if (response != null) {
            System.out.println(response);
          } else {
            // MultiThreadedServer closed connection
            break;
          }
        } catch (IOException e) {
          System.err.println("Error reading from server");
          break;
        }
      }
    }


  }







