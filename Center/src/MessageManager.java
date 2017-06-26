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

    public MessageManager() {

        //TODO: wait for updates from buses and inform the stations

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
        int temp = 0;

    }
}
