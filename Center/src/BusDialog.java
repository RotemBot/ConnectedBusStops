import java.io.*;
import java.net.Socket;

/**
 * Created by rotem on 12/06/2017.
 */
public class BusDialog extends Thread {
    Socket client;
    BusServer myServer;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    BusDialogWin myOutput;
    private MessageManager mesMan;

    int busId;
    int lineNumber;
    int[] route;

    public BusDialog(Socket clientSocket, BusServer myServer, MessageManager getMesMan)
    {
        this.mesMan = getMesMan;
        client = clientSocket;
        this.myServer = myServer;

        myOutput = new BusDialogWin("Dialog Win for: " + client.toString(), this);

        try
        {
            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    clientSocket.getOutputStream())), true);

            // The first line the bus sends its identification
            String identification = bufferSocketIn.readLine();
            // Extract information
            String[] words = identification.split(" ");

            busId = Integer.parseInt(words[1]);
            lineNumber = Integer.parseInt(words[0]);

            // get the route for this specific bus line
            route = mesMan.getRoute(lineNumber);

            String routeString = "";

            // Convert the route to String, to send over socket
            for(int stop : route) {
                routeString += stop;
                routeString += " ";
            }

            // send the route back to the bus
            bufferSocketOut.println(routeString.trim());


        }
        catch (IOException e)
        {
            try
            {
                client.close();
            } catch (IOException e2)
            {
            }
            System.err.println("server:Exception when opening sockets: " + e);
            return;
        }
        // myOutput = new BusDialogWin("Dialog Win for: " + client.toString(), this);

        start();
    }

    public void run()
    {
        String line;
        boolean stop=false;



        try
        {

            while (true)
            {
                line = bufferSocketIn.readLine();
                if (line == null)
                    break;
                if (line.equals("end"))
                    break;
                mesMan.updateBusPosition(this.lineNumber, Integer.parseInt(line), this.busId);
                myOutput.printOther("Bus " + this.lineNumber + " is at station " + line);
            }
        } catch (IOException e)
        {
        } finally
        {
            try
            {
                client.close();
            } catch (IOException e2)
            {
            }
        }

        myOutput.printMe("end of  dialog ");
        myOutput.send.setText("Close");

    }

    void exit()
    {
        try
        {
            client.close();
        } catch (IOException e2)
        {
        }
    }
}
