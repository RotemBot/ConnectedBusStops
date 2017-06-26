import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rotem on 12/06/2017.
 */
public class StationServer extends Thread {
    int DEFAULT_PORT = 770;
    ServerSocket listenSocket;
    Socket clientSockets;
    MessageManager mesMan;
    Event64[] stopsEvents;
    static int i = 0;

    public StationServer(MessageManager mesMan)   // constructor of a TCP server
    {
        this.mesMan = mesMan;

        try
        {
            listenSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e)    //error
        {
            System.out.println("Problem creating the server-socket");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Station server starts on port " + DEFAULT_PORT);
        start();
    }

    public void run()
    {
        try
        {
            while (true)
            {
                clientSockets = listenSocket.accept();
                stopsEvents[i] = new Event64();
                new StationDialog(clientSockets, this, this.mesMan, stopsEvents[i]);
                i++;
            }

        } catch (IOException e)
        {
            System.out.println("Problem listening server-socket");
            System.exit(1);
        }

        System.out.println("end of server");
    }
}
