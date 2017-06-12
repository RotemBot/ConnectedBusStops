import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rotem on 12/06/2017.
 */
public class BusServer extends Thread {
    private int DEFAULT_PORT = 771;
    private ServerSocket listenSocket;
    private Socket clientSockets;

    public BusServer()   // constructor of a TCP server
    {
        try
        {
            listenSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e)    //error
        {
            System.out.println("Problem creating the server-socket");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Bus server starts on port " + DEFAULT_PORT);
        start();
    }

    public void run()
    {
        try
        {
            while (true)
            {
                clientSockets = listenSocket.accept();
                new BusDialog(clientSockets, this);
            }

        } catch (IOException e)
        {
            System.out.println("Problem listening server-socket");
            System.exit(1);
        }

        System.out.println("end of server");
    }
}

