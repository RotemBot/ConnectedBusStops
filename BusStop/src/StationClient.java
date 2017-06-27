import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Created by rotem on 12/06/2017.
 */
public class StationClient {
    //    String SERVERHOST = "147.161.105.71";

    String SERVERHOST = "127.0.0.1";
    int DEFAULT_PORT = 770;
    Socket clientSocket = null;
    BufferedReader bufferSocketIn;
    PrintWriter bufferSocketOut;
    BufferedReader keyBoard;
    StationClientWin myOutput;
    String line;
    private int stationId;
    // manage bus arrival times
    // pairs of <busId : interval> mapped to bus lines.
    private Map<Integer ,Map<Integer, Integer>> lines = new HashMap<>();



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


            myOutput = new StationClientWin("Client  ", this);

            // notice about the connection
            myOutput.printMe("Connected to " + clientSocket.getInetAddress() +
                    ":" + clientSocket.getPort());

            myOutput.printMe("Please enter station number");


            while (true)
            {
                line = bufferSocketIn.readLine(); // reads a line from the server
                if (line == null)  // connection is closed ?  exit
                {
                    myOutput.printMe("Connection closed by the Server.");
                    break;
                }
                myOutput.printMe("Got new data");
                updateMessageBoard(line);
                //myOutput.printOther(line); // shows it on the screen
                printBusStatus();

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
        myOutput.printMe("end of client ");
        myOutput.send.setText("Close");

        System.out.println("end of client ");
    }

    private void printBusStatus() {

        ArrayList<Integer> lineNums = new ArrayList<>(lines.keySet());
        for (int i = 0; i < lines.size(); i ++) {
            // get the line number
            int lineNum = lineNums.get(i);
            // get the map of bus IDs and intervals
            Map queue = lines.get(lineNum);
            ArrayList<Integer> busQue = new ArrayList<>(queue.keySet());

            String toPrint = "Line: "+ lineNum + " ";
            boolean first = true;

            for (int j = 0; j < queue.size(); j ++) {
                int bus = busQue.get(j);
                Integer interval = (Integer) queue.get(bus);
                String time;

                if (interval == 0)  {
                    time = "Now";
                    // Delete the bus that reached the station
                    lines.get(lineNum).remove(bus);
                }

                else time = (interval * 5) + "s";

                if (first) {
                    toPrint += time;
                    first = false;
                }

                else toPrint += ", " + time;
            }

            myOutput.printMe(toPrint);

        }
    }

    private void updateMessageBoard(String data) {
        String[] details = data.split(" ");
        Integer interval = Integer.parseInt(details[0]);
        Integer busLine = Integer.parseInt(details[1]);
        Integer busID = Integer.parseInt(details[2]);

        Map<Integer, Integer> queue = new HashMap<>();
        queue.put(busID, interval);

        if (lines.isEmpty()) {
            lines.put(busLine, queue);
        }

        else if (!lines.containsKey(busLine)) {
            lines.put(busLine, queue);
        }

        else {
            Map waiting = lines.get(busLine);
            if (waiting.isEmpty()) {
                waiting.put(busID, interval);
            }
            else if (!waiting.containsKey(busID)) {
                waiting.put(busID, interval);
            }

            else {
                waiting.replace(busID, interval);
            }
        }

    }

    public static void main(String[] args)
    {
        StationClient station = new StationClient();
        station.doit();

    }

}
