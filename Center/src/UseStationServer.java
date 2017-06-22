/**
 * Created by rotem on 12/06/2017.
 */
import java.io.*;
import java.net.*;

public class UseStationServer {
    public static void main(String[] arg)
    {
        //Create the message manager
        MessageManager mesMan = new MessageManager();

        //Create servers for bus-stops and buses
        new StationServer(mesMan);
        new BusServer(mesMan);


    }
}
