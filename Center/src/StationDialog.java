import java.io.*;
import java.net.Socket;

/**
 * Created by rotem on 12/06/2017.
 */
public class StationDialog extends Thread {
    Socket client;
    StationServer myServer;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    PassengerDialogWin myOutput;
    MessageManager mesMan;

    private int stopNumber;

    public StationDialog(Socket clientSocket, StationServer myServer, MessageManager getMesMan)
    {
        this.mesMan = getMesMan;
        client = clientSocket;
        this.myServer = myServer;
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

            //The first line the station sends is it's ID
            String stationID = bufferSocketIn.readLine();
            // Extract information
            String[] words = stationID.split("\\s ");

            stopNumber = Integer.parseInt(words[4]);

            //TODO: create a custom event for each station and send it to the MessageManager
        } catch (IOException e)
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
        myOutput = new PassengerDialogWin("Dialog Win for station: " + this.stopNumber, this);
        // What is this starting? Should it be "run()"?
        start();
    }

    public void run()
    {
        String line;
        boolean stop = false;
        try
        {
            while (true)
            {
                //TODO: wait for events and let the StationClient know
                line = bufferSocketIn.readLine();
                if (line == null)
                    break;
                if (line.equals("end"))
                    break;
                myOutput.printOther(line);
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