import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by rotem on 12/06/2017.
 */
public class BusClient {

    //    String SERVERHOST = "147.161.105.71";

    String SERVERHOST = "127.0.0.1";
    int DEFAULT_PORT = 771;
    Socket clientSocket = null;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    BufferedReader keyBoard;
    BusClientWin myOutput;
    String line;




    public void doit()
    {
        try
        {
            // request to server
            clientSocket = new Socket(SERVERHOST, DEFAULT_PORT);

            // Init streams to read/write text in this socket
            bufferSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            bufferSocketOut = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    clientSocket.getOutputStream())), true);


//  	   Init streams to read text from the keyboard
//	   keyBoard = new BufferedReader(
//	   new InputStreamReader(System.in));


            myOutput = new BusClientWin("Client  ", this);

            // notice about the connection
            myOutput.printMe("Connected to " + clientSocket.getInetAddress() +
                    ":" + clientSocket.getPort());


            /*
            // request line to input bus line number from user
            myOutput.printMe("Please enter the desired line number for this bus (0-5).");
            line = bufferSocketIn.readLine();
            try {
                lineNumber = Integer.parseInt(line.trim());
                if (lineNumber > 5 || lineNumber < 0) lineNumber = 0;
            }
            catch(Exception e) {
                myOutput.printMe("The value you have entered is illegal.\nBus line number is set to 0.");
                lineNumber = 0;
            }**/
            // request line to input bus line number from user
            myOutput.printMe("Please enter the desired line number for this bus (0-5) and an ID.");

            // get the routes
            line = bufferSocketIn.readLine();
            String[] stops = line.split(" ");

            //Drive through route
            for (int i = 0; i < stops.length; i ++) {
                // Tell busClient we reached station #
                bufferSocketOut.println(stops[i]);
                 try {
                     Event64 evTime = new Event64();
                     new MyTimer72(5000, evTime);
                 }
                 catch (Exception e) {
                     System.out.print(e.getMessage());
                 }
            }

            while (true)
            {
                line = bufferSocketIn.readLine(); // reads a line from the server
                if (line == null)  // connection is closed ?  exit
                {
                    myOutput.printMe("Connection closed by the Server.");
                    break;
                }
                // TODO: kill bus when it reaches the end of the route
                myOutput.printOther(line); // shows it on the screen
                if (line.equals("end"))
                {
                    break;
                }
            }
        } catch (IOException e)
        {
            myOutput.printMe(e.toString());
            System.err.println(e);
        } finally
        {
            try
            {
                if (clientSocket != null)
                {
                    clientSocket.close();
                }
            } catch (IOException e2)
            {
            }
        }
        myOutput.printMe("end of bus client");
        myOutput.send.setText("Close");

        System.out.println("end of bus client");
    }

    public static void main(String[] args)
    {
        BusClient client = new BusClient();
        client.doit();
    }

}
