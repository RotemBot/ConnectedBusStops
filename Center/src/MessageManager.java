import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rotem on 12/06/2017.
 */
public class MessageManager {
    private int[][] routes = {
            {0,1,6},
            {2,5,6},
            {1,7,8,9},
            {0,3,9},
            {1,4,5,7},
            {2,3,8}

    };

    //TODO: ADD Map<String,Event64> to manage connection with the stations
    Map<Integer,Event64> stationsUpdateEv;


    public MessageManager() {

        //TODO: wait for updates from buses and inform the stations
        stationsUpdateEv = new HashMap<Integer,Event64>();
    }

    /**
     * Returns the line#'s corresponding route
     * @param busLine - Bus line number
     * @return - An array of bus stop numbers
     */
    public int[] getRoute(int busLine) {
        return routes[busLine];
    }

    public void updateBusPosition(int lineNumber, int stop, int busID) {

        int[] route = routes[lineNumber];

        int interval = 0;
        boolean position = false;
        for (int station: route) {
            if(!position) {
                if (station == stop) position = true;
            }

            else {
                stationsUpdateEv.get(station).sendEvent(interval + " " + lineNumber + " " + busID);
                interval ++;
            }
        }

    }
}
