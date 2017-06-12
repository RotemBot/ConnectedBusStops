/**
 * Created by rotem on 12/06/2017.
 */
import java.io.*;
import java.net.*;

public class UseStationServer {
    public static void main(String[] arg)
    {
        //Create servers for bus-stops and buses
        new StationServer();
        new BusServer();

        //Create the message manager
        new MessageManager();
    }
}
