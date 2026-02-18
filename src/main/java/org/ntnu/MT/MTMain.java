package org.ntnu.MT;


public class MTMain
{

  public static void main( String[] args ) {
    int port;
    if (args.length == 1) {
      port = Integer.parseInt(args[0]);
    } else {
      port = 8080;
    }

    MultiThreadedServer mtServer = new MultiThreadedServer(port);
    mtServer.run();
  }
}