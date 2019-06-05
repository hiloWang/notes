package book.java.tcpip.program.chapter4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPEchoServerPool {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) { // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Port> <Threads>");
        }

        int echoServPort = Integer.parseInt(args[0]); // Server port
        int threadPoolSize = Integer.parseInt(args[1]);

        // Create a server socket to accept client connection requests
        final ServerSocket servSock = new ServerSocket(echoServPort);

        final Logger logger = Logger.getLogger("practical");

        // Spawn a fixed number of threads to service clients
        for (int i = 0; i < threadPoolSize; i++) {
            Thread thread = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Socket clntSock = servSock.accept(); // Wait for a connection
                            EchoProtocol.handleEchoClient(clntSock, logger); // Handle it
                        } catch (IOException ex) {
                            logger.log(Level.WARNING, "Client accept failed", ex);
                        }
                    }
                }
            };
            thread.start();
            logger.info("Created and started Thread = " + thread.getName());
        }
    }
}