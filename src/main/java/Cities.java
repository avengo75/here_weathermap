import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cities {

    public static final String[] CITIES = {"Tel Aviv", "Singapore", "Auckland", "Ushuaia", "Miami", "London", "Berlin", "Reykjavik", "Cape Town", "Kathmandu"};
    public static final String APPID = "2129b0d4a034345575dc2dca4e05a2e6";
    public static final String URL = "http://api.openweathermap.org/data/2.5/weather/";

    public static void main(String[] args) {

        Response response;
        int currentMax = 0;
        int currentMin = 999999999;
        int daylite = 0;
        ArrayList<String> maxdaylight = new ArrayList<String>();
        ArrayList<String> mindaylight = new ArrayList<String>();
        Map responses = new HashMap();
        Float tempK;
        Float tempC;
        for (String city : CITIES) {
            response = given().params("APPID", APPID, "q", city).get(URL);
            responses.put(city, response.jsonPath().getMap("main").get("temp"));
            Map sys = response.jsonPath().getMap("sys");
            int sunrise = (Integer) sys.get("sunrise");
            int sunset = (Integer) sys.get("sunset");
            daylite = sunset - sunrise;
// Consider theoretical probability of having several cities with minimal or maximal daylight time
            if (daylite > currentMax) {
                maxdaylight.clear();
            }
            if (daylite >= currentMax) {
                maxdaylight.add(city);
                currentMax = daylite;
            }
            if (daylite < currentMin) {
                mindaylight.clear();
            }
            if (daylite <= currentMin) {
                mindaylight.add(city);
                currentMin = daylite;
            }
        }

        System.out.println("Cities with the longest daylight:  ");
        for (String cityMax : maxdaylight) {
            tempK = (Float) responses.get(cityMax);
            tempC = (Math.round(tempK * 100 - 27315)) / 100f;
            System.out.println(cityMax + ": Temperature = " + tempK + "K = " + tempC + "°C  \n");
        }

        System.out.println("Cities with the shortest daylight:  ");
        for (String cityMin : mindaylight) {
            tempK = (Float) responses.get(cityMin);
            tempC = (Math.round(tempK * 100 - 27315)) / 100f;
            System.out.println(cityMin + ": Temperature = " + tempK + "K = " + tempC + "°C");
        }

    }

}
