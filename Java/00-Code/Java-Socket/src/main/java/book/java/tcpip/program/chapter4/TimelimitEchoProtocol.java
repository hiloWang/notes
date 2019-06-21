package book.java.tcpip.program.chapter4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class TimelimitEchoProtocol implements Runnable {

    private static final int BUFSIZE = 32;  // Size (bytes) buffer
    private static final String TIMELIMIT = "10000";  // Default limit (ms)
    private static final String TIMELIMITPROP = "Timelimit";  // Thread property

    private static int timelimit;
    private Socket clntSock;
    private Logger logger;

    public TimelimitEchoProtocol(Socket clntSock, Logger logger) {
        this.clntSock = clntSock;
        this.logger = logger;
        // Get the time limit from the System properties or take the default
        timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP, TIMELIMIT));
    }

    public static void handleEchoClient(Socket clntSock, Logger logger) {

        try {
            // Get the input and output I/O streams from socket
            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();
            int recvMsgSize;                        // Size of received message
            int totalBytesEchoed = 0;               // Bytes received from client
            byte[] echoBuffer = new byte[BUFSIZE];  // Receive buffer
            long endTime = System.currentTimeMillis() + timelimit;
            int timeBoundMillis = timelimit;

            clntSock.setSoTimeout(timeBoundMillis);
            // Receive until client closes connection, indicated by -1
            while ((timeBoundMillis > 0) &&     // catch zero values
                    ((recvMsgSize = in.read(echoBuffer)) != -1)) {
                out.write(echoBuffer, 0, recvMsgSize);
                totalBytesEchoed += recvMsgSize;
                timeBoundMillis = (int) (endTime - System.currentTimeMillis());
                clntSock.setSoTimeout(timeBoundMillis);
            }
            logger.info("Client " + clntSock.getRemoteSocketAddress() +
                    ", echoed " + totalBytesEchoed + " bytes.");
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Exception in echo protocol", ex);
        }
    }

    public void run() {
        handleEchoClient(this.clntSock, this.logger);
    }
}