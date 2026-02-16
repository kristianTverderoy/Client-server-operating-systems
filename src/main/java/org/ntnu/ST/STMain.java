package org.ntnu.ST;


import org.ntnu.client.Client;

import java.util.ArrayList;
import java.util.List;

public class STMain
{

    public static void main(String[] args) {
        int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        SingleThreadedServer stServer = new SingleThreadedServer(port);
        stServer.run();


    }
}
